package com.rabbitminers.createtools.toolsbase;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class CTTiers {
    public static final ForgeTier CREATETOOLMAX = new ForgeTier(2, 1400, 1.5f,
            2f, 22, BlockTags.NEEDS_IRON_TOOL,
            () -> Ingredient.of(Items.DIRT));
}
