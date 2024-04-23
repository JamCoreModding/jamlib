package io.github.jamalam360.jamlib.fabriclike.config;

import com.mojang.datafixers.util.Pair;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.jamalam360.jamlib.config.ConfigManager;
import io.github.jamalam360.jamlib.config.gui.ConfigScreen;
import io.github.jamalam360.jamlib.config.gui.SelectConfigScreen;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ModMenuCompatibility implements ModMenuApi {

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
        return ConfigManager
              .MANAGERS
              .values()
              .stream()
              .map(ConfigManager::getModId)
              .distinct()
              .map(modId -> new Pair<String, ConfigScreenFactory<?>>(modId, createScreenFactoryForMod(modId)))
              .collect(HashMap::new, (m1, m2) -> m1.put(m2.getFirst(), m2.getSecond()), Map::putAll);
    }
}
