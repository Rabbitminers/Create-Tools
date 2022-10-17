package com.rabbitminers.createtools.blocks.testtable;

import com.rabbitminers.createtools.index.CTBlockEntities;
import com.rabbitminers.createtools.util.ItemDisplayBlockEntity;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class TestTableBlockEntity extends ItemDisplayBlockEntity {
    public TestTableBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(CTBlockEntities.TEST_TABLE.get(), p_155229_, p_155230_);
    }

    public void test() {
        System.out.println("Hello world!");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("Hello world");
    }
}
