/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Jamalam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.jamalam360.jamlib.log;

import java.util.Arrays;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>A wrapper for the SL4J {@link Logger} class that appends a mod ID when appropriate.</p>
 *
 * <p>This is necessary as in production mod ID's are often not included by the default logger, even
 * if specified.</p>
 */
@SuppressWarnings("unused")
public class JamLibLogger {

    private final String modId;
    private final boolean developmentOnly;
    private final Logger logger;

    private JamLibLogger(String modId, boolean developmentOnly) {
        this.modId = modId;
        this.logger = LoggerFactory.getLogger(modId);
        this.developmentOnly = developmentOnly;
    }

    /**
     * @param modId Your mod's ID. Used as the loggers name.
     *
     * @return An instance of {@link JamLibLogger} you can store to use later.
     */
    public static JamLibLogger getLogger(String modId) {
        return new JamLibLogger(modId, false);
    }

    /**
     * Get a logger that only logs in development environments.
     *
     * @param modId Your mod's ID. Used as the loggers name.
     *
     * @return An instance of {@link JamLibLogger} that only logs in development environments you can store to use later.
     */
    public static JamLibLogger getDevelopmentOnlyLogger(String modId) {
        return new JamLibLogger(modId, true);
    }

    private String addModId(Object... messages) {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
            messages[0] = "[" + modId + "] " + messages[0];
        }

        return (this.developmentOnly ? "(Development Only) " : "") + String.join(" ", Arrays.stream(messages).map(Object::toString).toList());
    }

    /**
     * Logs an info level message.
     */
    public void info(Object... messages) {
        if (this.isActive()) {
            logger.info(addModId(messages));
        }
    }

    /**
     * Logs a warning level message.
     */
    public void warn(Object... messages) {
        if (this.isActive()) {
            logger.warn(addModId(messages));
        }
    }

    /**
     * Logs an error level message.
     */
    public void error(Object... messages) {
        if (this.isActive()) {
            logger.error(addModId(messages));
        }
    }

    /**
     * Prints a generic {@code 'Mod initialized'} message, for consistency across mods.
     */
    public void logInitialize() {
        this.info("Mod initialized!");
    }

    /**
     * @return The SL4J {@link Logger} that backs this {@link JamLibLogger}.
     */
    public Logger getUnderlyingLogger() {
        return logger;
    }

    /**
     * @return Whether this logger is currently logging, or discarding messages.
     */
    public boolean isActive() {
        return !this.developmentOnly || FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
