package com.rabbitminers.createtools.tools.generators;

import com.rabbitminers.createtools.tooldata.CTGeneratorTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

public class SteamEngine extends Generator {

    public SteamEngine() {
        super(CTGeneratorTypes.STEAM_ENGINE.getMaxSU());
    }

    @Override
    public void inventoryTick(ItemStack stack, Entity entity) {
        super.inventoryTick(stack, entity);
    }
    @Override
    public void useTick(UseOnContext useOnContext) {
        super.useTick(useOnContext);
    }
    @Override
    public List<Component> appendDisplayText(List<Component> components) {
        return super.appendDisplayText(components);
    }
}
