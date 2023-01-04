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

package io.github.jamalam360.jamlib.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * <p>Represents a server-to-client channel in JamLib's networking system.</p>
 */
public class JamLibS2CNetworkChannel extends JamLibNetworkChannel<ClientPlayNetworking.PlayChannelHandler> {

    /**
     * Create and register a new network channel.
     *
     * @param identifier The {@link Identifier} of this network channel.
     */
    public JamLibS2CNetworkChannel(Identifier identifier) {
        super(identifier);
    }

    /**
     * Shorthand method for sending an empty packet to the client.
     */
    public void send(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.empty();
        ServerPlayNetworking.send(player, this.getIdentifier(), buf);
    }

    /**
     * Send a packet to the client.
     *
     * @param dataWriter A consumer of a {@link PacketByteBuf} which is used to write the data.
     */
    public void send(ServerPlayerEntity player, Consumer<PacketByteBuf> dataWriter) {
        PacketByteBuf buf = PacketByteBufs.create();
        dataWriter.accept(buf);
        ServerPlayNetworking.send(player, this.getIdentifier(), buf);
    }

    /**
     * Sets the client-side handler that is called when the client receives this packet.
     *
     * @param handler A {@link net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler} that is called when the packet is received
     *                client-side.
     */
    public void setHandler(ClientPlayNetworking.PlayChannelHandler handler) {
        this.handler = handler;
        List<JamLibNetworkChannel<?>> list = JamLibClientNetworking.CLIENT_CHANNELS.getOrDefault(this.getIdentifier().getNamespace(), new ArrayList<>());
        list.add(this);
        JamLibClientNetworking.CLIENT_CHANNELS.put(this.getIdentifier().getNamespace(), list);
    }
}
