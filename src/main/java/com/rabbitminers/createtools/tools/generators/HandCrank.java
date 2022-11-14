package com.rabbitminers.createtools.tools.generators;

import com.rabbitminers.createtools.tooldata.CTGeneratorTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class HandCrank extends Generator {
    int remainingTicks;
    int maxTicks;
    int increment;

    public HandCrank() {
        super(CTGeneratorTypes.HAND_CRANK.getMaxSU());
        this.remainingTicks = 0;
        this.maxTicks = 1200;
        this.increment = 25;
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public int getRemainingTicks() {
        return this.remainingTicks;
    }

    public int getMaxTicks() {
        return this.maxTicks;
    }
    @Override
    public void useTick(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        InteractionHand interactionHand = useOnContext.getHand();

        if (!player.isCrouching())
            return;

        // Todo: add flywheel functionallity :)

        if (this.remainingTicks >= this.maxTicks) {
            System.out.println("FULL");
            return;
        }

        this.remainingTicks += increment;
        player.displayClientMessage(new TextComponent(Math.floor(this.remainingTicks/40.0f) + "s/" + Math.floor(this.maxTicks/40.0f) + "s"), true);
    }

    public boolean isFull() {
        return this.remainingTicks >= maxTicks;
    }

    @Override
    public void inventoryTick(ItemStack stack, Entity entity) {
        if (this.remainingTicks > 0) {
            this.remainingTicks--;

            SUout = maxSU;
            rpm = 16;
        } else {
            SUout = 0;
            rpm = 0;
        }
    }

    @Override
    public List<Component> appendDisplayText(List<Component> components) {
        components.add(new TextComponent("Time Remaining: [" + Math.floor(this.remainingTicks / 40.0f) + "s]"));
        return components;
    }
}
