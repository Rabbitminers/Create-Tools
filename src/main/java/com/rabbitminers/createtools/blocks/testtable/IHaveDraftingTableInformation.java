package com.rabbitminers.createtools.blocks.testtable;

import java.util.List;
import java.util.Optional;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IHaveDraftingTableInformation {
    /**
     * Use Lang.[...].forGoggles(list)
     */
    String spacing = "    ";

    /**
     * Use Lang.[...].forGoggles(list)
     */
    @Deprecated
    Component componentSpacing = Components.literal(spacing);

    /**
     * this method will be called when looking at a TileEntity that implemented this
     * interface
     *
     * @return {@code true} if the tooltip creation was successful and should be
     *         displayed, or {@code false} if the overlay should not be displayed
     */
    default boolean addToDraftingTableTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return false;
    }

}
