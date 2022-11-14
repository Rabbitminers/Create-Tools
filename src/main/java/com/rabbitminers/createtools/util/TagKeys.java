package com.rabbitminers.createtools.util;

import com.rabbitminers.createtools.CreateTools;
import com.simibubi.create.Create;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class TagKeys {
    public static final TagKey<Block> DESTROY_CONNECTED = BlockTags.create(new ResourceLocation(CreateTools.MODID, "destroy_connected"));
    public static final TagKey<Block> IGNORE_CONNECTED = BlockTags.create(new ResourceLocation(CreateTools.MODID, "ignore_connected"));
}
