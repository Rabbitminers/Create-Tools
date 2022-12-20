package com.rabbitminers.createtools.render.tools.model;

import com.rabbitminers.createtools.render.custom.CreateToolsCustomRenderedItemModel;
import com.simibubi.create.content.curiosities.tools.ExtendoGripModel;
import com.simibubi.create.content.curiosities.weapons.PotatoCannonItemRenderer;
import com.simibubi.create.content.curiosities.weapons.PotatoCannonModel;
import net.minecraft.client.resources.model.BakedModel;

public class DrillToolModel extends CreateToolsCustomRenderedItemModel {
    public DrillToolModel(BakedModel template) {
        super(template, "drill");
        addPartials(
            "handle",
            "cog",
            "head",
            "flywheel",
            "blaze",
            "cage",
            "spyglass",
            "electron_tube",
            "steam_engine",
            "windmill",
            "encased_fan",
            "fan_blades",
            "rsc"
        );
    }
}