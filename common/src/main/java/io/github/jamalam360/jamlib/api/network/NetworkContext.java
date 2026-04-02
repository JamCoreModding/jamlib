package io.github.jamalam360.jamlib.api.network;

import net.minecraft.world.entity.player.Player;

/**
 * Context passed to a {@link NetworkPayloadHandler}.
 * @param player The player who received or sent the packet.
 */
public record NetworkContext(Player player) {
}
