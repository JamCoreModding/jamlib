/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Jamalam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
