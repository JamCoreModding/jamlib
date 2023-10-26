package io.github.jamalam360.jamlib.fabriclike.config;

import com.google.common.base.Suppliers;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.jamalam360.jamlib.config.ConfigManager;
import io.github.jamalam360.jamlib.config.gui.ConfigScreen;
import io.github.jamalam360.jamlib.config.gui.SelectConfigScreen;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@ApiStatus.Internal
public class ModMenuCompatibility implements ModMenuApi {
	private final Supplier<Map<String, ConfigScreenFactory<?>>> factories = Suppliers.memoize(() -> {
		Map<String, ConfigScreenFactory<?>> factories = new HashMap<>();
		ConfigManager.MANAGERS.values().stream().map(ConfigManager::getModId).distinct().forEach(modId -> factories.put(modId, createScreenFactoryForMod(modId)));
		return factories;
	});

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
		return this.factories.get();
	}
}
