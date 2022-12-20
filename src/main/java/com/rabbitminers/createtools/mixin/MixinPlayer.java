package com.rabbitminers.createtools.mixin;

import com.rabbitminers.createtools.tools.tooltypes.base.DrillTool;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Player.class)
public class MixinPlayer {
    @Inject(at = @At("RETURN"), method = "getDigSpeed", cancellable = true, remap = false)
    public void getDigSpeed(BlockState blockState, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        ItemStack heldStack = mc.player.getMainHandItem();
        if (heldStack.getItem() instanceof DrillTool tool && tool.getRPM(heldStack) <= 0) {
            float f = cir.getReturnValue();
            cir.setReturnValue(0.0f);
        }
    }
}
