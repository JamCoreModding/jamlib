package io.github.jamalam360.jamlib.impl.fabric;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.api.network.NetworkContext;
import io.github.jamalam360.jamlib.impl.fabric.network.JamLibPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class JamLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        JamLib.init();
        PayloadTypeRegistry.playC2S().register(JamLibPacket.TYPE, JamLibPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(JamLibPacket.TYPE, JamLibPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(JamLibPacket.TYPE, (payload, ctx) -> Network.receive(Network.Direction.SERVER_BOUND, new NetworkContext(ctx.player()), payload));
    }
}
