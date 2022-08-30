package com.rabbitminers.createtools.index;

import com.rabbitminers.createtools.CreateTools;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.contraptions.base.CasingBlock;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;

public class CPBlocks {
    private static final CreateRegistrate REGISTRATE = CreateTools.registrate().creativeModeTab(
            () -> CreateTools.itemGroup
    );


    public static final BlockEntry<CasingBlock> DYABLE_CASING = REGISTRATE.block("dyable_casing", CasingBlock::new)
            .properties(p -> p.color(MaterialColor.PODZOL))
            .transform(BuilderTransformers.casing(() -> CPSpriteShifts.DYEABLE_CASING))
            .register();

    public static void register() {}
}
