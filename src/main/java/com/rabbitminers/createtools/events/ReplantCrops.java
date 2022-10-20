package com.rabbitminers.createtools.events;

import com.rabbitminers.createtools.CreateTools;
import com.rabbitminers.createtools.toolsbase.BaseTools;
import com.rabbitminers.createtools.toolsbase.ToolTypes.HarvesterTool;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;


@EventBusSubscriber
public class ReplantCrops {
    public static Level getWorldIfInstanceOfAndNotRemote(LevelAccessor iworld) {
        if (iworld.isClientSide()) {
            return null;
        }
        if (iworld instanceof Level) {
            return ((Level)iworld);
        }
        return null;
    }

    private static HashMap<BlockPos, Block> checkreplant = new HashMap<BlockPos, Block>();
    private static HashMap<BlockPos, BlockState> cocoaStates = new HashMap<BlockPos, BlockState>();

    @SubscribeEvent
    public void onHarvest(BlockEvent.BreakEvent e) {
        Level world = getWorldIfInstanceOfAndNotRemote(e.getWorld());
        if (world == null) {
            return;
        }

        Player player = e.getPlayer();
        if (player == null) {
            return;
        }


        if (!(player.getMainHandItem().getItem() instanceof HarvesterTool))
            return;

        if (player.isShiftKeyDown()) {
            return;
        }

        BlockPos hpos = e.getPos().immutable();
        BlockState state = world.getBlockState(hpos);
        Block block = state.getBlock();

        if (block instanceof CropBlock) {
            checkreplant.put(hpos, block);
        }
        else if (block.equals(Blocks.NETHER_WART)) {
            checkreplant.put(hpos, block);
        }
        else if (block.equals(Blocks.COCOA)) {
            cocoaStates.put(hpos, state);
            checkreplant.put(hpos, block);
        }
        else {
            return;
        }
    }

    @SubscribeEvent
    public void onHarvest(EntityJoinWorldEvent e) {
        Level world = e.getWorld();
        if (world.isClientSide) {
            return;
        }

        Entity entity = e.getEntity();
        if (!(entity instanceof ItemEntity)) {
            return;
        }

        BlockPos ipos = entity.blockPosition();
        if (!checkreplant.containsKey(ipos)) {
            return;
        }

        Block preblock = checkreplant.get(ipos);

        Item compareitem = null;
        if (preblock instanceof CropBlock) {
            compareitem = ((CropBlock)preblock).getCloneItemStack(world, ipos, null).getItem();
        }

        ItemEntity itementity = (ItemEntity)entity;
        ItemStack itemstack = itementity.getItem();
        Item item = itemstack.getItem();

        if (item.equals(compareitem)) {
            world.setBlockAndUpdate(ipos, preblock.defaultBlockState());
        }
        else if (item.equals(Items.NETHER_WART)) {
            world.setBlockAndUpdate(ipos, Blocks.NETHER_WART.defaultBlockState());
        }
        else if (item.equals(Items.COCOA_BEANS)) {
            if (!cocoaStates.containsKey(ipos)) {
                checkreplant.remove(ipos);
                return;
            }
            world.setBlockAndUpdate(ipos, cocoaStates.get(ipos).setValue(CocoaBlock.AGE, 0));
            cocoaStates.remove(ipos);
        }
        else {
            return;
        }

        checkreplant.remove(ipos);

        if (itemstack.getCount() > 1) {
            itemstack.shrink(1);
        }
        else {
            e.setCanceled(true);
        }
    }
}