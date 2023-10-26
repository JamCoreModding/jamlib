package io.github.jamalam360.jamlib.forge.mixin;

import io.github.jamalam360.jamlib.config.ConfigManager;
import io.github.jamalam360.jamlib.config.gui.ConfigScreen;
import io.github.jamalam360.jamlib.config.gui.SelectConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.forgespi.language.IModInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * So, mix in to Forge impl or make users copy a line of code (to register the config screen factory)?
 * Forge won't let me provide config screens for _other_ mods as far as I can tell, so here we are.
 */
@Mixin(value = ConfigScreenHandler.class, remap = false)
public class ConfigScreenHandlerMixin {
	@Inject(
			method = "getScreenFactoryFor",
			at = @At("HEAD"),
			cancellable = true,
			remap = false
	)
	private static void jamlib$cursed$injectScreenFactoriesForJamLibConfig(IModInfo selectedMod, CallbackInfoReturnable<Optional<BiFunction<Minecraft, Screen, Screen>>> cir) {
		List<ConfigManager<?>> managers = ConfigManager.MANAGERS.values().stream().filter(m -> m.getModId().equals(selectedMod.getModId())).toList();

		if (managers.size() == 1) {
			cir.setReturnValue(Optional.of((minecraft, parent) -> new ConfigScreen<>(managers.get(0), parent)));
		} else if (managers.size() > 1) {
			cir.setReturnValue(Optional.of((minecraft, parent) -> new SelectConfigScreen(parent, selectedMod.getModId())));
		}
	}
}
