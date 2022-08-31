package com.rabbitminers.createtools.blocks.draftingtable;

import com.rabbitminers.createtools.CreateTools;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DraftingTableItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, CreateTools.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
