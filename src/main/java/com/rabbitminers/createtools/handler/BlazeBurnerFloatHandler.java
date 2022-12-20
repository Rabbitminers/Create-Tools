package com.rabbitminers.createtools.handler;

import com.rabbitminers.createtools.tools.ToolBase;
import com.rabbitminers.createtools.tools.generators.Generator;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import oshi.util.tuples.Pair;

import java.util.*;

public final class BlazeBurnerFloatHandler {

    // UUID, (height, goingUp)
    private static final Map<UUID, Pair<Float, Boolean>> UUID_FLOAT_MAP = new HashMap<>();
    public static Float getHeightOfUUID(UUID uuid) {
        return UUID_FLOAT_MAP.get(uuid).getA();
    }

    public static void update(UUID uuid) {
        Pair<Float, Boolean> data = UUID_FLOAT_MAP.get(uuid);
        Generator generator = GeneratorHandler.getGeneratorOfUUID(uuid);

        if (data != null && generator != null) {
            float currentHeight = data.getA();

            float step = (float) 0.00625/10.0f;
            boolean goingUp = UUID_FLOAT_MAP.get(uuid).getB();

            if (generator.getRPM() != 0) {
                if (currentHeight > 2.5f/16f && goingUp) UUID_FLOAT_MAP.put(uuid,
                    new Pair<>(currentHeight-step, false));
                else if (currentHeight < 0.2f/16f && !goingUp) UUID_FLOAT_MAP.put(uuid,
                    new Pair<>(currentHeight+step, true));
                else UUID_FLOAT_MAP.put(uuid,
                    new Pair<>(currentHeight+(goingUp ? +step : -step), goingUp));
            } else
                UUID_FLOAT_MAP.put(uuid, new Pair<Float, Boolean>(currentHeight, false));
        } else
            UUID_FLOAT_MAP.put(uuid, new Pair<Float, Boolean>(0.0f, false));
    }
    public static void remove(UUID uuid) {
        UUID_FLOAT_MAP.remove(uuid);
    }

    public static void clear() {
        UUID_FLOAT_MAP.clear();
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
        BlazeBurnerFloatHandler.clear();
    }
}
