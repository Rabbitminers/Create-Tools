package com.rabbitminers.createtools.tooldata;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public enum CTComponents {
    // TODO: make name and description into translatable components

    BLAZE_BURNER(0, "Blaze Burner", AllItems.EMPTY_BLAZE_BURNER.get(), 32, "Hello world"),
    CRUSHING_WHEEL(1, "Crushing Wheel", AllBlocks.CRUSHING_WHEEL.get().asItem(), 64, "Hello world"),
    MILL_STONE(2, "Mill Stone", AllBlocks.MILLSTONE.get().asItem(), 16, "Hello world"),
    ANDESITE_FILTER(3, "Andesite Filter", AllItems.FILTER.get(), 16, "Hello world"),
    EXTENDO_GRIP(4, "Extend-o-Grip", AllItems.EXTENDO_GRIP.get(), 16, "Hello world"),
    ROTATIONAL_SPEED_CONTROLLER(5, "Rotational Speed Controller", AllBlocks.ROTATION_SPEED_CONTROLLER.get().asItem(), 32, "Hello world"),
    MECHANICAL_ARM(6, "Mechanical Arm", AllBlocks.MECHANICAL_ARM.get().asItem(), 48, "Hello world"),
    SPYGLASS(7, "Spyglass", Items.SPYGLASS.asItem(), 16, "Hello world!"),
    FLYWHEEL(8, "Flywheel", AllBlocks.FLYWHEEL.get().asItem(), 0, "Hello world!"),
    CLOCK(9, "Clock", Items.CLOCK.asItem(), 16 ,"Hello world!"),
    ELECTRON_TUBE(10, "Electron Tube", AllItems.ELECTRON_TUBE.get(), 24, "Hello world!"),
    ENCASED_FAN(11, "Encased Fan", AllBlocks.ENCASED_FAN.get().asItem(), 32, "Hello world!"),
    COMPASS(12, "Compass", Items.COMPASS.asItem(), 16, "Hello world!");

    private final int id;
    private final String name;
    private final Item item;
    private final int SUconsumption;
    private final String description;

    CTComponents(int id, String name, Item item, int SUconsumption, String description) {
        this.id = id;
        this.name = name;
        this.item = item;
        this.SUconsumption = SUconsumption;
        this.description = description;
    }

    public static CTComponents of(Item item) {
        for (CTComponents component : CTComponents.values()) {
            if (component.item == item)
                return component;
        }
        return null;
    }

    public static CTComponents of(int id) {
        for (CTComponents component : CTComponents.values()) {
            if (component.id == id)
                return component;
        }
        return null;
    }

    public static CTComponents of(String name) {
        for (CTComponents component : CTComponents.values()) {
            if (component.name == name)
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
    public int getId() { return id; }
    public String getDescription() {
        return description;
    }
}