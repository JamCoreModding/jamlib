package io.github.jamalam360.jamlib.log;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jamalam
 */
@SuppressWarnings("unused")
public class JamLibLogger {
    private final String modId;
    private final Logger logger;

    private JamLibLogger(String modId) {
        this.modId = modId;
        this.logger = LogManager.getLogger(modId);
    }

    public static JamLibLogger getLogger(String modId) {
        return new JamLibLogger(modId);
    }

    private String addModId(String message) {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
            message = "[" + modId + "] " + message;
        }

        return message;
    }

    public void info(String message) {
        logger.info(addModId(message));
    }

    public void warn(String message) {
        logger.warn(addModId(message));
    }

    public void error(String message) {
        logger.error(addModId(message));
    }

    public void logInitialize() {
        logger.info("Mod initialized!");
    }

    public Logger getUnderlyingLogger() {
        return logger;
    }
}
