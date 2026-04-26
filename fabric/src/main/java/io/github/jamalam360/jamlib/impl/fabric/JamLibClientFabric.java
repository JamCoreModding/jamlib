package io.github.jamalam360.jamlib.impl.fabric;

import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.api.network.NetworkContext;
import io.github.jamalam360.jamlib.client.impl.JamLibClient;
import io.github.jamalam360.jamlib.impl.fabric.network.JamLibPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class JamLibClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		JamLibClient.init();
        ClientPlayNetworking.registerGlobalReceiver(JamLibPacket.TYPE, (payload, ctx) -> Network.receive(Network.Direction.CLIENT_BOUND, new NetworkContext(ctx.player()), payload));
	}
}
