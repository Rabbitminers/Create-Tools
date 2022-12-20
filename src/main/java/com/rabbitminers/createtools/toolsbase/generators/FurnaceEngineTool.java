package com.rabbitminers.createtools.toolsbase.generators;

import com.rabbitminers.createtools.handler.InputHandler;
import com.rabbitminers.createtools.toolsbase.BaseTools;
import com.rabbitminers.createtools.util.InventoryUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;


public class FurnaceEngineTool extends DiggerItem {
    double remainingFuel = 1000;
    enum DisplayColours {
        NONE(ChatFormatting.DARK_RED, 0),
        LOW(ChatFormatting.RED, 1),
        MEDIUM(ChatFormatting.YELLOW, 300),
        FAST(ChatFormatting.GREEN, 600);

        private final ChatFormatting textColor;
        private final int minOutput;

        DisplayColours(ChatFormatting textColor, int minOutput) {
            this.textColor = textColor;
            this.minOutput = minOutput;
        }

        public ChatFormatting getTextColor() { return textColor;}
        public int minOutput() { return minOutput; }

        public static FurnaceEngineTool.DisplayColours of(int su) {
            if (su >= FAST.minOutput())
                return FAST;
            if (su >= MEDIUM.minOutput())
                return MEDIUM;
            if (su >= LOW.minOutput())
                return LOW;
            return NONE;
        }

    }

    public FurnaceEngineTool(Tier p_42961_, int p_42962_, float p_42963_, Item.Properties p_42964_) {
        super((float)p_42962_, p_42963_, p_42961_, BlockTags.MINEABLE_WITH_PICKAXE, p_42964_);
    }

    public double getRemainingFuel() {
        return remainingFuel;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }


    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Inventory inventory = Objects.requireNonNull(useOnContext.getPlayer()).getInventory();
        int burnTime = ForgeHooks.getBurnTime(useOnContext.getPlayer().getOffhandItem(), RecipeType.SMELTING);
        ItemStack stack = useOnContext.getItemInHand();

        // EntityPlayer.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack);

        if (inventory.offhand.isEmpty())
            return super.useOn(useOnContext);

        if (burnTime > 0 && useOnContext.getPlayer().isCrouching()) {
            remainingFuel += burnTime*useOnContext.getPlayer().getOffhandItem().getCount();

            useOnContext.getPlayer().setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.AIR));
        }

        return super.useOn(useOnContext);
    }



    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (remainingFuel > 0) {
            remainingFuel--;
        } else {
            if (entity instanceof LivingEntity player) {
                Item item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
                if (stack.getItem() == item)
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 0, 10));
            }
        }

        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag p_41424_) {
        components.add(new TextComponent(String.valueOf("Time Remaining:" + Math.ceil(remainingFuel / 20) + "s"))
                .withStyle(DisplayColours.of((int) Math.floor(remainingFuel / 20)).getTextColor()));
        super.appendHoverText(stack, level, components, p_41424_);
    }

    /*
    Save last fuel count on disconnect for all tools
     */

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getPlayer();

        List<Integer> index = InventoryUtil.getAllInventorySlotIndex(
                player, BaseTools.DEPLOYER_TOOL.get()
        );

        for (int slot : index) {
            ItemStack stack = player.getInventory().getItem(slot);
            CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();

            nbt.putDouble("fuel", remainingFuel);
            stack.setTag(nbt);
        }
    }
}
