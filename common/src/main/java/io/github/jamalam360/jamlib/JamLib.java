package io.github.jamalam360.jamlib;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JamLib {
	public static final String MOD_ID = "jamlib";
	public static final String MOD_NAME = "JamLib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
	protected static final JarRenamingChecker JAR_RENAMING_CHECKER = new JarRenamingChecker();

	@ApiStatus.Internal
	public static void init() {
		LOGGER.info("Initializing JamLib on " + JamLibPlatform.getPlatform());
		checkForJarRenaming(JamLib.class);

		EnvExecutor.runInEnv(EnvType.CLIENT, () -> () -> ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(JamLibClient::onPlayerJoin));
	}

	/**
	 * Check that the jar that the given class is in has not been renamed (mod rehosting sites often do this).
	 * If the jar has been renamed, a message will be displayed to the player when they join a world.
	 *
	 * @param anyModClass any class from the mod
	 * @see JarRenamingChecker
	 */
	public static void checkForJarRenaming(Class<?> anyModClass) {
		if (!Platform.isDevelopmentEnvironment()) {
			JAR_RENAMING_CHECKER.checkJar(anyModClass);
		}
	}

	@ApiStatus.Internal
	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
