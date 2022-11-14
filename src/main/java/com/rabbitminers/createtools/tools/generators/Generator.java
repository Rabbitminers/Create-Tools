package com.rabbitminers.createtools.tools.generators;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

public class Generator{

    int maxSU;
    public int SUout;

    public float rpm;
    public int maxRPM;

    public Generator(int maxSU) {
        this.SUout = 0;
        this.maxSU = maxSU;
        this.rpm = 0;
        // TODO: CONFIG!
        this.maxRPM = 255;
    }
    public void inventoryTick(ItemStack stack, Entity entity) {
        return;
    }
    public void useTick(UseOnContext useOnContext) {
        return;
    }
    public List<Component> appendDisplayText(List<Component> components) {
        return components;
    }

    public int getSUout() {
        return SUout;
    }

    public float getRPM() {
        return rpm;
    }
}
