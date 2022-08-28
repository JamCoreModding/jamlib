package io.github.jamalam360.jamlib.mixin.event.client;

import io.github.jamalam360.jamlib.event.client.MouseScrollCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author Jamalam
 */

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow
    private double x;

    @Shadow
    private double y;

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
            method = "onMouseScroll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION,
            cancellable = true
    )
    private void jamlib$callMouseScrollEvent(long window, double scrollDeltaX, double scrollDeltaY, CallbackInfo ci, double amount) {
        double mouseX = this.x * (double) this.client.getWindow().getScaledWidth() / (double) this.client.getWindow().getWidth();
        double mouseY = this.y * (double) this.client.getWindow().getScaledHeight() / (double) this.client.getWindow().getHeight();

        boolean result = MouseScrollCallback.EVENT.invoker().onMouseScroll(mouseX, mouseY, amount);

        if (result) {
            ci.cancel();
        }
    }
}
