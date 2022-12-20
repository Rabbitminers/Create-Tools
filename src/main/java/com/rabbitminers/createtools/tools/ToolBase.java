package com.rabbitminers.createtools.tools;

import com.mojang.math.Vector3f;
import com.rabbitminers.createtools.handler.GeneratorHandler;
import com.rabbitminers.createtools.tooldata.CTGeneratorTypes;
import com.rabbitminers.createtools.tooldata.CTToolTypes;
import com.rabbitminers.createtools.tooldata.CTComponents;
import com.rabbitminers.createtools.tools.generators.*;
import com.rabbitminers.createtools.render.tools.render.DrillToolRender;
import com.rabbitminers.createtools.tools.tooltypes.base.DeployerTool;
import com.rabbitminers.createtools.util.ToolUtils;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.contraptions.components.fan.AirCurrent;
import com.simibubi.create.content.contraptions.components.fan.EncasedFanRenderer;
import com.simibubi.create.content.contraptions.components.fan.EncasedFanTileEntity;
import com.simibubi.create.content.contraptions.particle.AirFlowParticle;
import com.simibubi.create.content.contraptions.particle.AirFlowParticleData;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.LeashKnotRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.simibubi.create.content.contraptions.components.fan.AirCurrent.isPlayerCreativeFlying;

public class ToolBase extends Item {
    CTToolTypes toolType;
    private int SUout;
    private double remainingTicks;
    public ToolBase(Properties properties) {
        super(properties);
        this.SUout = 0;
        this.remainingTicks = 0;
    }
    public List<CTComponents> getActiveComponents(ItemStack stack) {
        if (!stack.hasTag())
            return new ArrayList<CTComponents>();

        CompoundTag nbt = stack.getTag();
        List<CTComponents> components = new ArrayList<>();

        for (int id : nbt.getIntArray("components")) {
            components.add(CTComponents.of(id));
        }

        return components;
    }

    public CTGeneratorTypes getGeneratorType(ItemStack stack) {
        if (!stack.hasTag())
            return CTGeneratorTypes.NONE;

        CompoundTag nbt = stack.getTag();
        return CTGeneratorTypes.of(nbt.getInt("generator"));
    }

    public int getRPM(ItemStack stack) {
        if (getActiveComponents(stack).contains(CTComponents.ROTATIONAL_SPEED_CONTROLLER))
            return 0;
        else {
            return (int) Math.floor(getGeneratorOfTool(stack).getRPM());
        }
    }

    public boolean isValidComponent(ItemStack stack) {
        return false;
    }
    public int getSUcost(ItemStack stack) {
        int SUcost = 0;
        System.out.println(SUcost);
        for (CTComponents component : getActiveComponents(stack))
            SUcost += component.getSUconsumption();
        return SUcost;
    }

