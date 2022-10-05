package com.rabbitminers.createtools.toolsbase.generators;

import com.simibubi.create.foundation.utility.Components;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WindmillTool extends DiggerItem {

    enum DisplayColours {
        NONE(ChatFormatting.DARK_RED, 0),
        LOW(ChatFormatting.RED, 1),
        MEDIUM(ChatFormatting.YELLOW, 500),
        FAST(ChatFormatting.GREEN, 700);

        private final ChatFormatting textColor;
        private final int minOutput;

        DisplayColours(ChatFormatting textColor, int minOutput) {
            this.textColor = textColor;
            this.minOutput = minOutput;
        }

        public ChatFormatting getTextColor() { return textColor;}
        public int minOutput() { return minOutput; }

        public static DisplayColours of(int su) {
            if (su >= FAST.minOutput())
                return FAST;
            if (su >= MEDIUM.minOutput())
                return MEDIUM;
            if (su >= LOW.minOutput())
                return LOW;
            return NONE;
        }

    }

    public int SUout = 0;
    public int maxSU = 1024;
    public WindmillTool(float p_204108_, float p_204109_, Tier p_204110_, TagKey<Block> p_204111_, Properties p_204112_) {
        super(p_204108_, p_204109_, p_204110_, p_204111_, p_204112_);
    }

    static double logCal(double n, double b) {
        double c = 0;
        while(n > 1) {
            n = n / b;
            c++;
        }
        c = c + n;
        return c;
    }


    // Fuel over time
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        // Make a value between 1 and 377, so it can be multiplied by
        double yHeight = entity.getY() + 65.0f;
        int y = (yHeight > 377.0f) ? 377 : (int) Math.floor(yHeight);

        // Generate a value dependent on the yHeight between 0 and the MaxSU
        // Python: (math.floor(math.log((yHeight*(maxSU//377)) + minSU, 1.1))) * 10
        // SUout = (int) Math.floor(logCal(y*(maxSU/377.0f), 1.1)) * 10;

        SUout = (int) Math.floor((yHeight/377)*maxSU);

        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level p_41422_,
            List<Component> components,
            TooltipFlag tooltipFlag
    ) {
        CompoundTag nbt;

        if (stack.hasTag()) {
            nbt = stack.getTag();
        } else {
            nbt = new CompoundTag();
        }

        components.add(new TextComponent(String.valueOf("Stress Output: "))
                .withStyle(ChatFormatting.DARK_GRAY)
                .append(new TextComponent(String.valueOf("[" + SUout + "]"))
                .withStyle(DisplayColours.of(SUout).getTextColor())));

        if (Screen.hasShiftDown()) {
            components.add(new TextComponent(String.valueOf(
            "Type: Drill\n Casing: Andesite\nHandle: Andesite\nGenerator: Windmill\nAttachments: None"
            )).withStyle(ChatFormatting.DARK_GRAY));

        } else {
            components.add(new TextComponent(String.valueOf(
            "Type: Drill\n Generator: Windmill\n Attachments: None "
            )).withStyle(ChatFormatting.DARK_GRAY));

            components.add(new TextComponent(String.valueOf("Hold [SHIFT] for more information"))
                    .withStyle(ChatFormatting.GRAY));
        }


        super.appendHoverText(stack, p_41422_, components, tooltipFlag);
    }
}