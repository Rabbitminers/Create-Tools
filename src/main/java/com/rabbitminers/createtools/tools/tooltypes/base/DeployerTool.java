package com.rabbitminers.createtools.tools.tooltypes.base;

import java.util.Set;

import com.google.common.base.CharMatcher;
import com.rabbitminers.createtools.handler.DeployerAnimationHandler;
import com.rabbitminers.createtools.handler.DeployerArmHandler;
import com.rabbitminers.createtools.handler.InputHandler;
import com.rabbitminers.createtools.render.tools.render.DeployerToolRender;
import com.rabbitminers.createtools.render.tools.render.DrillToolRender;
import com.rabbitminers.createtools.tools.ToolBase;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.outliner.Outliner;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.ArrowKnockbackEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraft.world.Container;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static com.rabbitminers.createtools.util.NBTHelper.*;

public class DeployerTool extends ToolBase {

    private Object outlineSlot = new Object();
    private final int maxVolume = 25;
    private Direction selectedFace;
    public static final String TILE_DATA_KEY = "tileData";
    public static final String[] FACING_KEYS = { "rotation", "rot", "facing", "face", "direction", "dir", "front", "forward" };


    BlockPos pos1 = null;
    BlockPos pos2 = null;

    public enum DeployerMode {
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
            this.colour = colour;
        }

        public static DeployerMode of(ItemStack stack) {
            CompoundTag nbt = stack.hasTag()
                ? stack.getTag()
                : new CompoundTag();

            if (nbt != null && nbt.contains("deployer_mode")) {
                for (DeployerMode deployerMode : DeployerMode.values()) {
                    if (nbt.getString("deployer_mode").equals(deployerMode.name))
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
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Vec3 playerFacingVector = player.getLookAngle();
        double knockback = DeployerMode.of(stack) == DeployerMode.ATTACK
                ? 3.0D
                : 1.0D;
        Vec3 appliedMotion = playerFacingVector
                .multiply(1.0D, 0.0D, 1.0D)
                .normalize()
                .scale(knockback * 0.6);
        if (appliedMotion.lengthSqr() > 0.0D)
            entity.push(appliedMotion.x, 0.1D, appliedMotion.z);

        CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();
        if (nbt == null)
            return super.onLeftClickEntity(stack, player, entity);

        DeployerAnimationHandler animationHandler = nbt.hasUUID("toolid")
                ? DeployerArmHandler.getAnimationHandlerOfUUID(nbt.getUUID("toolid"))
                : null;

        if (animationHandler != null)
            animationHandler.setRemainingFrames();

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (entity instanceof Player player) {
            Item offhandItem = player.getOffhandItem().getItem();

            CompoundTag nbt = stack.hasTag()
                    ? stack.getTag()
                    : new CompoundTag();

            if (hasTileData(stack)) {
                if (player.isCreative())
                    return;

                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 2, false, false));
            }

            if (nbt != null && InputHandler.isSwitchUsed(player) && !player.getCooldowns().isOnCooldown(this)) {
                if (!hasTileData(stack)) {
                    String currentMode = nbt.contains("deployer_mode") ? nbt.getString("deployer_mode") : "none";

                    String newMode = switch (currentMode) {
                        case "dolly" -> "attack";
                        case "attack" -> "place";
                        default -> "dolly";
                    };

                    player.getCooldowns().addCooldown(this, 20);

                    player.displayClientMessage(new TextComponent("Mode: " + newMode).withStyle(DeployerMode.of(stack).colour), true);
                    nbt.putString("deployer_mode", newMode);

                    stack.setTag(nbt);
                } else {
                    player.displayClientMessage(new TextComponent("Cannot Change Modes While Still Holding Something"), true);
                }
            }

            if (DeployerMode.of(stack) != DeployerMode.PLACE && (pos1 != null || pos2 != null))
                pos1 = pos2 = null;

            if (InputHandler.isToolUsed(player) && offhandItem instanceof BlockItem) {
                BlockItem blockItem = (BlockItem) offhandItem;
                BlockState state = blockItem.getBlock().defaultBlockState();

                if (blockItem != Items.AIR)
                    fillArea(level, state, player);
            }

            if (pos1 != null && pos2 != null) {
                AABB currentSelectionBox = new AABB(pos1, pos2).expandTowards(1, 1, 1);
                if (player.getMainHandItem() == stack)
                    outliner().chaseAABB(outlineSlot, currentSelectionBox)
                            .colored(0x6886c5) // 0x6886c5
                            .withFaceTextures(AllSpecialTextures.CHECKERED, AllSpecialTextures.HIGHLIGHT_CHECKERED)
                            .lineWidth(1 / 16f)
                            .highlightFace(selectedFace);
            }
            super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        DeployerMode mode = DeployerMode.of(useOnContext.getItemInHand());
        ItemStack stack = useOnContext.getItemInHand();
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        BlockPos pos = useOnContext.getClickedPos();

        CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();
        if (nbt == null)
            return super.useOn(useOnContext);

        DeployerAnimationHandler animationHandler = nbt.hasUUID("toolid")
                ? DeployerArmHandler.getAnimationHandlerOfUUID(nbt.getUUID("toolid"))
                : null;

        if (mode == DeployerMode.PLACE) {

            if (player.isCrouching()) {
                pos2 = getBlockPlacePos(useOnContext);
                player.displayClientMessage(new TextComponent("Set position 2"), true);
            } else {
                pos1 = getBlockPlacePos(useOnContext);
                player.displayClientMessage(new TextComponent("Set position 1"), true);
            }

            if (animationHandler != null)
                animationHandler.setRemainingFrames();
        }

        if (mode == DeployerMode.DOLLY) {
            BlockState heldBlock = NbtUtils.readBlockState(nbt);
            if (hasTileData(stack)) {
                placeBlock(useOnContext);
            } else {
                BlockEntity be = level.getBlockEntity(useOnContext.getClickedPos());
                BlockState state = level.getBlockState(pos);

                boolean succsess = storeTileData(be, level, pos, state, stack);
                if (succsess) {
                    emptyTileEntity(be);
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 512);
                }
            }

            if (animationHandler != null)
                animationHandler.setRemainingFrames();
        }

        return super.useOn(useOnContext);
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
            level.addParticle(ParticleTypes.POOF, pos.getX(), pos.getY(), pos.getY(), 0, 0.5,1);
        }

