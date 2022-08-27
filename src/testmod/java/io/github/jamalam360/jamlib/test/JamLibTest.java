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

package io.github.jamalam360.jamlib.test;

import io.github.jamalam360.jamlib.config.v2.Description;
import io.github.jamalam360.jamlib.config.v2.Excluded;
import io.github.jamalam360.jamlib.config.v2.JamLibConfig;
import io.github.jamalam360.jamlib.config.v2.NestedConfig;
import io.github.jamalam360.jamlib.log.JamLibLogger;
import io.github.jamalam360.jamlib.network.JamLibServerNetworking;
import io.github.jamalam360.jamlib.registry.JamLibRegistry;
import io.github.jamalam360.jamlib.test.registry.TestBlocks;
import io.github.jamalam360.jamlib.test.registry.TestItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.literal;

public class JamLibTest implements ModInitializer {
    public static final String MOD_ID = "jamlib-test";
    public static final JamLibLogger LOGGER = JamLibLogger.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        JamLibRegistry.register(TestItems.class, TestBlocks.class);

        JamLibTestNetwork.NETWORK_KEYBIND_PRESS.setHandler(((server, player, handler, buf, responseSender) -> {
            int i = buf.readInt();
            player.sendMessage(Text.literal("Random number: " + i + "; Client: " + player.world.isClient()), false);
            JamLibTestNetwork.NETWORK_KEYBIND_PRESS_RESPONSE.send(player, (resBuf) -> resBuf.writeInt(i + 1));
        }));

        JamLibServerNetworking.registerHandlers("jamlib-test");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("jamlib-test").then(literal("config").executes(context -> {
            context.getSource().sendFeedback(Text.literal(String.valueOf(Config.testInt)), false);
            return 1;
        }))));

//        JamLibConfig.init("jamlib-test", Config.class);
        JamLibConfig.register(Config.class);

        System.out.println(new Config());
        System.out.println(new Config.Favourites());
        System.out.println(new Config.Favourites.Activity());

        LOGGER.logInitialize();
    }

    public enum Cheese {
        CHEDDAR,
        BRIE,
        COTTAGE_CHEESE,
        MOZZARELLA
    }

    @io.github.jamalam360.jamlib.config.v2.Config("jamlib-test")
    public static class Config {
        public static int testInt = 0;
        public static boolean testBoolean = false;
        public static Identifier dirt = new Identifier("minecraft:dirt");
        @Description("There is only one correct answer.")
        public static Cheese bestCheese = Cheese.CHEDDAR;
        @Excluded
        public static String ignored = "i am not here";

        @Override
        public String toString() {
            return "Config{" +
                    "testInt=" + testInt +
                    ", testBoolean=" + testBoolean +
                    ", dirt=" + dirt +
                    ", bestCheese=" + bestCheese +
                    ", ignored='" + ignored + '\'' +
                    '}';
        }

        @NestedConfig("favourites")
        public static class Favourites {
            public static String food = "Pizza";
            public static String drink = "Coke";

            @Override
            public String toString() {
                return "Favourites{" +
                        "food='" + food + '\'' +
                        ", drink='" + drink + '\'' +
                        '}';
            }

            @Description("What is your favourite activity?")
            @NestedConfig("activities")
            public static class Activity {
                public static String sport = "Basketball";
                public static String music = "Rock";

                @Override
                public String toString() {
                    return "Activity{" +
                            "sport='" + sport + '\'' +
                            ", music='" + music + '\'' +
                            '}';
                }
            }
        }
    }
}
