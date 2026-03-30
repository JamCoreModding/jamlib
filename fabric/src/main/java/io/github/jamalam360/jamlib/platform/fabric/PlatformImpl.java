package io.github.jamalam360.jamlib.platform.fabric;

import io.github.jamalam360.jamlib.platform.ModInfo;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.util.Optional;

public class PlatformImpl {
	public static Optional<ModInfo> getMod(String modId) {
		return FabricLoader.getInstance()
				.getModContainer(modId)
				.map(m -> new ModInfo(m.getMetadata().getId(), m.getMetadata().getName(), m.getMetadata().getVersion().getFriendlyString()));
	}

	public static boolean isForge() {
		return false;
	}

	public static boolean isNeoForge() {
		return false;
	}

    public static boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static Path getGameFolder() {
        return FabricLoader.getInstance().getGameDir().toAbsolutePath();
    }

    public static Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir().toAbsolutePath();
    }
}
