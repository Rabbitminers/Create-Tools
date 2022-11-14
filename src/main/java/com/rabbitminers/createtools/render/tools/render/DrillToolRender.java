package com.rabbitminers.createtools.render.tools.render;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.rabbitminers.createtools.handler.BlazeBurnerFloatHandler;
import com.rabbitminers.createtools.handler.DrillHeadRotationHandler;
import com.rabbitminers.createtools.handler.GeneratorHandler;
import com.rabbitminers.createtools.handler.WindmillRotationAngleHandler;
import com.rabbitminers.createtools.render.tools.model.DrillToolModel;
import com.rabbitminers.createtools.tooldata.CTComponents;
import com.rabbitminers.createtools.tooldata.CTGeneratorTypes;
import com.rabbitminers.createtools.tools.ToolBase;
import com.rabbitminers.createtools.tools.generators.Generator;
import com.rabbitminers.createtools.util.ToolUtils;
import com.simibubi.create.content.curiosities.tools.ExtendoGripModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueHandler;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class DrillToolRender extends CustomRenderedItemModelRenderer<DrillToolModel> {
    @Override
    protected void render(ItemStack stack, DrillToolModel model, PartialItemModelRenderer renderer, ItemTransforms.TransformType transformType,
                          PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        renderer.render(model.getOriginalModel(), light);

        renderer.renderSolid(model.getPartial("handle"), light);

        float xOffset = -1/16f;
        ms.translate(-xOffset, 0, 0);
        ms.mulPose(Vector3f.ZP.rotationDegrees(ScrollValueHandler.getScroll(AnimationTickHolder.getPartialTicks())));
        ms.translate(xOffset, 0, 0);

        CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();

        if (stack.getItem() instanceof ToolBase && ToolUtils.toolContainsComponent(stack, CTComponents.FLYWHEEL))
            renderer.render(model.getPartial("flywheel"), light);
        else
            renderer.render(model.getPartial("cog"), light);

        ms.popPose();
        ms.pushPose();

        ms.translate(0.5, 0.5, 0.5);

        ms.translate(-xOffset, 0, 0);

        if (nbt.hasUUID("toolid") && !Minecraft.getInstance().isPaused()) {
            DrillHeadRotationHandler.update(nbt.getUUID("toolid"));
            ms.mulPose(Vector3f.ZP.rotationDegrees(DrillHeadRotationHandler.getRotationOfUUID(nbt.getUUID("toolid"))));
        }

        ms.translate(xOffset, 0, 0);

        renderer.renderSolid(model.getPartial("head"), light);

        ms.popPose();
        ms.pushPose();

        if (stack.getItem() instanceof ToolBase && nbt.hasUUID("toolid")) {
            if (ToolUtils.toolContainsComponent(stack, CTComponents.BLAZE_BURNER)) {
                ms.translate(0.5, 0.5, 0.5);

                if (!ToolUtils.toolContainsComponent(stack, CTComponents.MILL_STONE))
                    renderer.render(model.getPartial("cage"), light);

                ms.popPose();
                ms.pushPose();

                ms.translate(0.5, 0.5, 0.5);

                BlazeBurnerFloatHandler.update(nbt.getUUID("toolid"));

                ms.translate(0,  Math.sin(BlazeBurnerFloatHandler.getHeightOfUUID(nbt.getUUID("toolid"))), 0);

                renderer.renderSolid(model.getPartial("blaze"), light);

                ms.popPose();
                ms.pushPose();
            }

            if (ToolUtils.toolContainsComponent(stack, CTComponents.SPYGLASS)) {
                ms.translate(0.5, 0.5, 0.5);

                renderer.renderSolid(model.getPartial("spyglass"), light);

                ms.popPose();
                ms.pushPose();
            }

            if (ToolUtils.toolContainsComponent(stack, CTComponents.ELECTRON_TUBE)) {
                ms.translate(0.5, 0.5, 0.5);

                renderer.renderSolid(model.getPartial("electron_tube"), light);

                ms.popPose();
                ms.pushPose();
            }
        }

        CTGeneratorTypes generatorType = ToolUtils.getGeneratorType(stack);
        switch (generatorType.getId()) {
            case 0 -> {}

            case 1 -> {}

            case 2 -> {
                ms.translate(0.5, 0.5, 0.5);
                renderer.renderSolid(model.getPartial("steam_engine"), light);
            }

            case 3 -> {
                ms.translate(0.5, 0.5, 0.5);
                ms.translate(-xOffset, 0, 0);

                if (nbt.hasUUID("toolid") && !Minecraft.getInstance().isPaused()) {
                    WindmillRotationAngleHandler.update(nbt.getUUID("toolid"));
                    ms.mulPose(Vector3f.XN.rotationDegrees(WindmillRotationAngleHandler.getRotationOfUUID(nbt.getUUID("toolid"))));
                }
                ms.translate(xOffset, 0, 0);

                renderer.renderSolid(model.getPartial("windmill"), light);
            }
        }

        ms.popPose();
        ms.pushPose();
    }

    @Override
    public DrillToolModel createModel(BakedModel originalModel) {
        return new DrillToolModel(originalModel);
    }
}
