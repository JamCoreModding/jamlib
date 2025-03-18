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

package io.github.jamalam360.jamlib.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

/**
 * @author Jamalam
 */
@Deprecated
public class JamLibServerNetworking {

    /**
     * An {@link Map} that maps mod ID's to a list of registered channels under that mod ID.
     */
    @Deprecated
    protected static final Map<String, List<JamLibNetworkChannel<?>>> SERVER_CHANNELS = new HashMap<>();

    /**
     * Register all server-side networking channels associated with this {@code modId}.
     *
     * @param modId Your mods ID.
     */
    @Deprecated
    public static void registerHandlers(String modId) {
        for (JamLibNetworkChannel<?> channel : SERVER_CHANNELS.getOrDefault(modId, List.of())) {
            ServerPlayNetworking.registerGlobalReceiver(channel.getIdentifier(), (ServerPlayNetworking.PlayChannelHandler) channel.getHandler());
        }
    }
}
