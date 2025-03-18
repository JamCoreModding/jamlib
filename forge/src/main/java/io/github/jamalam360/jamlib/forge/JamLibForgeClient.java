package io.github.jamalam360.jamlib.forge;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.client.config.gui.ConfigScreen;
import io.github.jamalam360.jamlib.client.config.gui.SelectConfigScreen;
import io.github.jamalam360.jamlib.config.ConfigManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = JamLib.MOD_ID)
public class JamLibForgeClient {
	public static void registerConfigScreens() {
		JamLib.LOGGER.info("Registering config screens for mods");
		ModList.get().forEachModContainer((modId, mod) -> {
			List<ConfigManager<?>> managers = ConfigManager.MANAGERS.values().stream().filter(m -> m.getModId().equals(modId)).toList();

			if (mod.getCustomExtension(ConfigScreenHandler.ConfigScreenFactory.class).isPresent() || managers.isEmpty()) {
				return;
			}

			mod.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> createConfigScreenFactory(modId, managers));
		});
	}

	private static ConfigScreenHandler.ConfigScreenFactory createConfigScreenFactory(String modId, List<ConfigManager<?>> managers) {
		if (managers.size() == 1) {
			return new ConfigScreenHandler.ConfigScreenFactory((_mod, parent) -> new ConfigScreen<>(managers.get(0), parent));
		} else {
			return new ConfigScreenHandler.ConfigScreenFactory((_mod, parent) -> new SelectConfigScreen(parent, modId));
		}
	}
}
