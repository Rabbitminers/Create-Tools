package com.rabbitminers.createtools.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.stream.IntStream;

public abstract class ItemDisplayBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer, IOwnerProtected {
    @Nullable
    private UUID owner = null;
    private NonNullList<ItemStack> stacks;

    public ItemDisplayBlockEntity(BlockEntityType type, BlockPos pos, BlockState state) {
        this(type, pos, state, 1);
    }

    public ItemDisplayBlockEntity(BlockEntityType type, BlockPos pos, BlockState state, int slots) {
        super(type, pos, state);
        this.stacks = NonNullList.withSize(slots, ItemStack.EMPTY);
    }

    @Override
    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }

    @Override
    @Nullable
    public UUID getOwner() {
        return owner;
    }
    @Override
    public void setChanged() {
        if (this.level == null) return;
        this.updateTileOnInventoryChanged();
        if (this.needsToUpdateClientWhenChanged()) {
            //this saves and sends a packet to update the client tile
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
        super.setChanged();
    }

    public void updateTileOnInventoryChanged() {
    }

    public boolean needsToUpdateClientWhenChanged() {
        return true;
    }
    public void updateClientVisualsOnLoad() {
    }

    public ItemStack getDisplayedItem() {
        return this.getItem(0);
    }

    public void setDisplayedItem(ItemStack stack) {
        this.setItem(0, stack);
    }

    public InteractionResult interact(Player player, InteractionHand handIn) {
        return this.interact(player, handIn, 0);
    }

    public InteractionResult interact(Player player, InteractionHand handIn, int slot) {
        if (!this.isAccessibleBy(player)) {
            System.out.println("NOT ACCESSIBLE"); //TODO: ADD AN ACTUAL MESSAGE
        } else if (handIn == InteractionHand.MAIN_HAND) {
            ItemStack handItem = player.getItemInHand(handIn);
            //remove
            if (!this.isEmpty() && handItem.isEmpty()) {
                ItemStack it = this.removeItemNoUpdate(slot);
                if (!this.level.isClientSide()) {
                    player.setItemInHand(handIn, it);
                    this.setChanged();
                } else {
                    //also update visuals on client. will get overwritten by packet tho
                    this.updateClientVisualsOnLoad();
                }
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
            //place
            else if (!handItem.isEmpty() && this.canPlaceItem(slot, handItem)) {
                ItemStack it = handItem.copy();
                it.setCount(1);
                this.setItem(slot, it);

                if (!player.isCreative()) {
                    handItem.shrink(1);
                }
                if (!this.level.isClientSide()) {
                    this.level.playSound(null, this.worldPosition, this.getAddItemSound(), SoundSource.BLOCKS, 1.0F, this.level.random.nextFloat() * 0.10F + 0.95F);
                    //this.setChanged();
                } else {
                    //also update visuals on client. will get overwritten by packet tho
                    this.updateClientVisualsOnLoad();
                }
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    public SoundEvent getAddItemSound() {
        return SoundEvents.ITEM_FRAME_ADD_ITEM;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (!this.tryLoadLootTable(compound)) {
            this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        }
        ContainerHelper.loadAllItems(compound, this.stacks);
        if (this.level != null) {
            if (this.level.isClientSide) this.updateClientVisualsOnLoad();
                //this doesn't work on first load cause world is null on server. You need to save stuff on nbt
            else this.updateTileOnInventoryChanged();
        }
        this.loadOwner(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.stacks);
        }
        this.saveOwner(compound);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public int getContainerSize() {
        return stacks.size();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return ChestMenu.threeRows(id, player, this);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    public void setItems(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return this.isEmpty();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return IntStream.range(0, this.getContainerSize()).toArray();
    }




}