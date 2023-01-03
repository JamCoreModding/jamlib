package io.github.jamalam360.jamlib.compatibility;

import io.github.jamalam360.jamlib.JamLib;
import java.util.Optional;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class JamLibCompatibilityModuleHandler {

    public static void initialize(String modId) {
        Optional<ModContainer> mod = FabricLoader.getInstance().getModContainer(modId);

        if (mod.isEmpty()) {
            JamLib.LOGGER.warn("Attempted to initialize compatibility modules for non-existent mod " + modId);
            return;
        }

        if (!mod.get().getMetadata().containsCustomValue("jamlib:compatibility_modules")) {
            JamLib.LOGGER.warn("Attempted to initialize compatibility modules for mod with incorrect metadata " + modId);
        }

        mod.get().getMetadata().getCustomValue("jamlib:compatibility_modules").getAsObject().forEach(((e -> {
            if (FabricLoader.getInstance().isModLoaded(e.getKey())) {
                JamLib.LOGGER.info("Initializing " + modId + " compatibility module for " + e.getKey());

                try {
                    Class<?> clazz = Class.forName("io.github.jamalam360.honk.compatibility." + e.getValue().getAsString());
                    ModInitializer init = (ModInitializer) clazz.getConstructor().newInstance();
                    init.onInitialize();
                } catch (Exception exception) {
                    JamLib.LOGGER.warn("Failed to initialize compatibility module: " + exception);
                }
            }
        })));
    }
}
