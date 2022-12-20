package com.rabbitminers.createtools.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.createtools.tools.ToolBase;
import com.rabbitminers.createtools.tools.tooltypes.base.DrillTool;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

@Mixin(LevelRenderer.class)
@OnlyIn(Dist.CLIENT)
public class WorldRendererMixin {
    @Inject(at = @At("HEAD"), method = "renderHitOutline", cancellable = true)
    private void drawBlockOutline(PoseStack stack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        ItemStack heldStack = mc.player.getMainHandItem();
        if (heldStack.getItem() instanceof ToolBase tool && (tool.getRPM(mc.player.getMainHandItem()) <= 0 || tool instanceof DrillTool)) {
            ci.cancel();
        }
    }
}