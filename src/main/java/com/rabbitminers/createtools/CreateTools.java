package com.rabbitminers.createtools;

import com.mojang.logging.LogUtils;
import com.rabbitminers.createtools.blocks.draftingtable.DraftingTableBlockRegistry;
import com.rabbitminers.createtools.blocks.draftingtable.DraftingTableItemRegistry;
import com.rabbitminers.createtools.index.CPBlocks;
import com.rabbitminers.createtools.toolsbase.BaseTools;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CreateTools.MODID)
public class CreateTools {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "createtools";

    private static final NonNullSupplier<CreateRegistrate> registrate = CreateRegistrate.lazy(CreateTools.MODID);

    public CreateTools() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CPBlocks.register();
        DraftingTableItemRegistry.register(eventBus);
        DraftingTableBlockRegistry.register(eventBus);
        BaseTools.register(eventBus);

        eventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final CreativeModeTab itemGroup = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AllBlocks.ANDESITE_CASING.get());
        }
    };

    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(
                DraftingTableBlockRegistry.DRAFTING_TABLE.get(),
                RenderType.cutout() // Switched from transparent to fix rendering issues
        );
    }

    private void setup(final FMLCommonSetupEvent event) {}

    public static CreateRegistrate registrate() {
        return registrate.get();
    }


}
