package com.rabbitminers.createtools.blocks.draftingtable;

import com.rabbitminers.createtools.CreateTools;
import com.rabbitminers.createtools.blocks.testtable.TestTableBlock;
import com.rabbitminers.createtools.blocks.testtable.TestTableBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class DraftingTableBlockRegistry {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CreateTools.MODID);

    public static final RegistryObject<Block> DRAFTING_TABLE = registerBlock("drafting_table",
            () -> new DraftingTable(BlockBehaviour
                    .Properties.of(Material.METAL)
                    .noOcclusion()
                    .strength(9f)),
            CreateTools.itemGroup
    );

    public static final RegistryObject<Block> TEST = registerBlock("test_table",
            () -> new TestTableBlock(BlockBehaviour
                    .Properties.of(Material.METAL)
                    .noOcclusion()
                    .strength(9f)),
            CreateTools.itemGroup
    );

    private static <T extends Block> RegistryObject<T> registerBlock(
            String name,
            Supplier<T> block,
            CreativeModeTab tab
    ) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }
    private static <T extends Block> RegistryObject<Item> registerBlockItem(
            String name,
            RegistryObject<T> block,
            CreativeModeTab tab
    ) {
        return DraftingTableItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
