package com.rabbitminers.createtools.handler;

import com.rabbitminers.createtools.tools.ToolBase;
import com.rabbitminers.createtools.tools.generators.Generator;
import com.simibubi.create.AllKeys;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.animation.PhysicalFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.system.CallbackI;

import java.util.*;

public final class DrillHeadRotationHandler {
    private static final Map<UUID, Float> DRILL_HEAD_ROTATION_MAP = new HashMap<>();

    public static Float getRotationOfUUID(UUID uuid) {
        return DRILL_HEAD_ROTATION_MAP.get(uuid);
    }

    public static void update(UUID uuid) {
        Float currentRotation = getRotationOfUUID(uuid);
        Generator generator = GeneratorHandler.getGeneratorOfUUID(uuid);

        if (currentRotation != null && generator != null) {
            if (generator.getRPM() != 0) {
                float step = (360f / (20f * 60f) * (generator.getRPM() / 2.0f));
                float rotation = (currentRotation + step) < 360f ? currentRotation + step : currentRotation + step - 360f;
                DRILL_HEAD_ROTATION_MAP.put(uuid, rotation);
            } else {
                DRILL_HEAD_ROTATION_MAP.put(uuid, currentRotation);
            }
        } else {
            DRILL_HEAD_ROTATION_MAP.put(uuid, 0.0f);
        }
    }
    public static void remove(UUID uuid) {
        DRILL_HEAD_ROTATION_MAP.remove(uuid);
    }

    public static void clear() {
        DRILL_HEAD_ROTATION_MAP.clear();
    }

    public void clearAllHeldTools(Player player) {
        List<UUID> toolsToRemove = new ArrayList<UUID>();

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack currentStack = player.getInventory().getItem(i);
            if (!currentStack.isEmpty() && currentStack.getItem() instanceof ToolBase) {
                CompoundTag nbt = currentStack.hasTag() ? currentStack.getTag() : new CompoundTag();
                if (nbt.hasUUID("toolid")) {
                    toolsToRemove.add(nbt.getUUID("toolid"));
                }
            }
        }

        for (UUID tool : toolsToRemove)
            remove(tool);

    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getPlayer();
        clearAllHeldTools(player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getPlayer();
        clearAllHeldTools(player);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        DrillHeadRotationHandler.clear();
    }

}
