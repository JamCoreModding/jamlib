package io.github.jamalam360.jamlib.client.mixin;

import io.github.jamalam360.jamlib.client.gui.WidgetList;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSelectionList.class)
public class AbstractSelectionListMixin {
	@Inject(
			method = "getEntryAtPosition",
			at = @At("HEAD"),
			cancellable = true
	)
	private void jamlib$modifyGetEntryAtPositionForWidgetList(double mouseX, double mouseY, CallbackInfoReturnable<Object> cir) {
		if ((Object) this instanceof WidgetList widgetList) {
			cir.setReturnValue(widgetList.getRealEntryAtPosition(mouseX, mouseY));
		}
	}
}
