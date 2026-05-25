package io.github.jamalam360.jamlib.impl.fabric;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.events.CommandRegistrationEvent;
import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.api.network.NetworkContext;
import io.github.jamalam360.jamlib.impl.network.JamLibPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class JamLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        JamLib.init();

        // Commands
        CommandRegistrationCallback.EVENT.register(((d, b, s) -> CommandRegistrationEvent.EVENT.invoke((l) -> l.register(d, b, s))));

        // Networking
        PayloadTypeRegistry.serverboundPlay().register(JamLibPacket.TYPE, JamLibPacket.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(JamLibPacket.TYPE, JamLibPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(JamLibPacket.TYPE, (payload, ctx) -> Network.receive(Network.Direction.SERVER_BOUND, new NetworkContext(ctx.player()), payload));
    }
}
