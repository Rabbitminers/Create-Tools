package com.rabbitminers.createtools.tools.generators;

import com.rabbitminers.createtools.tooldata.CTGeneratorTypes;
import com.rabbitminers.createtools.util.ToolUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

import static com.rabbitminers.createtools.util.InventoryUtil.isFlywheelPresent;

public class SteamEngine extends Generator {

    public int coalTicks;
    public int waterMb;
    private final int waterMbConsumptionPerTick = 2;
    public double remainingWaterTicks;
    public double remainingFuelTicks;

    public SteamEngine() {
        super(CTGeneratorTypes.STEAM_ENGINE.getMaxSU());
    }

    @Override
    public void inventoryTick(ItemStack stack, Entity entity) {
        if (this.remainingFuelTicks > 0 && this.remainingWaterTicks > 0) {
            this.remainingWaterTicks = isFlywheelPresent(stack)
                ? remainingWaterTicks - waterMbConsumptionPerTick*0.75
                : remainingWaterTicks - waterMbConsumptionPerTick;
        }

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
