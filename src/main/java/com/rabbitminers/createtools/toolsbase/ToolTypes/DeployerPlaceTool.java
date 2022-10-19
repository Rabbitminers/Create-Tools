package com.rabbitminers.createtools.toolsbase.ToolTypes;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Vector3f;
import com.rabbitminers.createtools.handler.InputHandler;
import com.rabbitminers.createtools.handler.KeybindHandler;
import com.rabbitminers.createtools.toolsbase.BaseTools;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllKeys;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.schematics.client.SchematicEditScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.RaycastHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.commands.FillCommand;
import net.minecraft.server.commands.SetBlockCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class DeployerPlaceTool extends Item {

    private Object outlineSlot = new Object();
    // TODO: Add a config value for this
    private final int maxVolume = 25;
    BlockPos pos1 = null;
    BlockPos pos2 = null;
    private Direction selectedFace;

    public DeployerPlaceTool(Properties properties) {
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
                    placeBlock(level, state, player);
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


    public void placeBlock(Level level, BlockState state, Player player) {
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

        if (volume > offHandItem.getCount()) {
            player.displayClientMessage(new TextComponent("Not enough blocks to fill the area"), true);
            return;
        }

        player.getOffhandItem().shrink(volume);

        for (int x = lX; x<=uX; x++) {
            for (int y = lY; y<=uY; y++) {
                for (int z = lZ; z<=uZ; z++) {
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


    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
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

    private Outliner outliner() {
        return CreateClient.OUTLINER;
    }

}
