package com.rabbitminers.createtools.render.custom;

import com.rabbitminers.createtools.CreateTools;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import net.minecraft.client.resources.model.BakedModel;

public abstract class CreateToolsCustomRenderedItemModel extends CustomRenderedItemModel {
    public CreateToolsCustomRenderedItemModel(BakedModel template, String basePath) {
        super(template, CreateTools.MODID, basePath);
    }
}
