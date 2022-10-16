package com.rabbitminers.createtools.armor.extendoboots;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ExtendoBootsUtil {
    public static Boolean isBootsActive(Player player) {
        var stack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (stack.isEmpty())
            return false;
        return true;
    }
}
