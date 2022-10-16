package com.rabbitminers.createtools.toolsbase;

import com.rabbitminers.createtools.CreateTools;
import com.rabbitminers.createtools.toolsbase.ToolTypes.DeployerTool;
import com.rabbitminers.createtools.toolsbase.generators.FurnaceEngineTool;
import com.rabbitminers.createtools.toolsbase.Windmill.WindmillDrill;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BaseTools {
    public static final DeferredRegister<Item> BASETOOLS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CreateTools.MODID);

    public static final RegistryObject<Item> DRILL = BASETOOLS.register("drill",
            () -> new CTDrill(CTTiers.CREATETOOLMAX, 2, 3f,
                    new Item.Properties().tab(CreateTools.itemGroup)));

    public static final RegistryObject<Item> DEPLOYER_ITEM = BASETOOLS.register("deployer_item",
            () -> new DeployerTool(new Item.Properties().tab(CreateTools.itemGroup)));

    public static final RegistryObject<Item> WINDMILL_DRILL = BASETOOLS.register("windmill_drill",
            () -> new WindmillDrill(CTTiers.CREATETOOLMAX, 2, 3f,
                    new Item.Properties().tab(CreateTools.itemGroup)));

    public static final RegistryObject<Item> STEAM_ENGINE_DRILL = BASETOOLS.register("steam_engine_drill",
            () -> new FurnaceEngineTool(CTTiers.CREATETOOLMAX, 2, 3f,
                    new Item.Properties().tab(CreateTools.itemGroup)));

    public static final RegistryObject<Item> HAND_CRANK_TOOL = BASETOOLS.register("hand_crank_tool",
            () -> new HandCrankTool(CTTiers.CREATETOOLMAX, 2, 3f,
                    new Item.Properties().tab(CreateTools.itemGroup)));
    public static void register(IEventBus eventBus) {
        BASETOOLS.register(eventBus);
    }
}
