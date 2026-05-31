package io.github.jamalam360.jamlib.api.network;

import net.minecraft.world.entity.player.Player;

/**
 * Context passed to a {@link NetworkPayloadHandler}.
 */
public interface NetworkContext {
	/**
	 * @return The player associated with the received packet.
	 */
	Player getPlayer();
}
