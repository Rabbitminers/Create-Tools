package com.rabbitminers.createtools.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

import static com.rabbitminers.createtools.tools.tooltypes.base.DeployerTool.TILE_DATA_KEY;

public class NBTHelper {
    public static boolean storeTileData(@Nullable BlockEntity tile, Level level, BlockPos pos, BlockState state, ItemStack stack)
    {
        if (stack.isEmpty())
            return false;

        CompoundTag tileTag = new CompoundTag();
        if (tile != null)
            tileTag = tile.saveWithId();

        CompoundTag tag = stack.hasTag() ? stack.getTag() : new CompoundTag();
        if (tag != null && tag.contains(TILE_DATA_KEY))
            return false;

        tag.put(TILE_DATA_KEY, tileTag);

        tag.putString("block", state.getBlock().getRegistryName().toString());

        tag.putInt("stateid", Block.getId(state));
        stack.setTag(tag);
        return true;
    }

    public static boolean hasTileData(ItemStack stack) {
        if (stack.hasTag())
        {
            CompoundTag tag = stack.getTag();
            return tag.contains(TILE_DATA_KEY) && tag.contains("block") && tag.contains("stateid");
        }
        return false;
    }

    public static void updateTileLocation(CompoundTag tag, BlockPos pos) {
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
    }
    public static void clearTileData(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            tag.remove(TILE_DATA_KEY);
            tag.remove("block");
            tag.remove("stateid");
        }
    }

    public static CompoundTag getTileData(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            return tag.getCompound(TILE_DATA_KEY);
        }
        return null;
    }

    public static Block getBlock(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            int id = tag.getInt("stateid");
            return Block.stateById(id).getBlock();
        }
        return Blocks.AIR;
    }

    public static BlockState getBlockState(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            int id = tag.getInt("stateid");
            return Block.stateById(id);
        }
        return Blocks.AIR.defaultBlockState();
    }

}
