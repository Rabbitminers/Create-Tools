package com.rabbitminers.createtools.toolsbase.generators;

import com.simibubi.create.AllItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;


public class FurnaceEngineTool extends DiggerItem {

    double remainingFuel = 4000;
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
        if (remainingFuel > 0)
            return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
        return false;
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
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        if (remainingFuel > 0)
            remainingFuel--;
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag p_41424_) {
        components.add(new TextComponent(String.valueOf(Math.ceil(remainingFuel/20)+"s")));

        super.appendHoverText(stack, level, components, p_41424_);
    }
}