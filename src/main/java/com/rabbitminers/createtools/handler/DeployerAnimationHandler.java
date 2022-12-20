package com.rabbitminers.createtools.handler;

import oshi.util.tuples.Pair;

import java.util.UUID;

public class DeployerAnimationHandler {
    private final UUID uuid;
    private final float MAX_EXTENSION = 0.6f;
    private int remainingFrames = 0;
    private float ZoffSet = 0;
    private boolean goingUp = false;
    public DeployerAnimationHandler(UUID uuid) {
        this.uuid = uuid;
    }
    public UUID getUuid() {
        return uuid;
    }

    public int getRemainingFrames() {
        return remainingFrames;
    }

    public void setRemainingFrames() {
        if (remainingFrames <= 0) {
            goingUp = true;
            this.remainingFrames = 160;
        }
    }

    public float getZoffSet() {
        return ZoffSet;
    }

    public void renderTick() {
        if (remainingFrames <= 0)
            return;

        float step = (float) 0.075/10.0f;

        if (ZoffSet > MAX_EXTENSION && goingUp) {
            ZoffSet -= step;
            goingUp = false;
        } else if (ZoffSet < 0.2f/16f && !goingUp) {
            ZoffSet += step;
            goingUp = true;
        } else
            ZoffSet+=(goingUp ? + step : -step);


        remainingFrames--;
    }
}
