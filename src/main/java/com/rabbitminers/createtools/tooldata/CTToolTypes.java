package com.rabbitminers.createtools.tooldata;

import com.rabbitminers.createtools.tools.ToolBase;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum CTToolTypes {

    DRILL("Drill Tool", AllBlocks.MECHANICAL_DRILL.get().asItem()),
    HARVESTER("Harvester Tool", AllBlocks.MECHANICAL_HARVESTER.get().asItem()),
    SAW("Saw Tool", AllBlocks.MECHANICAL_SAW.get().asItem()),
    DEPLOYER("Deployer Tool", AllBlocks.DEPLOYER.get().asItem()),
    PLOUGH("Plough Tool", AllBlocks.MECHANICAL_PLOUGH.get().asItem()),
    POTATOCANNON("Potato Cannon Tool", AllItems.POTATO_CANNON.get());
    private String name;
    private Item item;
    private ToolBase toolClass;

    CTToolTypes(String name, Item item) {
        this.name = name;
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public String getName() { return name; }

    public static CTToolTypes of(Item item) {
        for (CTToolTypes baseItem : CTToolTypes.values()) {
            if (item == baseItem.getItem())
                return baseItem;
        }
        return null;
    }


}
