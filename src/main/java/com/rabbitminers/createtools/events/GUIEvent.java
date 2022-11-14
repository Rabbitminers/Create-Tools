package com.rabbitminers.createtools.events;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.createtools.handler.GeneratorHandler;
import com.rabbitminers.createtools.tooldata.CTComponents;
import com.rabbitminers.createtools.tools.ToolBase;
import com.rabbitminers.createtools.tools.generators.Generator;
import com.rabbitminers.createtools.tools.generators.HandCrank;
import com.rabbitminers.createtools.util.ToolUtils;
import com.simibubi.create.content.curiosities.TreeFertilizerItem;
import com.simibubi.create.content.curiosities.weapons.PotatoCannonItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;

public class GUIEvent extends Gui {
    private static Minecraft mc;
    private static String daystring = "";

    public GUIEvent(Minecraft mc) {
        super(mc);
        GUIEvent.mc = mc;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void renderOverlay(RenderGameOverlayEvent.Post e) {
        Player player = mc.player;

        RenderGameOverlayEvent.ElementType type = e.getType();
        if (type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        PoseStack poseStack = e.getMatrixStack();
        poseStack.pushPose();

        Font fontRender = mc.font;
        Window scaled = mc.getWindow();
        int width = scaled.getGuiScaledWidth();

        int hOffset = 5;

        if (player != null) {

            ItemStack stack = player.getMainHandItem();
            CompoundTag nbt = stack.hasTag()
                    ? stack.getTag()
                    : new CompoundTag();

            if (stack.getItem() instanceof ToolBase tool && nbt != null && nbt.hasUUID("toolid")) {
                Generator generator =  GeneratorHandler.getGeneratorOfUUID(nbt.getUUID("toolid"));

                if (generator instanceof HandCrank handCrank)
                    fontRender.draw(poseStack, Math.floor(handCrank.getRemainingTicks() / 40.0f) + "s", width-(width/2)-10, hOffset, getColour(handCrank).getRGB());

                if (ToolUtils.toolContainsComponent(stack, CTComponents.CLOCK)) {
                    Color colour = new Color(255,223,0);
                    fontRender.draw(poseStack, getGameTime(), width - 60, hOffset, colour.getRGB());
                }
            }
        }

        poseStack.popPose();
    }

    private static Color getColour(HandCrank handCrank) {
        if (handCrank.getRemainingTicks() > handCrank.getMaxTicks()*0.75)
            return new Color(85, 255, 85);
        if (handCrank.getRemainingTicks() > handCrank.getMaxTicks()*0.5)
            return new Color(255, 255, 85);
        if (handCrank.getRemainingTicks() > handCrank.getMaxTicks()*0.25)
            return new Color(255, 85, 85);
        return new Color(170, 0, 0);
    }

    private static String getGameTime() {
        int time = 0;
        int gametime = (int)mc.level.getDayTime();
        int daysplayed = 0;

        while (gametime >= 24000) {
            gametime-=24000;
            daysplayed += 1;
        }

        if (gametime >= 18000) {
            time = gametime-18000;
        }
        else {
            time = 6000+gametime;
        }

        String suffix;
        if (time >= 13000) {
            time = time - 12000;
            suffix = " PM";
        }
        else {
            if (time >= 12000) {
                suffix = " PM";
            }
            else {
                suffix = " AM";
                if (time <= 999) {
                    time += 12000;
                }
            }
        }

        String stringtime = time/10 + "";
        for (int n = stringtime.length(); n < 4; n++) {
            stringtime = "0" + stringtime;
        }


        String[] strsplit = stringtime.split("");

        int minutes = (int)Math.floor(Double.parseDouble(strsplit[2] + strsplit[3])/100*60);
        String sm = minutes + "";
        if (minutes < 10) {
            sm = "0" + minutes;
        }

        stringtime = strsplit[1] + ":" + sm.charAt(0) + sm.charAt(1);

        return stringtime + suffix;
    }

}
