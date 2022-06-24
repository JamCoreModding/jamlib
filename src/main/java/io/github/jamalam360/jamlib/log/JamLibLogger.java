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
