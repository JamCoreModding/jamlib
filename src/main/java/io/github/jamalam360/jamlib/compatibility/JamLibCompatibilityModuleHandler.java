/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 Jamalam
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

package io.github.jamalam360.jamlib.compatibility;

import io.github.jamalam360.jamlib.JamLib;
import java.util.Optional;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;

/**
 * <p>Helper class to initialize 'compatibility modules' for a mod. A compatibility module is
 * an entrypoint (that implements {@link ModInitializer}) that is only loaded when another specific mod is loaded.</p>
 *
 * <p>To define compatibility modules, create a {@code jamlib:compatibility_modules} map
 * in your {@code fabric.mod.json}'s {@code custom} field (or the equivalent for the Quilt toolchain). The keys of this map should be mod ID's, and the values should be
 * the fully qualified name of your compatibility module</p>
 *
 * <p>For example, to create an entrypoint that is only loaded if EMI is installed:</p>
 * <pre>
 * {@code
 * "custom": {
 *      "jamlib:compatibility_modules": {
 *         "emi": "io.github.jamalam360.test.EmiCompatibility"
 *      }
 * }
 * }
 * </pre>
 * <p>JamLib only loads these classes if the other mod is present, so as long as you are
 * careful, you shouldn't run into classloading issues.</p>
 *
 * <p>You can also optionally define a version(-range), which is parsed using {@link VersionPredicate#parse(String)}:</p>
 * <pre>
 * {@code
 * "custom": {
 *      "jamlib:compatibility_modules": {
 *         "emi[>=0.3.0]": "io.github.jamalam360.test.EmiCompatibility",
 *         "emi[<0.3.0]": "io.github.jamalam360.test.OldEmiCompatibility",
 *      }
 * }
 * }
 * </pre>
 */
public class JamLibCompatibilityModuleHandler {

    /**
     * Search for, and initialize if required, all compatibility modules under the specified {@code modId}.
     *
     * @param modId <b>Your mod's</b> ID. Used to lookup the {@code jamlib:compatibility_modules}
     *              map from the {@code fabric.mod.json} or {@code quilt.mod.json}.
     */
    public static void initialize(String modId) {
        Optional<ModContainer> mod = FabricLoader.getInstance().getModContainer(modId);

        if (mod.isEmpty()) {
            JamLib.LOGGER.warn("Attempted to initialize compatibility modules for non-existent mod " + modId);
            return;
        }

        if (!mod.get().getMetadata().containsCustomValue("jamlib:compatibility_modules")) {
            JamLib.LOGGER.warn("Attempted to initialize compatibility modules for mod with incorrect metadata", modId);
        }

        mod.get().getMetadata().getCustomValue("jamlib:compatibility_modules").getAsObject().forEach(((e -> {
            String compatId = e.getKey();
            VersionPredicate versionPredicate = null;

            if (compatId.contains("[") && compatId.endsWith("]")) {
                String version = compatId.substring(compatId.indexOf("[") + 1, compatId.length() - 1);
                compatId = compatId.substring(0, compatId.indexOf("["));

                try {
                    versionPredicate = VersionPredicate.parse(version);
                } catch (VersionParsingException ex) {
                    JamLib.LOGGER.warn("Encountered invalid version range", version, "from compatibility modules of mod", modId);
                }
            }

            if (FabricLoader.getInstance().isModLoaded(compatId) && (versionPredicate == null || versionPredicate.test(FabricLoader.getInstance().getModContainer(compatId).get().getMetadata().getVersion()))) {
                JamLib.LOGGER.info("Initializing", modId, "compatibility module for", compatId);

                try {
                    Class<?> clazz = Class.forName(e.getValue().getAsString());
                    ModInitializer init = (ModInitializer) clazz.getConstructor().newInstance();
                    init.onInitialize();
                } catch (Exception exception) {
                    JamLib.LOGGER.warn("Failed to initialize compatibility module:", exception.toString());
                }
            }
        })));
    }
}
