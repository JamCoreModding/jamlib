package io.github.jamalam360.jamlib.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

/**
 * @author Jamalam
 */
public class JamLibS2CNetworkChannel extends JamLibNetworkChannel<ClientPlayNetworking.PlayChannelHandler> {
    public JamLibS2CNetworkChannel(Identifier identifier) {
        super(identifier);
    }

    public void send(ServerPlayerEntity player, Consumer<PacketByteBuf> dataWriter) {
        PacketByteBuf buf = PacketByteBufs.create();
        dataWriter.accept(buf);
        ServerPlayNetworking.send(player, this.getIdentifier(), buf);
    }

    @Override
    protected void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(this.getIdentifier(), this.getHandler());
    }
}
