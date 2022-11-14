package com.rabbitminers.createtools.tools.generators;

import com.rabbitminers.createtools.tooldata.CTGeneratorTypes;
import com.rabbitminers.createtools.tools.data.display.WindmillOutputData;
import com.rabbitminers.createtools.toolsbase.generators.WindmillTool;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

public class Windmill extends Generator {
    public Windmill() {
        super(CTGeneratorTypes.WINDMILL.getMaxSU());
    }

    static double logCal(double n, double b) {
        double c = 0;
        while(n > 1) {
            n = n / b;
            c++;
        }
        c = c + n;
        return c;
    }

    @Override
    public void inventoryTick(ItemStack stack, Entity entity) {
        double yHeight = entity.getY() + 65.0f;
        int y = (yHeight > 377.0f) ? 377 : (int) Math.floor(yHeight);

        SUout = (int) Math.floor((yHeight/377)*maxSU);
        rpm = SUout > 0 ? SUout/10.0f : 0;
    }
    @Override
    public List<Component> appendDisplayText(List<Component> components) {
        components.add(new TextComponent("Stress Output: ")
                .withStyle(ChatFormatting.DARK_GRAY)
                .append(new TextComponent("[" + SUout + "]")
                .withStyle(WindmillOutputData.of(SUout).getTextColor())));

        components.add(new TextComponent("RPM: " + rpm));
        return components;
    }
}
