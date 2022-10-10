package com.rabbitminers.createtools.toolsbase;

import com.rabbitminers.createtools.index.CPTileEntities;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.SerializationUtils;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Objects;

public class DeployerTool extends Item {

    public BlockState heldBlock;
    public DeployerTool(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if (stack.hasTag())
            components.add(new TextComponent(String.valueOf(stack.getTag())));

        super.appendHoverText(stack, level, components, tooltipFlag);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        BlockState data = useOnContext.getLevel().getBlockState(useOnContext.getClickedPos());
        ItemStack stack = useOnContext.getItemInHand();
        CompoundTag nbt;

        nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();
        if (nbt != null && !nbt.getString("Name").equals("None")) {
            placeBlock(useOnContext);
        } else {
            nbt = NbtUtils.writeBlockState(data);
            stack.setTag(nbt);
            useOnContext.getLevel().removeBlock(useOnContext.getClickedPos(), true);
        }

        return super.useOn(useOnContext);
    }
    
    public void placeBlock(UseOnContext useOnContext) {
        ItemStack stack = useOnContext.getItemInHand();
        BlockState state;

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

        BlockPos blockPlacePosition = new BlockPos(x, y, z);

        System.out.println(blockPos);
        System.out.println(blockPlacePosition);

        CompoundTag nbt = stack.getTag();
        if (nbt != null) {
            state = NbtUtils.readBlockState(nbt);
            useOnContext.getLevel().setBlock(blockPlacePosition, state, 512);

            System.out.println("Set block!");
            clearHeldItem(stack, nbt);
        }
    }

    public void clearHeldItem(ItemStack stack, CompoundTag nbt) {
        nbt.putString("Name", "None");
        nbt.putString("Properties", "None");

        stack.setTag(nbt);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack p_41398_, Player p_41399_, LivingEntity p_41400_, InteractionHand p_41401_) {
        return super.interactLivingEntity(p_41398_, p_41399_, p_41400_, p_41401_);
    }
}
