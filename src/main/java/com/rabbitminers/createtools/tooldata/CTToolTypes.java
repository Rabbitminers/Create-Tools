package com.rabbitminers.createtools.tooldata;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import net.minecraft.world.item.Item;

public enum CTToolTypes {

    DRILL("Drill Tool", AllBlocks.MECHANICAL_DRILL.get().asItem()),
    HARVESTER("Harvester Tool", AllBlocks.MECHANICAL_HARVESTER.get().asItem()),
    SAW("Saw Tool", AllBlocks.MECHANICAL_SAW.get().asItem()),
    DEPLOYER("Deployer Tool", AllBlocks.DEPLOYER.get().asItem()),
    PLOUGH("Plough Tool", AllBlocks.MECHANICAL_PLOUGH.get().asItem()),
    POTATOCANNON("Potato Cannon Tool", AllItems.POTATO_CANNON.get());
    private String name;
    private Item item;

    CTToolTypes(String name, Item item) {
        this.name = name;
        this.item = item;
    }
}
