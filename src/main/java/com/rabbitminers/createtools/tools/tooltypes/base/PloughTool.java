package com.rabbitminers.createtools.tools.tooltypes.base;

import com.mojang.datafixers.util.Pair;
import com.rabbitminers.createtools.tools.ToolBase;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.minecraft.world.item.HoeItem.changeIntoState;

public class PloughTool extends ToolBase {

    private final TagKey<Block> blocks;
    protected final float speed;


    public PloughTool(Properties properties, TagKey<Block> blocks, float speed) {
        super(properties);
        this.blocks = blocks;
        this.speed = speed;
    }
    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(this.blocks) ? this.speed : 1.0F;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockpos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockpos);

        BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(useOnContext, net.minecraftforge.common.ToolActions.HOE_TILL, false);
        System.out.println(toolModifiedState);


        return InteractionResult.CONSUME;
    }


}
