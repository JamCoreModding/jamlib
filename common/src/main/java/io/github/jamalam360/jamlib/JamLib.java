package io.github.jamalam360.jamlib;

import io.github.jamalam360.jamlib.api.platform.Platform;
import io.github.jamalam360.jamlib.impl.JarRenamingChecker;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JamLib {

    public static final String MOD_ID = "jamlib";
    public static final String MOD_NAME = "JamLib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    @ApiStatus.Internal
    public static final JarRenamingChecker JAR_RENAMING_CHECKER = new JarRenamingChecker();

    @ApiStatus.Internal
    public static void init() {
	    LOGGER.info("Initializing JamLib on {}", Platform.getModLoader());
        checkForJarRenaming(JamLib.class);
    }

    /**
     * Check that the jar that the given class is in has not been renamed (mod rehosting sites often do this). If the jar has been renamed, a message will be displayed to
     * the player when they join a world.
     *
     * @param anyModClass any class from the mod
     *
     * @see JarRenamingChecker
     */
    public static void checkForJarRenaming(Class<?> anyModClass) {
        if (!Platform.isDevelopmentEnvironment()) {
            JAR_RENAMING_CHECKER.checkJar(anyModClass);
        }
    }

    @ApiStatus.Internal
    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @ApiStatus.Internal
    public static <T> T expectPlatform() {
        throw new AssertionError("Expected platform-specific implementation");
    }
}
