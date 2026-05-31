package io.github.jamalam360.jamlib.impl.network.fabric;

import io.github.jamalam360.jamlib.api.network.NetworkContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.player.Player;

public class FabricServerNetworkContext implements NetworkContext {
	private final ServerPlayNetworking.Context delegate;

	public FabricServerNetworkContext(ServerPlayNetworking.Context delegate) {
		this.delegate = delegate;
	}

	@Override
	public Player getPlayer() {
		return this.delegate.player();
	}
}
