package com.rabbitminers.createtools.blocks.testtable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Rotation;

public class TestTableBlockRenderer implements BlockEntityRenderer<TestTableBlockEntity> {

    private final Minecraft minecraft = Minecraft.getInstance();
    private final ItemRenderer itemRenderer;
    private final EntityRenderDispatcher entityRenderer;

    public TestTableBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = minecraft.getItemRenderer();
        this.entityRenderer = minecraft.getEntityRenderDispatcher();
    }

    public int remain = 1000;
    public int state = 1;

    @Override
    public void render(
        TestTableBlockEntity tile,
        float partialTicks,
        PoseStack matrixStackIn,
        MultiBufferSource bufferIn,
        int combinedLightIn,
        int combinedOverlayIn
    ) {
        ItemStack stack;
        Player player = Minecraft.getInstance().player;

        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5, 1.15, 0.5);

        matrixStackIn.scale(0.5f, 0.5f, 0.5f);

        ItemTransforms.TransformType transform = ItemTransforms.TransformType.FIXED;
        stack = player!=null
                ? new ItemStack(player.getMainHandItem().getItem())
                : new ItemStack(Items.DIAMOND);

        /*
        matrixStackIn.translate(0, 0, 1); // Horizontal
        matrixStackIn.translate(0, 1, 0); // Height
        matrixStackIn.translate(1, 0, 0); // Horizontal
        */

        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(270));
        this.itemRenderer.renderStatic(
                stack, transform, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, 0
        );
        matrixStackIn.popPose();
    }
}
