package com.rabbitminers.createtools.toolsbase;

import com.rabbitminers.createtools.CreateTools;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BaseTools {
    public static final DeferredRegister<Item> BASETOOLS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CreateTools.MODID);

    public static final RegistryObject<Item> DRILL = BASETOOLS.register("drill",
            () -> new PickaxeItem(CTTiers.CREATETOOLMAX, 2, 3f,
                    new Item.Properties().tab(CreativeModeTab.TAB_BREWING)));
    public static void register(IEventBus eventBus) {
        BASETOOLS.register(eventBus);
    }
}
