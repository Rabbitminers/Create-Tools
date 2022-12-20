package com.rabbitminers.createtools.tools.tooltypes.base;

import com.rabbitminers.createtools.tools.ToolBase;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class HarvesterTool extends ToolBase {
    private final TagKey<Block> blocks;

    public HarvesterTool(Properties p_41383_, TagKey<Block> blocks, float speed) {
        super(p_41383_);
        this.blocks = blocks;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 1;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return 0;
    }
}
