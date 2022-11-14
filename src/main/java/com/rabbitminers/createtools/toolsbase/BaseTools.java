package com.rabbitminers.createtools.toolsbase;

import com.rabbitminers.createtools.CreateTools;
import com.rabbitminers.createtools.tooldata.CTToolTypes;
import com.rabbitminers.createtools.tools.ToolBase;
import com.rabbitminers.createtools.tools.tooltypes.base.DrillTool;
import com.rabbitminers.createtools.tools.tooltypes.base.PloughTool;
import com.rabbitminers.createtools.toolsbase.ToolTypes.DeployerPlaceTool;
import com.rabbitminers.createtools.toolsbase.ToolTypes.DeployerTool;
import com.rabbitminers.createtools.toolsbase.ToolTypes.HarvesterTool;
import com.rabbitminers.createtools.toolsbase.ToolTypes.SawTool;
import com.rabbitminers.createtools.toolsbase.generators.FurnaceEngineTool;
import com.rabbitminers.createtools.toolsbase.Windmill.WindmillDrill;
import net.minecraft.tags.BlockTags;
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

    public static final RegistryObject<Item> DEPLOYER_PLACE_ITEM = BASETOOLS.register("deployer_place_item",
            () -> new DeployerPlaceTool(new Item.Properties().tab(CreateTools.itemGroup)));

    public static final RegistryObject<Item> WINDMILL_DRILL = BASETOOLS.register("windmill_drill",
            () -> new WindmillDrill(CTTiers.CREATETOOLMAX, 2, 3f,
                    new Item.Properties().tab(CreateTools.itemGroup)));
    public static final RegistryObject<Item> FURNACE_ENGINE_DRILL = BASETOOLS.register("furnace_engine_drill",
            () -> new FurnaceEngineTool(CTTiers.CREATETOOLMAX, 2, 3f,
                    new Item.Properties().tab(CreateTools.itemGroup)));

    public static final RegistryObject<Item> HAND_CRANK_TOOL = BASETOOLS.register("hand_crank_tool",
            () -> new HandCrankTool(CTTiers.CREATETOOLMAX, 2, 3f,
                    new Item.Properties().tab(CreateTools.itemGroup)));

    public static final RegistryObject<Item> SAW_TOOLL = BASETOOLS.register("saw_tool",
            () -> new SawTool(CTTiers.CREATETOOLMAX, 2, 3f,
                    new Item.Properties().tab(CreateTools.itemGroup)));

    public static final RegistryObject<Item> HARVESTER_TOOL = BASETOOLS.register("harvester_tool",
            () -> new HarvesterTool(new Item.Properties().tab(CreateTools.itemGroup)));

    public static final RegistryObject<Item> FE_TEST_ITEM = BASETOOLS.register("furnace_engine",
            () -> new ToolBase(new Item.Properties().tab(CreateTools.itemGroup).stacksTo(1)));

    public static final RegistryObject<Item> DRILL_TOOL = BASETOOLS.register("drill_tool",
            () -> new DrillTool(new Item.Properties()
                    .tab(CreateTools.itemGroup)
                    .stacksTo(1), BlockTags.MINEABLE_WITH_PICKAXE, 3.0f));

    public static final RegistryObject<Item> PLOUGH_TOOL = BASETOOLS.register("plough_tool",
            () -> new PloughTool(new Item.Properties()
                    .tab(CreateTools.itemGroup)
                    .stacksTo(1), BlockTags.MINEABLE_WITH_HOE, 3.0f));




    public static void register(IEventBus eventBus) {
        BASETOOLS.register(eventBus);
    }
}
