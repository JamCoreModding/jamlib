package io.github.jamalam360.jamlib.impl.network.fabric;

import io.github.jamalam360.jamlib.api.network.NetworkContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.player.Player;

public class FabricClientNetworkContext implements NetworkContext {
	private final ClientPlayNetworking.Context delegate;

	public FabricClientNetworkContext(ClientPlayNetworking.Context delegate) {
		this.delegate = delegate;
	}

	@Override
	public Player getPlayer() {
		return this.delegate.player();
	}
}
