package com.rabbitminers.createtools.tooldata;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public enum CTGeneratorTypes {

    FURNACE_ENGINE(0, "Furnace Engine", Blocks.FURNACE.asItem(), 512),
    HAND_CRANK(1, "Hand Crank", AllBlocks.HAND_CRANK.get().asItem(), 128),
    STEAM_ENGINE(2, "Steam Engine", AllBlocks.STEAM_ENGINE.get().asItem(), 1024),
    WINDMILL(3, "Windmill", AllBlocks.WINDMILL_BEARING.get().asItem(), 256),
    NONE(4, "None", Items.AIR.asItem(), 0);

    private final int id;
    private final String name;
    private final Item item;
    private final int maxSU;

    CTGeneratorTypes(int id, String name, Item item, int maxSU) {
        this.id = id;
        this.name = name;
        this.item = item;
        this.maxSU = maxSU;
    }

    public static CTGeneratorTypes of(int id) {
        for (CTGeneratorTypes generatorType : CTGeneratorTypes.values()) {
            if (generatorType.id == id)
                return generatorType;
        }
        return CTGeneratorTypes.NONE;
    }

    public static CTGeneratorTypes of(Item item) {
        for (CTGeneratorTypes generatorType : CTGeneratorTypes.values()) {
            if (generatorType.item == item)
                return generatorType;
        }
        return CTGeneratorTypes.NONE;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Item getItem() {
        return item;
    }
    public int getMaxSU() {
        return maxSU;
    }

}
