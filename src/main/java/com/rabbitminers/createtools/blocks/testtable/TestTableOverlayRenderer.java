package com.rabbitminers.createtools.blocks.testtable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.components.structureMovement.IDisplayAssemblyExceptions;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.PistonExtensionPoleBlock;
import com.simibubi.create.content.contraptions.goggles.GoggleOverlayRenderer;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.logistics.trains.entity.TrainRelocator;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.config.CClient;
import com.simibubi.create.foundation.gui.RemovedGuiUtils;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.tileEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.outliner.Outline;
import com.simibubi.create.foundation.utility.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TestTableOverlayRenderer {
    public static final IIngameOverlay OVERLAY = TestTableOverlayRenderer::renderOverlay;

    private static final Map<Object, Outliner.OutlineEntry> outlines = CreateClient.OUTLINER.getOutlines();

    public static int hoverTicks = 0;
    public static BlockPos lastHovered = null;

    public static void renderOverlay(ForgeIngameGui gui, PoseStack poseStack, float partialTicks, int width,
                                     int height) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        HitResult objectMouseOver = mc.hitResult;
        if (!(objectMouseOver instanceof BlockHitResult)) {
            lastHovered = null;
            hoverTicks = 0;
            return;
        }

        for (Outliner.OutlineEntry entry : outlines.values()) {
            if (!entry.isAlive())
                continue;
            Outline outline = entry.getOutline();
            if (outline instanceof ValueBox && !((ValueBox) outline).isPassive)
                return;
        }

        BlockHitResult result = (BlockHitResult) objectMouseOver;
        ClientLevel world = mc.level;
        BlockPos pos = result.getBlockPos();
        BlockEntity te = world.getBlockEntity(pos);

        if (!(te instanceof TestTableBlockEntity))
            return;

        int prevHoverTicks = hoverTicks;
        if (lastHovered == null || lastHovered.equals(pos))
            hoverTicks++;
        else
            hoverTicks = 0;
        lastHovered = pos;

        List<Component> tooltip = new ArrayList<>();

        boolean goggleAddedInformation = false;

        IHaveDraftingTableInformation gte = (IHaveDraftingTableInformation) te;
        goggleAddedInformation = gte.addToDraftingTableTooltip(tooltip, mc.player.isShiftKeyDown());

        poseStack.pushPose();

        int tooltipTextWidth = 0;
        for (FormattedText textLine : tooltip) {
            int textLineWidth = mc.font.width(textLine);
            if (textLineWidth > tooltipTextWidth)
                tooltipTextWidth = textLineWidth;
        }

        int tooltipHeight = 8;
        if (tooltip.size() > 1) {
            tooltipHeight += 2; // gap between title lines and next lines
            tooltipHeight += (tooltip.size() - 1) * 10;
        }

        CClient cfg = AllConfigs.CLIENT;
        int posX = width / 2 + cfg.overlayOffsetX.get();
        int posY = height / 2 + cfg.overlayOffsetY.get();

        posX = Math.min(posX, width - tooltipTextWidth - 20);
        posY = Math.min(posY, height - tooltipHeight - 20);

        float fade = Mth.clamp((hoverTicks + partialTicks) / 12f, 0, 1);
        Boolean useCustom = cfg.overlayCustomColor.get();
        Color colorBackground = useCustom ? new Color(cfg.overlayBackgroundColor.get())
                : Theme.c(Theme.Key.VANILLA_TOOLTIP_BACKGROUND)
                .scaleAlpha(.75f);
        Color colorBorderTop = useCustom ? new Color(cfg.overlayBorderColorTop.get())
                : Theme.c(Theme.Key.VANILLA_TOOLTIP_BORDER, true)
                .copy();
        Color colorBorderBot = useCustom ? new Color(cfg.overlayBorderColorBot.get())
                : Theme.c(Theme.Key.VANILLA_TOOLTIP_BORDER, false)
                .copy();

        if (fade < 1) {
            poseStack.translate((1 - fade) * Math.signum(cfg.overlayOffsetX.get() + .5f) * 4, 0, 0);
            colorBackground.scaleAlpha(fade);
            colorBorderTop.scaleAlpha(fade);
            colorBorderBot.scaleAlpha(fade);
        }

        RemovedGuiUtils.drawHoveringText(poseStack, tooltip, posX, posY, width, height, -1, colorBackground.getRGB(),
                colorBorderTop.getRGB(), colorBorderBot.getRGB(), mc.font);

        Block[] casings = {
                AllBlocks.ANDESITE_CASING.get(),
                AllBlocks.BRASS_CASING.get(),
                AllBlocks.COPPER_CASING.get(),
                AllBlocks.RAILWAY_CASING.get(),
                AllBlocks.REFINED_RADIANCE_CASING.get(),
                AllBlocks.SHADOW_STEEL_CASING.get()
        };

        for (int i = 0; i < casings.length; i++) {
            GuiGameElement.of(casings[i].asItem())
                    .at(posX + (18*i) + 12, posY + tooltipHeight - 28, 450)
                    .render(poseStack);
        }

        poseStack.popPose();

        if (Screen.hasAltDown()) {
        }

        /*
        GuiGameElement.of(item)
                .at(posX + 10, posY - 16, 450)
                .render(poseStack);
        poseStack.popPose();
         */
    }
}
