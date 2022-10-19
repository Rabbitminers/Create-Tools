package com.rabbitminers.createtools.handler;


import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public final class InputHandler {
    private static final Map<Player, Boolean> HOLDING_UP = new HashMap<>();
    private static final Map<Player, Boolean> HOLDING_DOWN = new HashMap<>();

    private static final Map<Player, Boolean> USE_TOOL = new HashMap<>();

    public static boolean isHoldingUp(Player player) {
        return HOLDING_UP.containsKey(player) && HOLDING_UP.get(player);
    }
    public static boolean isHoldingDown(Player player) {
        return HOLDING_DOWN.containsKey(player) && HOLDING_DOWN.get(player);
    }
    public static boolean isToolUsed(Player player) {
        return USE_TOOL.containsKey(player) && USE_TOOL.get(player);
    }
    public static void update(Player player, boolean up, boolean down, boolean use) {
        HOLDING_UP.put(player, up);
        HOLDING_DOWN.put(player, down);
        USE_TOOL.put(player, use);
    }

    public static void remove(Player player) {
        HOLDING_UP.remove(player);
        HOLDING_DOWN.remove(player);
        USE_TOOL.remove(player);
    }

    public static void clear() {
        HOLDING_UP.clear();
        HOLDING_DOWN.clear();
        USE_TOOL.clear();
    }


    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        remove(event.getPlayer());
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        remove(event.getPlayer());
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        InputHandler.clear();
    }
}
