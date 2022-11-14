package com.rabbitminers.createtools.tools.data.display;

import com.rabbitminers.createtools.toolsbase.generators.FurnaceEngineTool;
import net.minecraft.ChatFormatting;

public enum FurnaceEngineFuelData {
    NONE(ChatFormatting.DARK_RED, 0),
    LOW(ChatFormatting.RED, 1),
    MEDIUM(ChatFormatting.YELLOW, 300),
    FAST(ChatFormatting.GREEN, 600);

    private final ChatFormatting textColor;
    private final int minOutput;

    FurnaceEngineFuelData(ChatFormatting textColor, int minOutput) {
        this.textColor = textColor;
        this.minOutput = minOutput;
    }

    public ChatFormatting getTextColor() { return textColor;}
    public int minOutput() { return minOutput; }

    public static FurnaceEngineFuelData of(int su) {
        if (su >= FAST.minOutput())
            return FAST;
        if (su >= MEDIUM.minOutput())
            return MEDIUM;
        if (su >= LOW.minOutput())
            return LOW;
        return NONE;
    }
}
