package com.rabbitminers.createtools.handler;

import com.rabbitminers.createtools.tools.ToolBase;
import com.rabbitminers.createtools.tools.generators.Generator;
import com.rabbitminers.createtools.util.InventoryUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public final class GeneratorHandler {
    private static final Map<UUID, Generator> GENERATOR_MAP = new HashMap<>();

    public static Generator getGeneratorOfUUID(UUID uuid) {
        return GENERATOR_MAP.get(uuid);
    }

    public static void update(UUID uuid, Generator generator) {
        GENERATOR_MAP.put(uuid, generator);
    }

    public static void remove(UUID uuid) {
        GENERATOR_MAP.remove(uuid);
    }

    public static void clear() {
        GENERATOR_MAP.clear();
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
        GeneratorHandler.clear();
    }
}
