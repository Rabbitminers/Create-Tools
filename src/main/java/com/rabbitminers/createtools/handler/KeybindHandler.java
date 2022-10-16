package com.rabbitminers.createtools.handler;

import com.rabbitminers.createtools.CreateTools;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
    private static KeyMapping keyExtendoBootsUp;
    private static KeyMapping keyExtendoBootsDown;

    private static boolean up = false;
    private static boolean down = false;

    public static void onClientSetup() {
        keyExtendoBootsUp = new KeyMapping("keybind.createtools.up", GLFW.GLFW_KEY_V, CreateTools.MODID);
        keyExtendoBootsDown = new KeyMapping("keybind.createtools.down", GLFW.GLFW_KEY_G, CreateTools.MODID);

        ClientRegistry.registerKeyBinding(keyExtendoBootsUp);
        ClientRegistry.registerKeyBinding(keyExtendoBootsDown);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            var mc = Minecraft.getInstance();
            var settings = mc.options;

            if (mc.getConnection() == null)
                return;

            boolean upNow = keyExtendoBootsUp.isDown();
            boolean downNow = keyExtendoBootsDown.isUnbound()
                    ? settings.keyShift.isDown()
                    : keyExtendoBootsDown.isDown();


            if (upNow != up || downNow != down) {
                up = upNow;
                down = downNow;

                InputHandler.update(mc.player, upNow, downNow);
            }
        }
    }
}
