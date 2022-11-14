package com.rabbitminers.createtools.util;

import com.rabbitminers.createtools.CreateTools;
import com.rabbitminers.createtools.blocks.draftingtable.DraftingTableBlockRegistry;
import com.rabbitminers.createtools.blocks.testtable.TestTableBlockRenderer;
import com.rabbitminers.createtools.blocks.testtable.TestTableOverlayRenderer;
import com.rabbitminers.createtools.client.DraftingTableHudOverlay;
import com.rabbitminers.createtools.index.CTBlockEntities;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CreateTools.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistry {
    private ClientRegistry() {}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(
                DraftingTableBlockRegistry.TEST.get(),
                RenderType.cutout() // Switched from transparent to fix rendering issues
        );

        registerOverlays();
    }

    private static void registerOverlays() {
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Drafting Table Display Information", TestTableOverlayRenderer.OVERLAY);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CTBlockEntities.TEST_TABLE.get(), TestTableBlockRenderer::new);
    }
}
