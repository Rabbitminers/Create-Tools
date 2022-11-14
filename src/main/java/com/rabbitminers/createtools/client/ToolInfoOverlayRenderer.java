package com.rabbitminers.createtools.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.createtools.blocks.testtable.TestTableOverlayRenderer;
import com.rabbitminers.createtools.tools.ToolBase;
import com.rabbitminers.createtools.toolsbase.BaseTools;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.config.CClient;
import com.simibubi.create.foundation.gui.RemovedGuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class ToolInfoOverlayRenderer {
    public static final IIngameOverlay OVERLAY = ToolInfoOverlayRenderer::renderOverlay;

    public static void renderOverlay(
        ForgeIngameGui gui,
        PoseStack poseStack,
        float partialTicks,
        int width,
        int height
    ) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        ClientLevel world = mc.level;
        Player player = mc.player;
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() instanceof ToolBase toolBase) {
            CClient cfg = AllConfigs.CLIENT;
            int posX = width / 2 + cfg.overlayOffsetX.get();
            int posY = height / 2 + cfg.overlayOffsetY.get();
        }
    }
}
