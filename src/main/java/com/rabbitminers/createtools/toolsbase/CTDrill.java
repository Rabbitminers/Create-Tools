package com.rabbitminers.createtools.toolsbase;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class CTDrill extends DiggerItem {

    public CTDrill(Tier p_42961_, int p_42962_, float p_42963_, Item.Properties p_42964_) {
        super((float)p_42962_, p_42963_, p_42961_, BlockTags.MINEABLE_WITH_PICKAXE, p_42964_);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level level,
            List<Component> components,
            TooltipFlag tooltipFlag
    ) {
        if (stack.hasTag()) {
            String fuelCount = stack.getTag().getAsString();
            components.add(new TextComponent(fuelCount));
        }

        super.appendHoverText(stack, level, components, tooltipFlag);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        /*
        TODO: UPDATE NBT DATA
        */
        System.out.println(stack);
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (player.getItemInHand(interactionHand).hasTag()) {
            player.getItemInHand(interactionHand).setTag(new CompoundTag());
        }

        return super.use(level, player, interactionHand);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        /*
        TODO: READ NBT DATA
        if fuel < 0 prevent block breaking
         */

        return super.onBlockStartBreak(itemstack, pos, player);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack p_150899_) {
        return false;
    }

    // Is enchanted
    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return super.isFoil(p_41453_);
    }
}
