package com.rabbitminers.createtools;

import com.mojang.logging.LogUtils;
import com.rabbitminers.createtools.index.CPBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CreateTools.MODID)
public class CreateTools {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "createtools";

    private static final NonNullSupplier<CreateRegistrate> registrate = CreateRegistrate.lazy(CreateTools.MODID);

    public CreateTools() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CPBlocks.register();
    }

    public static final CreativeModeTab itemGroup = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AllBlocks.ANDESITE_CASING.get());
        }
    };

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    public static CreateRegistrate registrate() {
        return registrate.get();
    }


}
