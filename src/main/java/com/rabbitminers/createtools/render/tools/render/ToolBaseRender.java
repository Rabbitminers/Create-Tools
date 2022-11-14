package com.rabbitminers.createtools.render.tools.render;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.rabbitminers.createtools.render.tools.model.DrillToolModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueHandler;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ToolBaseRender extends CustomRenderedItemModelRenderer<DrillToolModel> {
    @Override
    protected void render(ItemStack stack, DrillToolModel model, PartialItemModelRenderer renderer, ItemTransforms.TransformType transformType,
                          PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        renderer.render(model.getOriginalModel(), light);
        TransformStack stacker = TransformStack.cast(ms);
        float animation = 0.25f;

        float xOffset = -1/16f;
        ms.translate(-xOffset, 0, 0);
        ms.mulPose(Vector3f.YP.rotationDegrees(ScrollValueHandler.getScroll(AnimationTickHolder.getPartialTicks())));
        ms.translate(xOffset, 0, 0);

        CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();

        if (nbt != null && nbt.contains("generator") && nbt.getInt("generator") == 3)
            ms.scale(0, 0 , 0);
        else
            ms.scale(1, 1, 1);

        renderer.render(model.getPartial("gear"), light);
    }

    @Override
    public DrillToolModel createModel(BakedModel originalModel) {
        return new DrillToolModel(originalModel);
    }
}
