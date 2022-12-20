package com.rabbitminers.createtools.blocks.testtable;

import com.rabbitminers.createtools.handler.GeneratorHandler;
import com.rabbitminers.createtools.tools.ToolBase;
import com.rabbitminers.createtools.tools.generators.Generator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class TestTableCameraHandler {
    private static final Map<UUID, TestTableCameraController> CONTROLLER_MAP = new HashMap<>();

    public static TestTableCameraController getControllerOfPlayerUUID(UUID uuid) {
        return CONTROLLER_MAP.get(uuid);
    }

    public static void update(UUID uuid, TestTableCameraController controller) {
        CONTROLLER_MAP.put(uuid, controller);
    }

    public static void remove(UUID uuid) {
        CONTROLLER_MAP.remove(uuid);
    }

    public static void clear() {
        CONTROLLER_MAP.clear();
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getPlayer();
        remove(player.getUUID());
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        TestTableCameraHandler.clear();
    }
}
