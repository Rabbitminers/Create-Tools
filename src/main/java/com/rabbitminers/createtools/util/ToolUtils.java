package com.rabbitminers.createtools.util;

import com.google.gson.Gson;
import com.rabbitminers.createtools.tooldata.CTComponents;
import com.rabbitminers.createtools.tooldata.CTGeneratorTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ToolUtils {

    public static boolean toolContainsComponent(ItemStack stack, CTComponents componentSearch) {
        CompoundTag nbt = stack.hasTag()
                ? stack.getTag()
                : new CompoundTag();

        if (nbt == null || !nbt.contains("components"))
            return false;

        for (int component : nbt.getIntArray("components")) {
            if (component == componentSearch.getId())
                return true;
        }
        return false;
    }

    public static CTGeneratorTypes getGeneratorType(ItemStack stack) {
        if (!stack.hasTag())
            return CTGeneratorTypes.NONE;

        CompoundTag nbt = stack.getTag();
        return CTGeneratorTypes.of(nbt.getInt("generator"));
    }


}
