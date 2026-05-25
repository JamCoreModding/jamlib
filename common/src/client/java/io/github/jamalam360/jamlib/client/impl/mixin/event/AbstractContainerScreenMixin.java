package io.github.jamalam360.jamlib.client.impl.mixin.event;

import io.github.jamalam360.jamlib.client.api.events.ClientContainerRenderEvents;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
	@Inject(
			method = "extractRenderState",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;extractContents(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
					shift = At.Shift.AFTER
			)
	)
	private void jamlib$injectRenderEvents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
		ClientContainerRenderEvents.RENDER_FOREGROUND.invoke((l) -> l.render((AbstractContainerScreen<?>) (Object) this, graphics, mouseX, mouseY, a));
	}
}
