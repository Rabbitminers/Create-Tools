package com.rabbitminers.createtools.client;

import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DraftingTableInput extends Input {
    private final Options options;
    private static final float MOVING_SLOW_FACTOR = 0.3F;

    public DraftingTableInput(Options p_108580_) {
        this.options = p_108580_;
    }

    @Override
    public void tick(boolean x) {
        this.shiftKeyDown = this.options.keyShift.isDown();
    }
}
