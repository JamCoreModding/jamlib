package io.github.jamalam360.jamlib.impl.fabric;

import com.mojang.brigadier.CommandDispatcher;
import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.api.network.NetworkContext;
import io.github.jamalam360.jamlib.client.api.command.ClientCommandRegistrationEvent;
import io.github.jamalam360.jamlib.client.api.command.ClientCommandSourceStack;
import io.github.jamalam360.jamlib.client.impl.JamLibClient;
import io.github.jamalam360.jamlib.impl.network.JamLibPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class JamLibClientFabric implements ClientModInitializer {
	@SuppressWarnings("unchecked")
	@Override
	public void onInitializeClient() {
		JamLibClient.init();

		// Commands
		ClientCommandRegistrationCallback.EVENT.register(((dispatcher, context) -> ClientCommandRegistrationEvent.EVENT.invoke(l -> l.register((CommandDispatcher<ClientCommandSourceStack>) (CommandDispatcher<?>) dispatcher, context))));

		// Networking
        ClientPlayNetworking.registerGlobalReceiver(JamLibPacket.TYPE, (payload, ctx) -> Network.receive(Network.Direction.CLIENT_BOUND, new NetworkContext(ctx.player()), payload));
	}
}
