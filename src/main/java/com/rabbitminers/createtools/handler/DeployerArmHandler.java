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

public final class DeployerArmHandler {
    private static final Map<UUID, DeployerAnimationHandler> ANIMATION_HANDLER_MAP = new HashMap<>();

    public static DeployerAnimationHandler getAnimationHandlerOfUUID(UUID uuid) {
        return ANIMATION_HANDLER_MAP.get(uuid);
    }

    public static void add(UUID uuid) {
        ANIMATION_HANDLER_MAP.put(uuid, new DeployerAnimationHandler(uuid));
    }

    public static boolean deployerHasAnimationHandler(UUID uuid) {
        return ANIMATION_HANDLER_MAP.containsKey(uuid);
    }

    public static void remove(UUID uuid) {
        ANIMATION_HANDLER_MAP.remove(uuid);
    }

    public static void clear() {
        ANIMATION_HANDLER_MAP.clear();
    }


    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getPlayer();

    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getPlayer();
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        com.rabbitminers.createtools.handler.WindmillRotationAngleHandler.clear();
    }

}
