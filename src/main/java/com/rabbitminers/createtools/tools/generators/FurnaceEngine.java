package com.rabbitminers.createtools.tools.generators;

import com.rabbitminers.createtools.handler.GeneratorHandler;
import com.rabbitminers.createtools.tooldata.CTGeneratorTypes;
import com.rabbitminers.createtools.tools.ToolBase;
import com.rabbitminers.createtools.tools.data.display.FurnaceEngineFuelData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;
import java.util.Objects;

import static com.rabbitminers.createtools.util.InventoryUtil.isFlywheelPresent;

public class FurnaceEngine extends Generator {
    public double remainingTicks;
    public int runningTicks;

    int warmUpTicks = 300;

    public FurnaceEngine() {
        super(CTGeneratorTypes.FURNACE_ENGINE.getMaxSU());
        this.remainingTicks = 0;
        this.runningTicks = 0;
    }

    public int getSU() {
        return this.SUout;
    }

    public void autoFuel(Player player, CompoundTag nbt) {
        Inventory inventory = player.getInventory();

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack currentStack = player.getInventory().getItem(i);
            int burnTime = ForgeHooks.getBurnTime(currentStack, RecipeType.SMELTING);

            if (burnTime > 0 && nbt.contains("toolid") && currentStack.getItem() instanceof ToolBase) {
                if (GeneratorHandler.getGeneratorOfUUID(nbt.getUUID("toolid")) instanceof FurnaceEngine furnaceEngine) {
                    furnaceEngine.remainingTicks += burnTime;
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Entity entity) {
        if (this.remainingTicks > 0) {
            this.remainingTicks = isFlywheelPresent(stack) ? remainingTicks-0.75 : remainingTicks-1;
            this.runningTicks++;

            this.SUout = maxSU*(this.runningTicks/this.warmUpTicks);
        } else {
            this.runningTicks = 0;
            this.SUout = 0;
        }

        CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();
        if (nbt.contains("autofuel") && nbt.getBoolean("autofuel") && entity instanceof Player player)
            this.autoFuel(player, nbt);
    }

    @Override
    public void useTick(UseOnContext useOnContext) {
        Inventory inventory = Objects.requireNonNull(useOnContext.getPlayer()).getInventory();
        int burnTime = ForgeHooks.getBurnTime(useOnContext.getPlayer().getOffhandItem(), RecipeType.SMELTING);
        ItemStack stack = useOnContext.getItemInHand();

        if (inventory.offhand.isEmpty())
            return;

        if (burnTime > 0 && useOnContext.getPlayer().isCrouching()) {
            this.remainingTicks += burnTime*useOnContext.getPlayer().getOffhandItem().getCount();

            useOnContext.getPlayer().setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.AIR));
        }
    }
    
    @Override
    public List<Component> appendDisplayText(List<Component> components) {
        components.add(new TextComponent("Time Remaining:" + Math.ceil(this.remainingTicks / 20) + "s")
                .withStyle(FurnaceEngineFuelData.of((int) Math.floor(this.remainingTicks / 40)).getTextColor()));
        return components;
    }
}
