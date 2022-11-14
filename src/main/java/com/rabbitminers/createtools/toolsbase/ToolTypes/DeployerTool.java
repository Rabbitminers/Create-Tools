package com.rabbitminers.createtools.toolsbase.ToolTypes;

import com.rabbitminers.createtools.handler.InputHandler;
import com.rabbitminers.createtools.index.CPTileEntities;
import com.rabbitminers.createtools.tools.ToolBase;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import org.apache.commons.lang3.SerializationUtils;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Objects;

public class DeployerTool extends ToolBase {

    public BlockState heldBlock;
    public DeployerTool(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack p_41398_, Player p_41399_, LivingEntity p_41400_, InteractionHand p_41401_) {
        return super.interactLivingEntity(p_41398_, p_41399_, p_41400_, p_41401_);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);

        CompoundTag nbt = stack.hasTag()
                ? stack.getTag()
                : new CompoundTag();

        if (nbt != null && entity instanceof Player player && InputHandler.isSwitchUsed(player) && !player.getCooldowns().isOnCooldown(this)) {
            String currentMode = nbt.contains("deployer_mode") ? nbt.getString("deployer_mode") : "none";

            String newMode =  switch (currentMode) {
                case "dolley" -> "attack";
                case "attack" -> "place";
                default -> "dolley";
            };

            player.getCooldowns().addCooldown(this, 20);

            player.displayClientMessage(new TextComponent("Mode: " + newMode), true);
            nbt.putString("deployer_mode", newMode);

            stack.setTag(nbt);
        }
    }

    public void placeModeUseOn(UseOnContext useOnContext) {

    }

    public void dolleyModeUseOn(UseOnContext useOnContext) {
        ItemStack stack = useOnContext.getItemInHand();
        CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();

        if (nbt != null && nbt.contains("Name")) {

        }
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        ItemStack stack = useOnContext.getItemInHand();
        CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();

        if (nbt != null && nbt.contains("deployer_mode")) {
            if (nbt.getString("deployer_mode").equals("place"))
                placeModeUseOn(useOnContext);

            if (nbt.getString("deployer_mode").equals("dolley"))
                dolleyModeUseOn(useOnContext);
        }

        return super.useOn(useOnContext);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> components, TooltipFlag p_41424_) {
        super.appendHoverText(stack, p_41422_, components, p_41424_);

        CompoundTag nbt = stack.hasTag()
                ? stack.getTag()
                : new CompoundTag();

        if (nbt == null)
            return;

        components.add(new TextComponent("Deployer Mode" +
                (nbt.contains("deployer_mode") ? nbt.getString("deployer_mode") : "None")));
    }
}
