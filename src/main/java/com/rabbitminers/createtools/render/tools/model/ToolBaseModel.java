package com.rabbitminers.createtools.render.tools.model;

import com.rabbitminers.createtools.CreateTools;
import com.rabbitminers.createtools.render.custom.CreateToolsCustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CreateCustomRenderedItemModel;
import net.minecraft.client.resources.model.BakedModel;

public class ToolBaseModel extends CreateToolsCustomRenderedItemModel {
    public ToolBaseModel(BakedModel template) {
        super(template, "drill");
        addPartials("cog", "head");
    }

}
