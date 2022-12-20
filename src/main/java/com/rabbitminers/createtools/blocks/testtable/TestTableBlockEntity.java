package com.rabbitminers.createtools.blocks.testtable;

import com.rabbitminers.createtools.index.CTBlockEntities;
import com.rabbitminers.createtools.tooldata.CTToolTypes;
import com.rabbitminers.createtools.util.ItemDisplayBlockEntity;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class TestTableBlockEntity extends ItemDisplayBlockEntity implements IHaveDraftingTableInformation {
    public final Direction direction;
    public TestTableBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CTBlockEntities.TEST_TABLE.get(), blockPos, blockState);
        this.direction = blockState.getValue(TestTableBlock.FACING);
    }
    TestTableCameraController controller = new TestTableCameraController(this.worldPosition);

    public TestTableCameraController getController() {
        return controller;
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

    @SubscribeEvent
    public void onKeyInputEvent(TickEvent.WorldTickEvent event) {
        System.out.println("a");
        controller.getInputHandler().tick(true);

        if (controller.getInputHandler().shiftKeyDown)
            controller.disable();
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("Hello world");
    }
}
