package com.rabbitminers.createtools.events;

import com.rabbitminers.createtools.tooldata.CTComponents;
import com.rabbitminers.createtools.tools.ToolBase;
import net.minecraft.client.gui.Gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CompassOverlayEvent extends Gui {
    private static Minecraft mc;

    public CompassOverlayEvent(Minecraft mc){
        super(mc);
        CompassOverlayEvent.mc = mc;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void renderOverlay(RenderGameOverlayEvent.Pre e) {
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof ToolBase tool))
            return;

        if (!tool.toolContainsComponent(stack, CTComponents.COMPASS))
            return;

        ElementType type = e.getType();
        if (type != ElementType.TEXT) {
            return;
        }

        String coordinates = getCoordinates();

        Font fontRender = mc.font;
        Window scaled = mc.getWindow();
        int width = scaled.getGuiScaledWidth();

        int stringWidth = fontRender.width(coordinates);

        Color colour = new Color(255, 0, 0, 255);

        PoseStack posestack = e.getMatrixStack();
        posestack.pushPose();

        int xcoord = 5;

        fontRender.draw(posestack, coordinates, xcoord, 5, colour.getRGB());

        posestack.popPose();
    }

    private static List<String> direction = new ArrayList<String>(Arrays.asList("S", "SW", "W", "NW", "N", "NE", "E", "SE"));
    private static String getCoordinates() {
        LocalPlayer player = mc.player;
        BlockPos ppos = player.blockPosition();

        int yaw = (int)player.getYHeadRot();
        if (yaw < 0) {
            yaw += 360;
        }
        yaw+=22;
        yaw%=360;

        int facing = yaw/45;
        if (facing < 0) {
            facing = facing * -1;
        }

        String toshow = "";

        toshow += direction.get(facing) + ": ";
        toshow += ppos.getX() + ", ";
        toshow += ppos.getY() + ", ";
        toshow += ppos.getZ() + ", ";

        return toshow.substring(0, toshow.length() - 2);
    }
}
