package com.rabbitminers.createtools.util;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum CTComponents {
    // TODO: make name and description into translatable components

    BLAZE_BURNER("Blaze Burner", AllItems.EMPTY_BLAZE_BURNER.get(), 32, "Hello world"),
    CRUSHING_WHEEL("Crushing Wheel", AllBlocks.CRUSHING_WHEEL.get().asItem(), 64, "Hello world"),
    MILL_STONE("Mill Stone", AllBlocks.MILLSTONE.get().asItem(), 16, "Hello world"),
    ANDESITE_FILTER("Andesite Filter", AllItems.FILTER.get(), 16, "Hello world"),
    EXTENDO_GRIP("Extend-o-Grip", AllItems.EXTENDO_GRIP.get(), 16, "Hello world");

    private final String name;
    private final Item item;
    private final int SUconsumption;
    private final String description;
    CTComponents(String name, Item item, int sUconsumption, String description) {
        this.name = name;
        this.item = item;
        SUconsumption = sUconsumption;
        this.description = description;
    }

    public static CTComponents of(Item item) {
        for (CTComponents component : CTComponents.values()) {
            if (component.item == item)
                return component;
        }
        return null;
    }

    public boolean isValidComponent(Item item) {
        for (CTComponents component : CTComponents.values()) {
            if (component.item == item)
                return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }
    public Item getItem() {
        return item;
    }
    public int getSUconsumption() {
        return SUconsumption;
    }
    public String getDescription() {
        return description;
    }
}