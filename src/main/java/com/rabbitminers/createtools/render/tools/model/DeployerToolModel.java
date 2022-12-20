package com.rabbitminers.createtools.render.tools.model;

import com.rabbitminers.createtools.render.custom.CreateToolsCustomRenderedItemModel;
import net.minecraft.client.resources.model.BakedModel;

public class DeployerToolModel extends CreateToolsCustomRenderedItemModel {

    public DeployerToolModel(BakedModel template) {
        super(template, "deployer");
        addPartials(
            "handle", "cog", "arm", "dolly", "place", "attack"
        );
    }
}
