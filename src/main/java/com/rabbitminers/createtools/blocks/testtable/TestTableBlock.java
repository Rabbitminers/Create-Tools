package com.rabbitminers.createtools.blocks.testtable;

import com.rabbitminers.createtools.util.CTBlockProperties;
import com.rabbitminers.createtools.util.CTComponents;
import net.minecraft.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;

import java.util.stream.Stream;

public class TestTableBlock extends Block implements EntityBlock, WorldlyContainerHolder {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_ITEM = CTBlockProperties.HAS_ITEM;
    public TestTableBlock(Properties properties) {
        super(properties);
    }
    private static final VoxelShape SHAPE = Stream.of(Block.box(0, 0, 0, 16, 16, 16)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }


    @Override
    public InteractionResult use(
        BlockState state,
        Level worldIn,
        BlockPos pos,
        Player player,
        InteractionHand handIn,
        BlockHitResult hit
    ) {
        InteractionResult resultType = InteractionResult.PASS;
        if (worldIn.getBlockEntity(pos) instanceof TestTableBlockEntity tile && tile.isAccessibleBy(player)) {
            ItemStack stack = player.getItemInHand(handIn);

            this.addModifier(player, stack, tile);
            CTComponents toolType = CTComponents.of(stack.getItem());

            if (toolType != null) {
                player.displayClientMessage(new TextComponent("Creating new tool"), true);
            }

            resultType = tile.interact(player, handIn);
        }
        return resultType;
    }

    public void addModifier(Player player, ItemStack stack, TestTableBlockEntity tile) {
        if (stack.getItem() == Items.AIR)
            return;

        // Assert if the item applied is a valid component
        if (CTComponents.of(stack.getItem()) != null) {
            // Get tool to update nbt data
            ItemStack tool = tile.getDisplayedItem();
            CTComponents component = CTComponents.of(stack.getItem());
            CompoundTag nbt;

            // Get existing nbt data
            nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();

            if (nbt.contains("components")) {
                String data = nbt.getString("components");
                nbt.putString("components", String.valueOf(data + component.getName()));
            } else {
                nbt.putString("components", component.getName());
            }

            player.displayClientMessage(new TextComponent("Added Component"), true);
            tool.setTag(nbt);
            stack.shrink(1);


        } else {
            player.displayClientMessage(new TextComponent("Invalid Component"), true);
        }
    }



    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TestTableBlockEntity(pos, state);
    }

    @Override
    public WorldlyContainer getContainer(BlockState state, LevelAccessor level, BlockPos pos) {
        if (state.getValue(HAS_ITEM))
            return (TestTableBlockEntity) level.getBlockEntity(pos);
        return new TileLessContainer(state, level, pos);
    }

    static class TileLessContainer extends SimpleContainer implements WorldlyContainer {
        private final BlockState state;
        private final LevelAccessor level;
        private final BlockPos pos;
        private TestTableBlockEntity tileReference = null;

        public TileLessContainer(BlockState blockState, LevelAccessor levelAccessor,
                                 BlockPos blockPos) {
            super(1);
            this.state = blockState;
            this.level = levelAccessor;
            this.pos = blockPos;
        }

        @Override
        public boolean stillValid(Player player) {
            return tileReference == null;
        }

        @Override
        public ItemStack getItem(int slot) {
            if (tileReference != null) return tileReference.getItem(slot);
            return super.getItem(slot);
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            if (tileReference != null) return tileReference.removeItem(slot, amount);
            return super.removeItem(slot, amount);
        }

        @Override
        public boolean isEmpty() {
            if (tileReference != null) return tileReference.isEmpty();
            return super.isEmpty();
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            if (tileReference != null) return tileReference.removeItemNoUpdate(slot);
            return super.removeItemNoUpdate(slot);
        }

        @Override
        public void clearContent() {
            if (tileReference != null) tileReference.clearContent();
            else super.clearContent();
        }

        @Override
        public boolean canPlaceItem(int index, ItemStack stack) {
            if (tileReference != null) return tileReference.canPlaceItem(index, stack);
            return super.canPlaceItem(index, stack);
        }

        @Override
        public int getMaxStackSize() {
            if (tileReference != null) return tileReference.getMaxStackSize();
            return 1;
        }

        @Override
        public int[] getSlotsForFace(Direction side) {
            if (tileReference != null) return tileReference.getSlotsForFace(side);
            return new int[]{0};
        }

        @Override
        public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
            if (tileReference != null) return canTakeItemThroughFace(index, itemStack, direction);
            return true;
        }

        @Override
        public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
            if (tileReference != null) return tileReference.canTakeItemThroughFace(index, stack, direction);
            return !this.isEmpty();
        }

        @Override
        public void setChanged() {
            if (!this.isEmpty()) {
                level.setBlock(pos, state.setValue(TestTableBlock.HAS_ITEM, true), 3);
                if (level.getBlockEntity(pos) instanceof TestTableBlockEntity tile) {

                    var item = this.getItem(0);
                    this.tileReference = tile;
                    tile.setDisplayedItem(item);
                }
            }
        }
    }
}
