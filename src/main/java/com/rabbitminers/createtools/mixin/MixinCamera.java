package com.rabbitminers.createtools.mixin;

import com.rabbitminers.createtools.blocks.testtable.TestTableCameraController;
import com.rabbitminers.createtools.blocks.testtable.TestTableCameraHandler;
import com.rabbitminers.createtools.handler.InputHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow(aliases = "Lnet/minecraft/client/Camera;setRotation(FF)V")
    protected abstract void setRotation(float p_90573_, float p_90574_);

    @Shadow(aliases = "Lnet/minecraft/client/Camera;setPosition(DDD)V")
    protected abstract void setPosition(double p_90585_, double p_90586_, double p_90587_);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 0), method = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V", cancellable = true)
    private void onSetup(BlockGetter level, Entity entity, boolean detached, boolean mirrored, float particalTicks, CallbackInfo info) {
        if (entity instanceof Player player) {
            TestTableCameraController controller = TestTableCameraHandler.getControllerOfPlayerUUID(player.getUUID());
            if (controller != null && player.isShiftKeyDown()) {
                controller.disable();
                System.out.println("Disabled!");
            } else if (controller != null && controller.isActive()) {
                double x = controller.getX();
                double y = controller.getY();
                double z = controller.getZ();

                if (InputHandler.isHoldingUp(player)) {
                    x += 0.5;
                    System.out.println("X" + x);
                }
                if (InputHandler.isHoldingDown(player)) {
                    x -= 0.5;
                    System.out.println("X" + x);
                }
                if (InputHandler.isSwitchUsed(player)) {
                    z += 0.5;
                    System.out.println("Z" + z);
                }
                if (InputHandler.isToolUsed(player)) {
                    z -= 0.5;
                    System.out.println("Z" + z);
                }

                setPosition(x, y-0.25, z);

                switch (controller.getDirection()) {
                    case NORTH -> {setRotation(180, 50);}
                    case EAST -> {setRotation(90, 50);}
                    case SOUTH -> {
                        setRotation(180, 50);
                        setPosition(x-0.25, y-0.25, z);
                    }
                    case WEST -> {setRotation(270, 50);}
                    default -> {setRotation(0, 0);}
                }

                player.displayClientMessage(new TextComponent("Press Shift To Exit").withStyle(ChatFormatting.GRAY), true);

                info.cancel();
            }
        }
    }
}