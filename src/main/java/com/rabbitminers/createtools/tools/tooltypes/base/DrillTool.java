package com.rabbitminers.createtools.tools.tooltypes.base;

import com.rabbitminers.createtools.render.tools.render.DrillToolRender;
import com.rabbitminers.createtools.tools.ToolBase;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static net.minecraft.world.entity.boss.wither.WitherBoss.canDestroy;

public class DrillTool extends ToolBase {
    private Object outlineSlot = new Object();
    private Direction selectedFace;
    private final TagKey<Block> blocks;
    protected final float speed;
    BlockPos pos1 = new BlockPos(0, 0, 0);
    BlockPos pos2 = new BlockPos(0, 0, 0);

    private final int depth;
    private final int radius;

    public DrillTool(Properties properties, TagKey<Block> blocks, float speed) {
        super(properties);
        this.blocks = blocks;
        this.speed = speed;
        this.depth = 1;
        this.radius = 3;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Minecraft mc = Minecraft.getInstance();
        HitResult hitResult = mc.hitResult;
        if (hitResult instanceof BlockHitResult blockHitResult && level.getBlockState(blockHitResult.getBlockPos()) != Blocks.AIR.defaultBlockState() && entity instanceof Player player && player.getMainHandItem() == stack) {
            BlockPos pos = blockHitResult.getBlockPos();

            switch (blockHitResult.getDirection()) {
                case DOWN, UP -> {
                    pos1 = new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()-1);
                    pos2 = new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()+1);
                }
                case NORTH, SOUTH -> {
                    pos1 = new BlockPos(pos.getX()-1, pos.getY()-1, pos.getZ());
                    pos2 = new BlockPos(pos.getX()+1, pos.getY()+1, pos.getZ());
                }
                case WEST, EAST -> {
                    pos1 = new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()-1);
                    pos2 = new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()+1);
                }
            };

            AABB currentSelectionBox = player.isShiftKeyDown()
                    ? new AABB(pos, pos).expandTowards(1, 1, 1)
                    : new AABB(pos1, pos2).expandTowards(1, 1, 1);

            outliner().chaseAABB(outlineSlot, currentSelectionBox)
                    .colored(0xffffff) // 0x6886c5
                    .withFaceTextures(AllSpecialTextures.CHECKERED, AllSpecialTextures.HIGHLIGHT_CHECKERED)
                    .lineWidth(1 / 16f)
                    .highlightFace(selectedFace);

        }

        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
    }


    @Override
    public int getMaxDamage(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new DrillToolRender()));
    }

    private Outliner outliner() {
        return CreateClient.OUTLINER;
    }

    @Override
    public boolean mineBlock(ItemStack hammerStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if (level.isClientSide /*|| blockState.getDestroySpeed(level, blockPos) == 0.0F*/) {
            return true;
        }

        HitResult pick = livingEntity.pick(20D, 1F, false);

        if (!(pick instanceof BlockHitResult)) {
            return super.mineBlock(hammerStack, level, blockState, blockPos, livingEntity);
        }

        if (livingEntity instanceof Player player && !player.isShiftKeyDown())
            this.findAndBreakNearBlocks(pick, blockPos, hammerStack, level, livingEntity);
        return super.mineBlock(hammerStack, level, blockState, blockPos, livingEntity);
    }

    public void findAndBreakNearBlocks(HitResult pick, BlockPos blockPos, ItemStack hammerStack, Level level, LivingEntity livingEntity) {
        if (!(livingEntity instanceof ServerPlayer player)) return;
        var size = (radius / 2);
        var offset = size - 1;

        Direction direction = ((BlockHitResult) pick).getDirection();
        var boundingBox = switch (direction) {
            case DOWN, UP -> new BoundingBox(blockPos.getX() - size, blockPos.getY() - (direction == Direction.UP ? depth - 1 : 0), blockPos.getZ() - size, blockPos.getX() + size, blockPos.getY() + (direction == Direction.DOWN ? depth - 1 : 0), blockPos.getZ() + size);
            case NORTH, SOUTH -> new BoundingBox(blockPos.getX() - size, blockPos.getY() - size + offset, blockPos.getZ() - (direction == Direction.SOUTH ? depth - 1 : 0), blockPos.getX() + size, blockPos.getY() + size + offset, blockPos.getZ() + (direction == Direction.NORTH ? depth - 1 : 0));
            case WEST, EAST -> new BoundingBox(blockPos.getX() - (direction == Direction.EAST ? depth - 1 : 0), blockPos.getY() - size + offset, blockPos.getZ() - size, blockPos.getX() + (direction == Direction.WEST ? depth - 1 : 0), blockPos.getY() + size + offset, blockPos.getZ() + size);
        };

        int damage = 0;
        Iterator<BlockPos> iterator = BlockPos.betweenClosedStream(boundingBox).iterator();
        Set<BlockPos> removedPos = new HashSet<>();
        while (iterator.hasNext()) {
            var pos = iterator.next();

            BlockState targetState = level.getBlockState(pos);
            if (pos == blockPos || removedPos.contains(pos)) {
                continue;
            }

            removedPos.add(pos);
            level.destroyBlock(pos, false, livingEntity);
            if (!player.isCreative()) {
                targetState.spawnAfterBreak((ServerLevel) level, pos, hammerStack);
                List<ItemStack> drops = Block.getDrops(targetState, (ServerLevel) level, pos, level.getBlockEntity(pos), livingEntity, hammerStack);
                drops.forEach(e -> Block.popResourceFromFace(level, pos, ((BlockHitResult) pick).getDirection(), e));
            }
            damage ++;
        }

        if (damage != 0 && !player.isCreative()) {
            hammerStack.hurtAndBreak(damage, livingEntity, (livingEntityx) -> {
                livingEntityx.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
    }
    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos blockPos, Player player) {
        return getGeneratorOfTool(player.getMainHandItem()).getRPM() > 0;
    }


}
