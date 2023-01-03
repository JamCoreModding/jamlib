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

package io.github.jamalam360.jamlib;

import static net.minecraft.server.command.CommandManager.literal;

import io.github.jamalam360.jamlib.log.JamLibLogger;
import io.github.jamalam360.jamlib.tick.TickScheduling;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

public class JamLib implements ModInitializer {

    public static final JamLibLogger LOGGER = JamLibLogger.getLogger("jamlib");
    public static final JamLibLogger DEV_LOGGER = JamLibLogger.getDevelopmentOnlyLogger("jamlib");

    @Override
    public void onInitialize() {
        ServerTickEvents.END_WORLD_TICK.register(TickScheduling::onEndTickServer);

        //noinspection CodeBlock2Expr
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, env) -> {
            dispatcher.register(
                  literal("jamlib").then(
                        literal("version").executes(context -> {
                            //noinspection OptionalGetWithoutIsPresent
                            context.getSource().sendFeedback(Text.literal("JamLib " + FabricLoader.getInstance().getModContainer("jamlib").get().getMetadata().getVersion()), false);
                            return 1;
                        })
                  )
            );
        });

        LOGGER.logInitialize();
    }
}
