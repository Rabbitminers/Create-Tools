package com.rabbitminers.createtools;

import com.mojang.logging.LogUtils;
import com.rabbitminers.createtools.armor.CTArmorItems;
import com.rabbitminers.createtools.blocks.draftingtable.DraftingTableBlockRegistry;
import com.rabbitminers.createtools.blocks.draftingtable.DraftingTableItemRegistry;
import com.rabbitminers.createtools.blocks.testtable.TestTableCameraHandler;
import com.rabbitminers.createtools.config.CTConfig;
import com.rabbitminers.createtools.events.*;
import com.rabbitminers.createtools.handler.KeybindHandler;
import com.rabbitminers.createtools.index.CPBlocks;
import com.rabbitminers.createtools.index.CTBlockEntities;
import com.rabbitminers.createtools.toolsbase.BaseTools;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CreateTools.MODID)
public class CreateTools {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "createtools";

    private static final NonNullSupplier<CreateRegistrate> registrate = CreateRegistrate.lazy(CreateTools.MODID);

    public CreateTools() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CTConfig.spec);

        CPBlocks.register();

        eventBus.addListener(this::loadComplete);

        DraftingTableItemRegistry.register(eventBus);
        DraftingTableBlockRegistry.register(eventBus);
        BaseTools.register(eventBus);
        CTArmorItems.register(eventBus);
        CTBlockEntities.register(eventBus);

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

        MinecraftForge.EVENT_BUS.register(new KeybindHandler());
        KeybindHandler.onClientSetup();
    }

    private void loadComplete(final FMLLoadCompleteEvent event) {
        MinecraftForge.EVENT_BUS.register(new ReplantCrops());
        MinecraftForge.EVENT_BUS.register(new ItemPickup());
        MinecraftForge.EVENT_BUS.register(new TreeChop());
        MinecraftForge.EVENT_BUS.register(new GUIEvent(Minecraft.getInstance()));
        MinecraftForge.EVENT_BUS.register(new TestTableCameraHandler());
        MinecraftForge.EVENT_BUS.register(new CompassOverlayEvent(Minecraft.getInstance()));
    }

    public static CreateRegistrate registrate() {
        return registrate.get();
    }
}
