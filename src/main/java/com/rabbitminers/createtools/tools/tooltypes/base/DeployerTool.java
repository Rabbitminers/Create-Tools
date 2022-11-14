package com.rabbitminers.createtools.tools.tooltypes.base;

import com.rabbitminers.createtools.handler.InputHandler;
import com.rabbitminers.createtools.tools.ToolBase;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.utility.outliner.Outliner;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeployerTool extends ToolBase {

    private Object outlineSlot = new Object();
    private final int maxVolume = 25;
    private Direction selectedFace;

    BlockPos pos1 = null;
    BlockPos pos2 = null;

    enum DeployerMode {
        NONE(0, "none", ChatFormatting.GRAY),
        ATTACK(2, "attack", ChatFormatting.RED),
        PLACE(1, "place", ChatFormatting.GREEN),
        DOLLY(3, "dolly", ChatFormatting.BLUE);

        private int id;
        private String name;
        private ChatFormatting colour;

        DeployerMode(int id, String name, ChatFormatting colour) {
            this.id = id;
            this.name = name;
        }

        public static DeployerMode of(ItemStack stack) {
            CompoundTag nbt = stack.hasTag()
                ? stack.getTag()
                : new CompoundTag();

            if (nbt != null && nbt.contains("mode")) {
                for (DeployerMode deployerMode : DeployerMode.values()) {
                    if (nbt.getString("mode").equals(deployerMode.name()))
                        return deployerMode;
                }
            }
            return NONE;
        }
    }

    public DeployerTool(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            Item offhandItem = player.getOffhandItem().getItem();

            if (InputHandler.isToolUsed(player) && offhandItem instanceof BlockItem) {
                BlockItem blockItem = (BlockItem) offhandItem;
                BlockState state = blockItem.getBlock().defaultBlockState();

                if (blockItem != Items.AIR)
                    fillArea(level, state, player);
            }

            if (pos1 == null || pos2 == null)
                return;

            AABB currentSelectionBox = new AABB(pos1, pos2).expandTowards(1, 1, 1);
            if (player.getMainHandItem() == stack)
                outliner().chaseAABB(outlineSlot, currentSelectionBox)
                        .colored(0x6886c5) // 0x6886c5
                        .withFaceTextures(AllSpecialTextures.CHECKERED, AllSpecialTextures.HIGHLIGHT_CHECKERED)
                        .lineWidth(1 / 16f)
                        .highlightFace(selectedFace);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if (DeployerMode.of(useOnContext.getItemInHand()) == DeployerMode.PLACE) {
            Player player = useOnContext.getPlayer();

            if (player.isCrouching()) {
                pos2 = getBlockPlacePos(useOnContext);
                player.displayClientMessage(new TextComponent("Set position 2"), true);
            } else {
                pos1 = getBlockPlacePos(useOnContext);
                player.displayClientMessage(new TextComponent("Set position 1"), true);
            }

            return super.useOn(useOnContext);
        }

        if (DeployerMode.of(useOnContext.getItemInHand()) == DeployerMode.DOLLY) {

        }

        return InteractionResult.CONSUME;
    }

    public BlockPos getBlockPlacePos(UseOnContext useOnContext) {
        BlockPos blockPos = useOnContext.getClickedPos();
        Direction direction = useOnContext.getClickedFace();

        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        switch (direction) {
            case UP -> y += 1; case DOWN -> y -= 1;
            case SOUTH -> z += 1; case NORTH -> z -= 1;
            case EAST -> x += 1; case WEST -> x -= 1;
        }

        return new BlockPos(x, y, z);
    }

    public void fillArea(Level level, BlockState state, Player player) {
        List<BlockPos> blocksToPlace = new ArrayList<BlockPos>();
        ItemStack offHandItem = player.getOffhandItem();

        if (pos1 == null || pos2 == null)
            return;

        int lX = Math.min(pos1.getX(), pos2.getX());
        int lY = Math.min(pos1.getY(), pos2.getY());
        int lZ = Math.min(pos1.getZ(), pos2.getZ());

        int uX = Math.max(pos1.getX(), pos2.getX());
        int uY = Math.max(pos1.getY(), pos2.getY());
        int uZ = Math.max(pos1.getZ(), pos2.getZ());

        int volume = (Math.abs(uX - lX) + 1) * (Math.abs(uY - lY) + 1) * (Math.abs(uZ - lZ) + 1);

        if (volume > maxVolume) {
            player.displayClientMessage(new TextComponent("Area too large, max volume is " + maxVolume), true);
            return;
        }

        if (volume > offHandItem.getCount() && !player.isCreative()) {
            player.displayClientMessage(new TextComponent("Not enough blocks to fill the area"), true);
            return;
        }

        if (!player.isCreative())
            player.getOffhandItem().shrink(volume);

        for (int x = lX; x <= uX; x++) {
            for (int y = lY; y <= uY; y++) {
                for (int z = lZ; z <= uZ; z++) {
                    blocksToPlace.add(new BlockPos(x, y, z));
                }
            }
        }

        for (BlockPos pos : blocksToPlace) {
            level.setBlock(pos, state, 512);
        }

        player.displayClientMessage(new TextComponent("Placed Blocks"), true);
        pos1 = pos2 = null;
    }
    private Outliner outliner() {
        return CreateClient.OUTLINER;
    }

}
