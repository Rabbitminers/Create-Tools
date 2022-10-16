package com.rabbitminers.createtools.index;

import com.rabbitminers.createtools.CreateTools;
import com.rabbitminers.createtools.blocks.draftingtable.DraftingTableBlockRegistry;
import com.rabbitminers.createtools.blocks.testtable.TestTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CTBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, CreateTools.MODID);

    public static final RegistryObject<BlockEntityType<TestTableBlockEntity>> TEST_TABLE =
            BLOCK_ENTITIES.register("test_table_entity", () ->
                    BlockEntityType.Builder.of(TestTableBlockEntity::new,
                            DraftingTableBlockRegistry.TEST.get()).build(null));
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
