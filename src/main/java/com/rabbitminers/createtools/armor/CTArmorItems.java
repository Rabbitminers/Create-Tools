package com.rabbitminers.createtools.armor;

import com.rabbitminers.createtools.CreateTools;
import com.rabbitminers.createtools.armor.extendoboots.ExtendoBootsItem;
import com.simibubi.create.Create;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CTArmorItems {
    public static final DeferredRegister<Item> ARMOR =
            DeferredRegister.create(ForgeRegistries.ITEMS, CreateTools.MODID);

    public static final RegistryObject<Item> EXTENDOBOOTS = ARMOR.register("extendo_boots",
            () -> new ExtendoBootsItem(CTArmorMaterial.IRON, EquipmentSlot.FEET,
                    new Item.Properties().tab(CreateTools.itemGroup)));

    public static void register(IEventBus eventBus) {
        ARMOR.register(eventBus);
    }
}
