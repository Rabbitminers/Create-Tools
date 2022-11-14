package com.rabbitminers.createtools.events;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.rabbitminers.createtools.config.CTConfig;
import com.rabbitminers.createtools.toolsbase.ToolTypes.SawTool;
import com.rabbitminers.createtools.util.TagKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber
public class TreeChop {

    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.START) return;
        if (event.side != LogicalSide.SERVER) return;

        final UUID uuid = event.player.getUUID();
        if (!nextMap.containsKey(uuid) || nextMap.get(uuid).isEmpty()) return;

        int i = 0;
        for (BlockPos point : ImmutableSet.copyOf(nextMap.get(uuid)))
        {
            ((ServerPlayer) event.player).gameMode.destroyBlock(point);
            nextMap.remove(uuid, point);
            if (i++ > CTConfig.GENERAL.tickLimit.get()) break;
        }
        if (pointMap.get(uuid).size() > CTConfig.GENERAL.totalLimit.get()) nextMap.removeAll(uuid);
        if (!nextMap.containsKey(uuid) || !nextMap.get(uuid).isEmpty()) pointMap.removeAll(uuid);
    }
    private final HashMultimap<UUID, BlockPos> pointMap = HashMultimap.create();
    private final HashMultimap<UUID, BlockPos> nextMap = HashMultimap.create();

    @SubscribeEvent
    public void breakEvent(BlockEvent.BreakEvent event) {
        final Player player = event.getPlayer();
        if (player == null)
            return;

        ItemStack itemStack = player.getMainHandItem();
        if (itemStack == ItemStack.EMPTY || !(itemStack.getItem() instanceof SawTool)) return;

        TagKey<Block> destroyConnectedTag = TagKeys.DESTROY_CONNECTED;
        TagKey<Block> ignoreConnectedTag = TagKeys.IGNORE_CONNECTED;

        // Only interact if wood or leaves
        final UUID uuid = player.getUUID();
        final BlockState state = event.getState();

        if (!shalCut(state, destroyConnectedTag, ignoreConnectedTag))
            return;

        pointMap.put(uuid, event.getPos());
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
                for (int offsetY = -1; offsetY <= 1; offsetY++) {
                    BlockPos newPoint = event.getPos().offset(offsetX, offsetY, offsetZ);

                    if (nextMap.containsEntry(uuid, newPoint) || pointMap.containsEntry(uuid, newPoint)) continue;

                    BlockState newBlockState = event.getWorld().getBlockState(newPoint);
                    boolean isLeaves = CTConfig.GENERAL.leaves.get() && newBlockState.getMaterial() == Material.LEAVES;

                    if (!newBlockState.is(ignoreConnectedTag) && CTConfig.GENERAL.mode.get() == 0 && (isLeaves || newBlockState.getBlock() == state.getBlock())
                            || CTConfig.GENERAL.mode.get() == 1 && (isLeaves || shalCut(newBlockState, destroyConnectedTag, ignoreConnectedTag)))
                        nextMap.put(uuid, newPoint); // Add the block for next tick
                }
            }
        }
    }

    private boolean shalCut(BlockState state, TagKey<Block> destroyTag, TagKey<Block> ignoreTag)
    {
        if (state.is(ignoreTag))
            return false;

        Material material = state.getMaterial();

        if (CTConfig.GENERAL.useMaterials.get() && (material == Material.WOOD || material == Material.NETHER_WOOD))
            return true;

        if (CTConfig.GENERAL.leaves.get() && (material == Material.LEAVES))
            return true;

        return state.is(destroyTag);
    }
}
