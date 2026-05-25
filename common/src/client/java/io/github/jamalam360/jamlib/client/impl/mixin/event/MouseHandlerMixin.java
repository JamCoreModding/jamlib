package io.github.jamalam360.jamlib.client.impl.mixin.event;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.jamalam360.jamlib.client.api.events.ClientMouseScrollEvents;
import io.github.jamalam360.jamlib.client.impl.keymapping.ScreenKeyMappingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@Inject(
			method = "onScroll",
            at = @At(
					value = "INVOKE",
		            target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDDD)Z"
            ),
		    cancellable = true
    )
    public void jamlib$triggerMouseScrollInScreenEvent(long handle, double xoffset, double yoffset, CallbackInfo ci, @Local(name = "scaledYOffset") double scaledYOffset) {
	    if (ClientMouseScrollEvents.IN_SCREENS.invokeCancellable(l -> l.onScroll(this.minecraft, scaledYOffset)).wasCancelled()) {
			ci.cancel();
	    }
    }

	@Inject(
		method = "onScroll",
        at = @At(
				value = "INVOKE",
	            target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z"
        ),
		cancellable = true
    )
    public void jamlib$triggerMouseScrollOutOfScreenEvent(long handle, double xoffset, double yoffset, CallbackInfo ci, @Local(name = "scaledYOffset") double scaledYOffset) {
	    if (ClientMouseScrollEvents.OUT_OF_SCREENS.invokeCancellable(l -> l.onScroll(this.minecraft, scaledYOffset)).wasCancelled()) {
			ci.cancel();
	    }
    }

	@Inject(
			method = "onButton",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screens/Screen;afterMouseAction()V"
			),
			cancellable = true
	)
	private void jamlib$triggerScreenKeybinds(long handle, MouseButtonInfo rawButtonInfo, int action, CallbackInfo ci, @Local MouseButtonEvent ev) {
		if (ScreenKeyMappingHelper.onMousePressed(ev)) {
			ci.cancel();
		}
	}
}