        player.displayClientMessage(new TextComponent("Placed Blocks"), true);
        pos1 = pos2 = null;
    }

    public InteractionResult placeBlock(UseOnContext context) {{
            Direction facing = context.getClickedFace();
            Player player = context.getPlayer();
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            ItemStack stack = context.getItemInHand();


            if (hasTileData(stack)) {
                try {
                    Vec3 vec = player.getLookAngle();
                    Direction facing2 = Direction.getNearest((float) vec.x, 0f, (float) vec.z);
                    BlockPos pos2 = pos;
                    Block containedblock = getBlock(stack);
                    BlockState containedstate = getBlockState(stack);
                    if (!level.getBlockState(pos2).canBeReplaced(new BlockPlaceContext(context))) {
                        pos2 = pos.relative(facing);
                    }

                    if (level.getBlockState(pos2).canBeReplaced(new BlockPlaceContext(context)) && containedblock != null) {
                        boolean canPlace = containedstate.canSurvive(level, pos2);

                        if (canPlace && player.mayUseItemAt(pos, facing, stack) && level.mayInteract(player, pos2)) {

                            BlockState placementState = containedblock.getStateForPlacement(new BlockPlaceContext(context));

                            BlockState actualState = placementState == null ? containedstate : placementState;

                            BlockSnapshot snapshot = BlockSnapshot.create(level.dimension(), level, pos2);
                            BlockEvent.EntityPlaceEvent event = new BlockEvent.EntityPlaceEvent(snapshot, level.getBlockState(pos), player);
                            MinecraftForge.EVENT_BUS.post(event);

                            if (!event.isCanceled()) {
                                level.setBlockAndUpdate(pos2, actualState);
                                if (!getTileData(stack).isEmpty()) {
                                    CompoundTag tag = getTileData(stack);
                                    Set<String> keys = tag.getAllKeys();
                                    keytester: for (String key : keys) {
                                        for (String facingKey : FACING_KEYS) {
                                            if (key.toLowerCase().equals(facingKey)) {
                                                byte type = tag.getTagType(key);
                                                switch (type) {
                                                    case 8 -> tag.putString(key, CharMatcher.javaUpperCase().matchesAllOf(tag.getString(key)) ? facing2.getOpposite().getName().toUpperCase() : facing2.getOpposite().getName());
                                                    case 3 -> tag.putInt(key, facing2.getOpposite().get3DDataValue());
                                                    case 1 -> tag.putByte(key, (byte) facing2.getOpposite().get3DDataValue());
                                                    default -> {
                                                    }
                                                }
                                                break keytester;
                                            }
                                        }
                                    }
                                }

                                BlockEntity tile = level.getBlockEntity(pos2);
                                if (tile != null) {
                                    CompoundTag data = getTileData(stack);
                                    updateTileLocation(data, pos2);
                                    tile.load(data);
                                }
                                clearTileData(stack);
                                player.playSound(actualState.getSoundType(level, pos2, player).getPlaceSound(), 1.0f, 0.5f);
                                player.getPersistentData().remove("overrideKey");
                                return InteractionResult.SUCCESS;

                            }
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }

            return InteractionResult.FAIL;
        }
    }

    public static void emptyTileEntity(BlockEntity te)
    {
        if (te != null)
        {
            for (Direction facing : Direction.values())
            {
                LazyOptional<IItemHandler> itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);

                itemHandler.ifPresent(handler -> {

                    for (int i = 0; i < handler.getSlots(); i++)
                    {
                        handler.extractItem(i, 64, false);
                    }

                });

            }

            LazyOptional<IItemHandler> itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

            itemHandler.ifPresent(handler -> {

                for (int i = 0; i < handler.getSlots(); i++)
                {
                    handler.extractItem(i, 64, false);
                }

            });

            if (te instanceof Container inv) {
                inv.clearContent();
            }

            if (te instanceof IItemHandler itemHandler1) {
                for (int i = 0; i < itemHandler1.getSlots(); i++)
                {
                    itemHandler1.extractItem(i, 64, false);
                }
            }

            te.setChanged();
        }
    }


    private Outliner outliner() {
        return CreateClient.OUTLINER;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> components, TooltipFlag p_41424_) {
        DeployerMode mode = DeployerMode.of(stack);

        components.add(new TextComponent("Mode: ")
                .withStyle(ChatFormatting.GRAY)
                .append(mode.name)
                .withStyle(mode.colour)
                .withStyle(ChatFormatting.BOLD));

        super.appendHoverText(stack, p_41422_, components, p_41424_);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new DeployerToolRender()));
    }
}
