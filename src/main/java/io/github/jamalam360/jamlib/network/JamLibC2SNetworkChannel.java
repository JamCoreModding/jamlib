package io.github.jamalam360.jamlib.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

/**
 * @author Jamalam
 */
public class JamLibC2SNetworkChannel extends JamLibNetworkChannel<ServerPlayNetworking.PlayChannelHandler> {
    public JamLibC2SNetworkChannel(Identifier identifier) {
        super(identifier);
    }

    public void send(Consumer<PacketByteBuf> dataWriter) {
        PacketByteBuf buf = PacketByteBufs.create();
        dataWriter.accept(buf);
        ClientPlayNetworking.send(this.getIdentifier(), buf);
    }

    @Override
    protected void registerHandler() {
        ServerPlayNetworking.registerGlobalReceiver(this.getIdentifier(), this.getHandler());
    }
}