    public int getSUoutput(ItemStack stack) {
        int out = 0;
        for (CTComponents component : this.getActiveComponents(stack))
            out += component.getSUconsumption();
        return out;
    }
    public ItemStack getBlazeBurnerDrops(ItemStack stack, Level level) {
        ItemStack smeltedItem = level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), level)
                .map(SmeltingRecipe::getResultItem)
                .filter(itemStack -> !itemStack.isEmpty())
                .map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
                .orElse(stack);

        return smeltedItem;
    }

    public void preventBlockBreak(Entity entity, ItemStack stack) {
        if (entity instanceof LivingEntity player) {
            Item item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
            if (stack.getItem() == item)
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 0, 10));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Generator generator = this.getGeneratorOfTool(useOnContext.getItemInHand());
        generator.useTick(useOnContext);

        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        BlockPos pos = useOnContext.getClickedPos();
        Direction sideHit = useOnContext.getClickedFace();
        BlockState state = level.getBlockState(pos);


        if (this.toolContainsComponent(player.getMainHandItem(), CTComponents.BLAZE_BURNER)) {
            if (!(generator instanceof HandCrank handCrank && handCrank.isFull()))
                ignite(level, pos, state, sideHit, useOnContext.getHorizontalDirection(), player);
        }

        return super.useOn(useOnContext);
    }

    public void addUUID(ItemStack stack) {
        CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();
        if (!nbt.hasUUID("toolid"))
            nbt.putUUID("toolid", UUID.randomUUID());
        stack.setTag(nbt);
    }

    public Generator getGeneratorOfTool(ItemStack stack) {
        CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();
        if (nbt != null && nbt.hasUUID("toolid")) {
            Generator generator = GeneratorHandler.getGeneratorOfUUID(nbt.getUUID("toolid"));
            if (generator != null) {
                return generator;
            } else {
                CTGeneratorTypes generatorType;
                generatorType = nbt.contains("generator")
                        ? CTGeneratorTypes.of(nbt.getInt("generator"))
                        : CTGeneratorTypes.NONE;

                GeneratorHandler.update(nbt.getUUID("toolid"), switch (generatorType.getId()) {
                    case 0 -> new FurnaceEngine();
                    case 1 -> new HandCrank();
                    case 2 -> new SteamEngine();
                    case 3 -> new Windmill();
                    default -> new Generator(0);
                });
            }
        }
        return new Generator(0);
    }


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (entity instanceof Player player) {
            ItemStack mainHandItem = player.getMainHandItem();

            this.addUUID(stack);
            this.addUUID(mainHandItem);

            CompoundTag stackNBT = stack.hasTag() ? stack.getTag() : new CompoundTag();
            CompoundTag heldToolNBT = mainHandItem.hasTag() ? mainHandItem.getTag() : new CompoundTag();

            Generator generator = this.getGeneratorOfTool(stack);
            generator.inventoryTick(stack, entity);

            if (mainHandItem.getItem() instanceof ToolBase) {
                UUID stackUUID = stackNBT.getUUID("toolid");
                UUID heldToolUUID = heldToolNBT.getUUID("toolid");
                /*
                if (stackUUID.equals(heldToolUUID) && generator.SUout <= 0)
                    // player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 0, 10));
                    System.out.println();
                */
                if (this.toolContainsComponent(stack, CTComponents.ENCASED_FAN) && generator.SUout >= 0)
                    encasedFanTick(stack, level, entity, p_41407_, p_41408_);
            }

        }

        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
    }
    public void encasedFanTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected){
        CompoundTag tag = stack.getOrCreateTag();
        int r = 5;
        AABB area = new AABB(entityIn.position().add(-r, -r, -r), entityIn.position().add(r, r, r));

        List<ItemEntity> items = worldIn.getEntities(EntityType.ITEM, area,
                item -> item.isAlive() && (!worldIn.isClientSide || item.tickCount > 1) &&
                        (item.getThrower() == null || !item.getThrower().equals(entityIn.getUUID()) || !item.hasPickUpDelay()) &&
                        !item.getItem().isEmpty() && !item.getPersistentData().contains("PreventRemoteMovement")
        );
        items.forEach(item -> item.setPos(entityIn.getX(), entityIn.getY(), entityIn.getZ()));
        // Directly add items to the player's inventory when ItemPhysic is installed
        if(!worldIn.isClientSide && entityIn instanceof Player && ModList.get().isLoaded("itemphysic"))
            items.forEach(item -> playerTouch(item, (Player)entityIn));


        if(!worldIn.isClientSide && entityIn instanceof Player){
            Player player = (Player)entityIn;
            List<ExperienceOrb> orbs = worldIn.getEntitiesOfClass(ExperienceOrb.class, area);
            orbs.forEach(orb -> {
                orb.invulnerableTime = 0;
                player.takeXpDelay = 0;
                orb.playerTouch(player);
            });
        }

        if (entityIn instanceof Player player) {
            Vector3f ParticleColour = new Vector3f(Vec3.fromRGB24(16777215));
            Vec3 playerFacingVector = player.getLookAngle();

            Vec3 appliedMotion = playerFacingVector
                    .multiply(0.5D, 0.5D, 0.5D)
                    .normalize()
                    .scale(0.6*2);
            worldIn.addParticle(new DustParticleOptions(ParticleColour, 0.4F), player.getX()+playerFacingVector.x, player.getY()+1.5, player.getZ()+playerFacingVector.y, appliedMotion.x, appliedMotion.y, appliedMotion.z);
        }

        HitResult objectMouseOver = Minecraft.getInstance().hitResult;
        if (objectMouseOver instanceof EntityHitResult entityHitResult && entityIn instanceof Player player) {
            Entity entity = entityHitResult.getEntity();

            if (!(entity instanceof LivingEntity))
                return;

            LivingEntity livingEntity = (LivingEntity) entity;
            Vec3 playerFacingVector = player.getLookAngle();

            Vec3 appliedMotion = playerFacingVector
                    .multiply(1.0D, 0.0D, 1.0D)
                    .normalize()
                    .scale(10 * 0.6);

            if (appliedMotion.lengthSqr() > 0.0D) {
                System.out.println("Pushed!");
                System.out.println(appliedMotion);
                livingEntity.push(appliedMotion.x, 0.1D, appliedMotion.z);
                livingEntity.causeFallDamage(100, 100, DamageSource.FALL);
            }

        }
    }

    private static void playerTouch(ItemEntity itemEntity, Player player){
        if (!itemEntity.level.isClientSide) {
            if (itemEntity.hasPickUpDelay()) return;
            ItemStack itemstack = itemEntity.getItem();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();

            int hook = net.minecraftforge.event.ForgeEventFactory.onItemPickup(itemEntity, player);
            if (hook < 0) return;

            ItemStack copy = itemstack.copy();

            if (!itemEntity.hasPickUpDelay() && (itemEntity.getOwner() == null || itemEntity.lifespan - itemEntity.getAge() <= 200 || itemEntity.getOwner().equals(player.getUUID())) && (hook == 1 || i <= 0 || player.getInventory().add(itemstack))) {
                copy.setCount(copy.getCount() - itemstack.getCount());
                net.minecraftforge.event.ForgeEventFactory.firePlayerItemPickupEvent(player, itemEntity, copy);
                player.take(itemEntity, i);

                if (itemstack.isEmpty()) {
                    itemEntity.discard();
                    itemstack.setCount(i);
                }

                player.awardStat(Stats.ITEM_PICKED_UP.get(item), i);
                player.onItemPickup(itemEntity);
            }

        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> components, TooltipFlag p_41424_) {
        Generator generator = this.getGeneratorOfTool(stack);
        components = generator.appendDisplayText(components);

        components.add(new TextComponent("Generator: " + getGeneratorType(stack).getName()));
        components.add(new TextComponent("Components: ").withStyle(ChatFormatting.GRAY));

        for (CTComponents component : getActiveComponents(stack)) {
            if (component == null) continue;
            String componentName = component.getName() != null
                    ? component.getName()
                    : "Invalid Component";
            components.add(new TextComponent("- " + componentName).withStyle(ChatFormatting.DARK_GRAY));
            if (Screen.hasShiftDown())
                components.add(new TextComponent(component.getDescription()).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
        }

        super.appendHoverText(stack, p_41422_, components, p_41424_);
    }

    public boolean toolContainsComponent(ItemStack stack, CTComponents componentSearch) {
        CompoundTag nbt = stack.hasTag()
                ? stack.getTag()
                : new CompoundTag();

        if (nbt == null || !nbt.contains("components"))
            return false;

        for (int component : nbt.getIntArray("components")) {
            if (component == componentSearch.getId())
                return true;
        }
        return false;
    }
    @Override
    public int getUseDuration(ItemStack stack) {
        if (this.toolContainsComponent(stack, CTComponents.SPYGLASS))
            return 1200;

        return super.getUseDuration(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        if (this.toolContainsComponent(stack, CTComponents.SPYGLASS))
            return UseAnim.SPYGLASS;
        return super.getUseAnimation(stack);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (this.toolContainsComponent(player.getMainHandItem(), CTComponents.SPYGLASS))
            return ItemUtils.startUsingInstantly(level, player, hand);
        return super.use(level, player, hand);
    }


    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos blockPos, LivingEntity livingEntity) {
        if (this.toolContainsComponent(stack, CTComponents.MECHANICAL_ARM)
            && livingEntity instanceof Player player
            && player.getOffhandItem().getItem() instanceof BlockItem blockItem
        ) {
            player.getOffhandItem().shrink(1);
            level.setBlock(blockPos, blockItem.getBlock().defaultBlockState(), 512);
        }
        return super.mineBlock(stack, level, state, blockPos, livingEntity);
    }

    private static boolean ignite(Level level, BlockPos pos, BlockState state, Direction sideHit, Direction horizontalFacing, @Nullable Player player) {
        if (CampfireBlock.canLight(state) || CandleBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
            level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 0.5f * 0.4F + 0.8F);
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
            return true;
        }

        if (state.getBlock() instanceof TntBlock tnt) {
            tnt.onCaughtFire(state, level, pos, sideHit, player);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
            return true;
        }

        BlockPos offset = pos.relative(sideHit);
        if (BaseFireBlock.canBePlacedAt(level, offset, horizontalFacing)) {
            level.playSound(player, offset, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 0.5f * 0.4F + 0.8F);
            level.setBlock(offset, BaseFireBlock.getState(level, offset), 11);
            return true;
        }
        return false;
    }

    /*
    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new DrillToolRender()));
    }
     */
}

