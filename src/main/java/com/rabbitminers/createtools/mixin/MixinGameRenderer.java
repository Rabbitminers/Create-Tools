package com.rabbitminers.createtools.mixin;

import com.rabbitminers.createtools.handler.InputHandler;
import com.rabbitminers.createtools.tooldata.CTComponents;
import com.rabbitminers.createtools.tools.ToolBase;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.player.inventory.Hotbar;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
@OnlyIn(Dist.CLIENT)
public abstract class MixinGameRenderer {
    @Shadow public abstract void render(float p_109094_, long p_109095_, boolean p_109096_);

    @Inject(at = @At("RETURN"), method = "getFov", cancellable = true)
    public void onGetFOVModifier(Camera c, float partial, boolean useFOVSetting, CallbackInfoReturnable<Double> info) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        ItemStack heldStack = mc.player.getMainHandItem();
        double defaultFOV = info.getReturnValue();

        if (heldStack.getItem() instanceof ToolBase toolBase
            && mc.player.isUsingItem()
            && toolBase.toolContainsComponent(heldStack, CTComponents.SPYGLASS
        )) {
            info.setReturnValue(defaultFOV/4.5f);
        }
    }
}
