package com.rabbitminers.createtools.toolsbase;

import com.rabbitminers.createtools.CreateTools;
import com.rabbitminers.createtools.tooldata.CTToolTypes;
import com.rabbitminers.createtools.tools.ToolBase;
import com.rabbitminers.createtools.tools.tooltypes.base.*;
import com.rabbitminers.createtools.toolsbase.ToolTypes.DeployerPlaceTool;
import com.rabbitminers.createtools.toolsbase.ToolTypes.DeployerTool;
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
    public static final RegistryObject<Item> DRILL_TOOL = BASETOOLS.register("drill_tool",
            () -> new DrillTool(new Item.Properties()
                    .tab(CreateTools.itemGroup)
                    .stacksTo(1), BlockTags.MINEABLE_WITH_PICKAXE, 3.0f));

    public static final RegistryObject<Item> SAW_TOOL = BASETOOLS.register("saw_tool",
            () -> new SawTool(new Item.Properties()
                    .tab(CreateTools.itemGroup)
                    .stacksTo(1)));

    public static final RegistryObject<Item> HARVESTER_TOOL = BASETOOLS.register("harvester_tool",
            () -> new HarvesterTool(new Item.Properties()
                    .tab(CreateTools.itemGroup)
                    .stacksTo(1), BlockTags.MINEABLE_WITH_HOE, 3.0f));

    public static final RegistryObject<Item> PLOUGH_TOOL = BASETOOLS.register("plough_tool",
            () -> new PloughTool(new Item.Properties()
                    .tab(CreateTools.itemGroup)
                    .stacksTo(1), BlockTags.MINEABLE_WITH_HOE, 3.0f));

    public static final RegistryObject<Item> DEPLOYER_TOOL = BASETOOLS.register("deployer_tool",
            () -> new com.rabbitminers.createtools.tools.tooltypes.base.DeployerTool(new Item.Properties()
                    .tab(CreateTools.itemGroup)
                    .stacksTo(1)));

    public static final RegistryObject<Item> POTATO_CANNON_TOOL = BASETOOLS.register("potato_cannon_item",
            () -> new PotatoCannonTool(new Item.Properties()
                    .tab(CreateTools.itemGroup)
                    .stacksTo(1)));
    public static void register(IEventBus eventBus) {
        BASETOOLS.register(eventBus);
    }
}
