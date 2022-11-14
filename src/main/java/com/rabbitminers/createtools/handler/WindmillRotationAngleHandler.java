package com.rabbitminers.createtools.handler;

import com.rabbitminers.createtools.tools.ToolBase;
import com.rabbitminers.createtools.tools.generators.Generator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public final class WindmillRotationAngleHandler {

    private static final Map<UUID, Float> ROTATION_ANGLE_HANDLER_MAP = new HashMap<>();

    public static Float getRotationOfUUID(UUID uuid) {
        return ROTATION_ANGLE_HANDLER_MAP.get(uuid);
    }

    public static void update(UUID uuid) {
        Float currentRotation = getRotationOfUUID(uuid);
        Generator generator = GeneratorHandler.getGeneratorOfUUID(uuid);

        if (currentRotation != null && generator != null) {
            if (generator.getRPM() != 0) {
                float step = (360f / (20f * 60f) * (generator.getRPM() / 1.5f));
                float rotation = (currentRotation + step) < 360f ? currentRotation + step : currentRotation + step - 360f;
                ROTATION_ANGLE_HANDLER_MAP.put(uuid, rotation);
            } else {
                ROTATION_ANGLE_HANDLER_MAP.put(uuid, currentRotation);
            }
        } else {
            ROTATION_ANGLE_HANDLER_MAP.put(uuid, 0.0f);
        }
    }

    public static void remove(UUID uuid) {
        ROTATION_ANGLE_HANDLER_MAP.remove(uuid);
    }

    public static void clear() {
        ROTATION_ANGLE_HANDLER_MAP.clear();
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
        WindmillRotationAngleHandler.clear();
    }
}
