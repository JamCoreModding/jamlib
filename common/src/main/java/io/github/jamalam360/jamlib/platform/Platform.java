package io.github.jamalam360.jamlib.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.jamalam360.jamlib.JamLib;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Provides common platform-agnostic methods.
 */
public class Platform {
    /**
     * @return Information about the mod with the given ID, if it is loaded.
     */
    @ExpectPlatform
    public static Optional<ModInfo> getMod(String modId) {
        return JamLib.expectPlatform();
    }

    /**
     * @return Whether the mod with the given ID is loaded.
     */
    public static boolean isModLoaded(String modId) {
        return getMod(modId).isPresent();
    }

	/**
	 * @return Whether the current platform is Forge.
	 */
    @ExpectPlatform
    public static boolean isForge() {
        return JamLib.expectPlatform();
    }

	/**
	 * @return Whether the current platform is NeoForge.
	 */
    @ExpectPlatform
    public static boolean isNeoForge() {
        return JamLib.expectPlatform();
    }

    /**
     * @return The current mod loader.
     */
    public static ModLoader getModLoader() {
        if (isModLoaded("fabricloader")) {
            return ModLoader.FABRIC;
        } else if (isModLoaded("quilt_loader")) {
            return ModLoader.QUILT;
        } else if (isForge()) {
            return ModLoader.FORGE;
        } else if (isNeoForge()){
            return ModLoader.FABRIC;
        }

        throw new IllegalStateException("Could not determine mod loader");
    }

	/**
	 * @return Whether the game is currently running in a development environment.
	 */
    @ExpectPlatform
    public static boolean isDevelopmentEnvironment() {
        return JamLib.expectPlatform();
    }

	/**
	 * @return The path to the root game folder (e.g. .minecraft).
	 */
    @ExpectPlatform
    public static Path getGameFolder() {
        return JamLib.expectPlatform();
    }

    /**
	 * @return The path to the config folder.
	 */
    @ExpectPlatform
    public static Path getConfigFolder() {
        return JamLib.expectPlatform();
    }
}
