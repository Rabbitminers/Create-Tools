package com.rabbitminers.createtools.armor.extendoboots;

import com.rabbitminers.createtools.handler.InputHandler;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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

        Vec3 startPos = extension == 0
                ? new Vec3(player.getX(), player.getY(), player.getZ())
                : null;

        // player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 0, 10));

        if (nbt.getInt("extension") != 0) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 0, -1));
            player.setDeltaMovement(0, 0, 0);

            if (startPos != null)
                player.setPos(new Vec3(startPos.x, startPos.y+extension, startPos.z));
        }

        if (timeOut == 0) {
            if (InputHandler.isHoldingUp(player) && extension < 10) {
                nbt.putInt("extension", extension+1);
                player.setDeltaMovement(0, 1, 0);
                timeOut = 20;
            } else if (InputHandler.isHoldingDown(player) && extension > 0) {
                nbt.putInt("extension", extension-1);
                player.setDeltaMovement(0, -1 ,0);
                timeOut = 20;
            }
        }

        if (player.isCrouching())
            nbt.putInt("extension", 0);


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
