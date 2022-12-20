package com.rabbitminers.createtools.render.tools.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.rabbitminers.createtools.handler.BlazeBurnerFloatHandler;
import com.rabbitminers.createtools.handler.DeployerAnimationHandler;
import com.rabbitminers.createtools.handler.DeployerArmHandler;
import com.rabbitminers.createtools.render.custom.CreateToolsCustomRenderedItemModel;
import com.rabbitminers.createtools.render.tools.model.DeployerToolModel;
import com.rabbitminers.createtools.render.tools.model.DrillToolModel;
import com.rabbitminers.createtools.tools.tooltypes.base.DeployerTool;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueHandler;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class DeployerToolRender extends CustomRenderedItemModelRenderer<DeployerToolModel> {
    @Override
    protected void render(ItemStack stack, DeployerToolModel model, PartialItemModelRenderer renderer, ItemTransforms.TransformType transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!(stack.getItem() instanceof DeployerTool deployerTool))
            return;

        CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();

        if (nbt == null || !nbt.hasUUID("toolid"))
            return;

        if (!DeployerArmHandler.deployerHasAnimationHandler(nbt.getUUID("toolid")))
            DeployerArmHandler.add(nbt.getUUID("toolid"));

        DeployerTool.DeployerMode mode = DeployerTool.DeployerMode.of(stack);

        renderer.render(model.getOriginalModel(), light);
        renderer.renderSolid(model.getPartial("handle"), light);
        float xOffset = -1/16f;

        ms.popPose();
        ms.pushPose();

        ms.translate(0.5, 0.5, 0.5);
        DeployerAnimationHandler animationHandler = DeployerArmHandler.getAnimationHandlerOfUUID(nbt.getUUID("toolid"));
        if (animationHandler == null)
            return;
        animationHandler.renderTick();
        ms.translate(0, 0, 0.15-Math.sin(animationHandler.getZoffSet()));

        renderer.renderSolid(model.getPartial("arm"), light);

        renderer.renderSolid(model.getPartial(switch (mode) {
            case DOLLY -> "dolly";
            case ATTACK -> "attack";
            default -> "place";
        }), light);

        ms.popPose();
        ms.pushPose();

        ms.translate(0.5, 0.5, 0.5);

        ms.translate(-xOffset, 0, 0);
        ms.mulPose(Vector3f.ZP.rotationDegrees(ScrollValueHandler.getScroll(AnimationTickHolder.getPartialTicks())));
        ms.translate(xOffset, 0, 0);

        renderer.render(model.getPartial("cog"), light);

        ms.popPose();
        ms.pushPose();
    }

    @Override
    public DeployerToolModel createModel(BakedModel originalModel) {
        return new DeployerToolModel(originalModel);
    }
}
