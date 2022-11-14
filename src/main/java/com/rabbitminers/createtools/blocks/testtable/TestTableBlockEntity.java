package com.rabbitminers.createtools.blocks.testtable;

import com.rabbitminers.createtools.index.CTBlockEntities;
import com.rabbitminers.createtools.tooldata.CTToolTypes;
import com.rabbitminers.createtools.util.ItemDisplayBlockEntity;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TestTableBlockEntity extends ItemDisplayBlockEntity implements IHaveDraftingTableInformation
{
    public TestTableBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(CTBlockEntities.TEST_TABLE.get(), p_155229_, p_155230_);
    }

    @Override
    public boolean addToDraftingTableTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        ItemStack stack = this.getDisplayedItem();
        CTToolTypes baseToolTypeItem = CTToolTypes.of(stack.getItem());

        if (baseToolTypeItem != null) {
            tooltip.add(new TextComponent("Creating New " + baseToolTypeItem.getName()).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.BOLD));
            tooltip.add(new TextComponent("Valid Components:").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(new TextComponent(""));
            tooltip.add(new TextComponent(""));
        }

        Component indent = Components.literal(IHaveDraftingTableInformation.spacing);
        Component indent2 = Components.literal(IHaveDraftingTableInformation.spacing + " ");

        return true;
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("Hello world");
    }
}
