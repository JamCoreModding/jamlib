package io.github.jamalam360.jamlib.client.impl.mixin.event;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jamalam360.jamlib.client.impl.keymapping.ScreenKeyMappingHelper;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
	@WrapOperation(
			method = "keyPress",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screens/Screen;keyPressed(Lnet/minecraft/client/input/KeyEvent;)Z"
			)
	)
	private boolean jamlib$triggerScreenKeybinds(Screen instance, KeyEvent event, Operation<Boolean> original) {
		return ScreenKeyMappingHelper.onKeyPressed(instance, event) || original.call(instance, event);
	}
}
