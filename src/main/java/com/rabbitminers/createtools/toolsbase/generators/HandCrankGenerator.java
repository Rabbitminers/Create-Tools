package com.rabbitminers.createtools.toolsbase.generators;

import com.ibm.icu.text.DecimalFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;

import java.util.List;

public class HandCrankGenerator extends DiggerItem {

    int remainingTicks = 0;
    int maxTicks = 1200;

    public HandCrankGenerator(float p_204108_, float p_204109_, Tier p_204110_, TagKey<Block> p_204111_, Properties p_204112_) {
        super(p_204108_, p_204109_, p_204110_, p_204111_, p_204112_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!player.isCrouching())
            return super.use(level, player, interactionHand);

        if (remainingTicks >= maxTicks) {
            System.out.println("FULL");
            return super.use(level, player, interactionHand);
        }

        remainingTicks += 25;

        return super.use(level, player, interactionHand);
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(new TextComponent("Time Remaining: [" + Math.floor(remainingTicks / 40.0f) + "s]"));

        super.appendHoverText(stack, level, components, flag);
    }


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (remainingTicks > 0) {
            remainingTicks--;
        } else {
            LivingEntity player = ((LivingEntity) entity);

            Item item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
            if (stack.getItem() == item)
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 0, 10));
        }

        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
    }

    // TODO: Add crank animation
    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return super.getUseAnimation(p_41452_);
    }
}
