package com.rabbitminers.createtools.armor.extendoboots;

import com.rabbitminers.createtools.handler.InputHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExtendoBootsItem extends ArmorItem {

    int timeOut = 0;
    public ExtendoBootsItem(ArmorMaterial p_40386_, EquipmentSlot p_40387_, Properties p_40388_) {
        super(p_40386_, p_40387_, p_40388_);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        CompoundTag nbt;
        nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();

        int extension = 0;
        if (nbt.contains("extension")) {
            extension = nbt.getInt("extension");
        } else {
            nbt.putInt("extension", extension);
        }

        if (timeOut == 0) {
            if (InputHandler.isHoldingUp(player)) {
                nbt.putInt("extension", extension+1);
                player.setDeltaMovement(0, -1, 0);
                timeOut = 100;
            } else if (InputHandler.isHoldingDown(player)) {
                nbt.putInt("extension", extension-1);
                player.setDeltaMovement(0, 1 ,0);
                timeOut = 100;
            }
        }
        if (timeOut > 0)
            timeOut--;
        super.onArmorTick(stack, level, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(new TextComponent(String.valueOf(stack.getTag())));

        super.appendHoverText(stack, level, components, flag);
    }
}
