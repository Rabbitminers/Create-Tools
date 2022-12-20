package com.rabbitminers.createtools.blocks.testtable;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.rabbitminers.createtools.client.DraftingTableInput;
import com.rabbitminers.createtools.handler.InputHandler;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TestTableCameraController {
    private final BlockPos position;
    private Direction direction;
    TestTableCameraController(BlockPos pos) {
        this.position = pos;
    }
    private final Minecraft mc = Minecraft.getInstance();
    private final Quaternion rotation = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
    private final Vector3f forwards = new Vector3f(0.0F, 0.0F, 1.0F);
    private final Vector3f up = new Vector3f(0.0F, 1.0F, 0.0F);
    private final Vector3f left = new Vector3f(1.0F, 0.0F, 0.0F);
    private boolean active;
    private CameraType oldCameraType;
    private Input tableInput = new Input();
    private Input oldInput;
    private double x, y, z;
    private float yRot, xRot;
    private double forwardVelocity;
    private double leftVelocity;
    private double upVelocity;
    private long lastTime;
    private boolean insideRenderDebug;
    private BlockPos pos;

    public boolean isActive() {
        return active;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getXRot() {
        return xRot;
    }

    public float getYRot() {
        return yRot;
    }

    public BlockPos getPos() {
        return position;
    }

    public Input getInputHandler() {
        return tableInput;
    }

    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    public Direction getDirection() {
        return direction;
    }

    public void toggle() {
        if (active) {
            disable();
        } else {
            enable();
        }
    }

    private void calculateVectors() {
        rotation.set(0.0F, 0.0F, 0.0F, 1.0F);
        rotation.mul(Vector3f.YP.rotationDegrees(-yRot));
        rotation.mul(Vector3f.XP.rotationDegrees(xRot));
        forwards.set(0.0F, 0.0F, 1.0F);
        forwards.transform(rotation);
        up.set(0.0F, 1.0F, 0.0F);
        up.transform(rotation);
        left.set(1.0F, 0.0F, 0.0F);
        left.transform(rotation);
    }
    public void enable() {
        if (mc.player == null)
            return;

        if (!active && mc.player.input != tableInput) {
            active = true;
            oldCameraType = mc.options.getCameraType();
            oldInput = mc.player.input;
            System.out.println(oldInput);
            // mc.player.input = tableInput;
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            if (oldCameraType.isFirstPerson() != mc.options.getCameraType().isFirstPerson()) {
                mc.gameRenderer.checkEntityPostEffect(mc.options.getCameraType().isFirstPerson() ? mc.getCameraEntity() : null);
            }

            y = position.getY() + 2;

            x = position.getX() + 0.1;
            z = position.getZ() + 0.1;

            switch (direction != null ? direction : Direction.UP) {
                case NORTH -> {
                    x = position.getX() + 1;
                    z = position.getZ() + 1;
                    System.out.println("NORTH");
                }

                case SOUTH -> {
                    x = position.getX() + 1;
                    z = position.getZ() + 1;
                    System.out.println("SOUTH");
                }

                case EAST -> {
                    x = position.getX() - 0.1;
                    z = position.getZ() - 0.1;
                }
                case WEST -> {
                    x = position.getX() - 0.1;
                    z = position.getZ() - 0.1;
                }

                default -> {
                    return;
                }
            }


            forwardVelocity = 0;
            leftVelocity = 0;
            upVelocity = 0;
            lastTime = 0;
        }
    }

    public void disable() {
        if (active) {
            active = false;
            CameraType cameraType = mc.options.getCameraType();
            System.out.println(mc.player.input);
            mc.options.setCameraType(oldCameraType);
            // mc.player.input = new KeyboardInput(mc.options);
            if (cameraType.isFirstPerson() != mc.options.getCameraType().isFirstPerson()) {
                mc.gameRenderer.checkEntityPostEffect(mc.options.getCameraType().isFirstPerson() ? mc.getCameraEntity() : null);
            }
            oldCameraType = null;
            System.out.println(mc.player.input);
        }
    }
}
