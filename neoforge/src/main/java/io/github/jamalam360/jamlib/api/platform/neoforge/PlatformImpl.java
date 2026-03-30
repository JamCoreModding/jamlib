package io.github.jamalam360.jamlib.api.platform.neoforge;

import io.github.jamalam360.jamlib.api.platform.ModInfo;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.Optional;

public class PlatformImpl {
		public static Optional<ModInfo> getMod(String modId) {
		return ModList.get()
				.getModContainerById(modId)
				.map(m -> new ModInfo(m.getModId(), m.getModInfo().getDisplayName(), m.getModInfo().getVersion().toString()));
	}

	public static boolean isForge() {
		return false;
	}

	public static boolean isNeoForge() {
		return true;
	}

    public static boolean isDevelopmentEnvironment() {
		return !FMLEnvironment.isProduction();
    }

    public static Path getGameFolder() {
		return FMLPaths.GAMEDIR.get().toAbsolutePath();
    }

    public static Path getConfigFolder() {
		return FMLPaths.CONFIGDIR.get().toAbsolutePath();
    }
}
