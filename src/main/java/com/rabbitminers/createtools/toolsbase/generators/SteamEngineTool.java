package com.rabbitminers.createtools.toolsbase.generators;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SteamEngineTool extends DiggerItem {

    private int remainingFuelTicks;
    private int remainingWaterTicks;
    private int runningTicks;

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

        public static SteamEngineTool.DisplayColours of(int su) {
            if (su >= FAST.minOutput())
                return FAST;
            if (su >= MEDIUM.minOutput())
                return MEDIUM;
            if (su >= LOW.minOutput())
                return LOW;
            return NONE;
        }

    }

    public SteamEngineTool(float p_204108_, float p_204109_, Tier p_204110_, TagKey<Block> p_204111_, Properties p_204112_) {
        super(p_204108_, p_204109_, p_204110_, p_204111_, p_204112_);
        int remainingFuelTicks = 0;
        int remainingWaterTicks = 0;
    }

    public double getRunningTickMultiplier() {
        int maxBonusTicks = (20*600);
        double runningTickMultiplier = this.runningTicks<(maxBonusTicks)
            ? this.runningTicks/(20f*600f)*5
            : maxBonusTicks*5;
        return runningTickMultiplier;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        CompoundTag nbt;
        nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();

        if (this.remainingFuelTicks > 0 && this.remainingWaterTicks > 0) {
            this.remainingFuelTicks--;
            this.remainingWaterTicks--;
            this.runningTicks++;
        }

        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
