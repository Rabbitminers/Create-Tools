package com.rabbitminers.createtools.toolsbase.ToolTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class SawTool extends DiggerItem {

    public SawTool(Tier p_40521_, float p_40522_, float p_40523_, Item.Properties p_40524_) {
        super(p_40522_, p_40523_, p_40521_, BlockTags.MINEABLE_WITH_AXE, p_40524_);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        return stack != ItemStack.EMPTY && (Material.LEAVES.equals(state.getMaterial()) || super.mineBlock(stack, level, state, pos, entityLiving));
    }
}
