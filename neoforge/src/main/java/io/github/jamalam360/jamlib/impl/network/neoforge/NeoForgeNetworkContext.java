package io.github.jamalam360.jamlib.impl.network.neoforge;

import io.github.jamalam360.jamlib.api.network.NetworkContext;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NeoForgeNetworkContext implements NetworkContext {
	private final IPayloadContext delegate;

	public NeoForgeNetworkContext(IPayloadContext delegate) {
		this.delegate = delegate;
	}

	@Override
	public Player getPlayer() {
		return this.delegate.player();
	}
}
