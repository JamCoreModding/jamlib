package io.github.jamalam360.jamlib.fabriclike.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.config.ConfigManager;
import io.github.jamalam360.jamlib.config.gui.ConfigScreen;
import io.github.jamalam360.jamlib.config.gui.SelectConfigScreen;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiStatus.Internal
public class ModMenuCompatibility implements ModMenuApi {
	private static final Map<String, ConfigScreenFactory<?>> FACTORIES = new HashMap<>();

	private static void repopulateFactories() {
		FACTORIES.clear();
		ConfigManager.MANAGERS.values().stream().map(ConfigManager::getModId).distinct().forEach(modId -> FACTORIES.put(modId, createScreenFactoryForMod(modId)));
	}

	private static ConfigScreenFactory<?> createScreenFactoryForMod(String modId) {
		List<ConfigManager<?>> managers = ConfigManager.MANAGERS.values().stream().filter(m -> m.getModId().equals(modId)).toList();

		if (managers.size() == 1) {
			return parent -> new ConfigScreen<>(managers.get(0), parent);
		} else {
			return parent -> new SelectConfigScreen(parent, modId);
		}
	}

	@Override
	public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
		repopulateFactories();
		JamLib.LOGGER.info("Registering config screens with ModMenu: " + String.join(", ", FACTORIES.keySet()));
		return FACTORIES;
	}
}
