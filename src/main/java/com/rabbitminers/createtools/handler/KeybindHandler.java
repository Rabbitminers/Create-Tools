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
    private static KeyMapping keyUseTool;
    private static KeyMapping keySwitchMode;

    private static boolean up = false;
    private static boolean down = false;
    private static boolean use = false;
    private static boolean mode = false;

    public static void onClientSetup() {
        keyExtendoBootsUp = new KeyMapping("keybind.createtools.up", GLFW.GLFW_KEY_V, CreateTools.MODID);
        keyExtendoBootsDown = new KeyMapping("keybind.createtools.down", GLFW.GLFW_KEY_G, CreateTools.MODID);
        keyUseTool = new KeyMapping("keybind.createtools.usetool", GLFW.GLFW_KEY_T, CreateTools.MODID);
        keySwitchMode = new KeyMapping("keybind.createtools.switchmode", GLFW.GLFW_KEY_DOWN, CreateTools.MODID);

        ClientRegistry.registerKeyBinding(keyExtendoBootsUp);
        ClientRegistry.registerKeyBinding(keyExtendoBootsDown);
        ClientRegistry.registerKeyBinding(keyUseTool);
        ClientRegistry.registerKeyBinding(keySwitchMode);
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
            boolean useNow = keyUseTool.isDown();
            boolean switchMode = keySwitchMode.isDown();

            if (upNow != up || downNow != down || useNow != use || switchMode != mode) {
                up = upNow;
                down = downNow;
                use = useNow;
                mode = switchMode;

                InputHandler.update(mc.player, upNow, downNow, useNow, mode);
            }
        }
    }
}
