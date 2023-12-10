package io.github.jamalam360.jamlib;


/**
 * Provides common platform agnostic methods.
 *
 * @see dev.architectury.platform.Platform
 */
public class JamLibPlatform {
	/**
	 * @return The current mod loader.
	 */
	public static Platform getPlatform() {
		if (dev.architectury.platform.Platform.isModLoaded("quilt_loader")) {
			return Platform.QUILT;
		} else if (dev.architectury.platform.Platform.isMinecraftForge()) {
			return Platform.FORGE;
		} else if (dev.architectury.platform.Platform.isNeoForge()) {
			return Platform.NEOFORGE;
		} else {
			return Platform.FABRIC;
		}
	}

	/**
	 * A mod loader.
	 */
	public enum Platform {
		FABRIC,
		FORGE,
		NEOFORGE,
		QUILT;

		@Override
		public String toString() {
			return switch (this) {
				case FABRIC -> "Fabric";
				case FORGE -> "Forge";
				case NEOFORGE -> "NeoForge";
				case QUILT -> "Quilt";
			};
		}
	}
}
