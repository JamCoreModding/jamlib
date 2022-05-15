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

package io.github.jamalam360.jamlib.tick;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class TickScheduling {
    private static final int POLL_INTERVAL = 5;
    private static final List<Pair<Integer, Consumer<World>>> SERVER_EVENTS = new ArrayList<>();
    private static final List<Pair<Integer, Consumer<World>>> SERVER_EVENTS_REMOVE = new ArrayList<>();
    private static final List<Pair<Integer, Consumer<World>>> CLIENT_EVENTS = new ArrayList<>();
    private static final List<Pair<Integer, Consumer<World>>> CLIENT_EVENTS_REMOVE = new ArrayList<>();

    private static int ticks = 0;

    public static void onEndTickClient(ClientWorld world) {
        ticks++;

        if (ticks % POLL_INTERVAL == 0) {
            for (Pair<Integer, Consumer<World>> entry : CLIENT_EVENTS) {
                if (entry.getFirst() >= ticks) {
                    entry.getSecond().accept(world);
                    CLIENT_EVENTS_REMOVE.add(entry);
                }
            }

            CLIENT_EVENTS.removeAll(CLIENT_EVENTS_REMOVE);
            CLIENT_EVENTS_REMOVE.clear();
        }
    }

    public static void onEndTickServer(ServerWorld world) {
        ticks++;

        if (ticks % POLL_INTERVAL == 0) {
            for (Pair<Integer, Consumer<World>> entry : SERVER_EVENTS) {
                if (entry.getFirst() >= ticks) {
                    entry.getSecond().accept(world);
                    SERVER_EVENTS_REMOVE.add(entry);
                }
            }

            SERVER_EVENTS.removeAll(SERVER_EVENTS_REMOVE);
            SERVER_EVENTS_REMOVE.clear();
        }
    }

    public void schedule(Side side, int ticksTillConsumerCalled, Consumer<World> consumer) {
        int finalTick = ticks + ticksTillConsumerCalled;

        switch (side) {
            case CLIENT -> CLIENT_EVENTS.add(Pair.of(finalTick, consumer));
            case SERVER -> SERVER_EVENTS.add(Pair.of(finalTick, consumer));
            default -> {
                CLIENT_EVENTS.add(Pair.of(finalTick, consumer));
                SERVER_EVENTS.add(Pair.of(finalTick, consumer));
            }
        }
    }

    enum Side {
        CLIENT,
        SERVER,
        BOTH
    }
}
