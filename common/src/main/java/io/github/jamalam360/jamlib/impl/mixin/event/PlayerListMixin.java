package io.github.jamalam360.jamlib.impl.mixin.event;

import io.github.jamalam360.jamlib.api.events.ServerConnectionEvents;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {
	@Inject(
			method = "placeNewPlayer",
			at = @At("RETURN")
	)
	private void jamlib$onPlayerConnect(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
		ServerConnectionEvents.CONNECT.invoke(c -> c.onConnect(player));
	}

	@Inject(
			method = "remove",
			at = @At("HEAD")
	)
	private void jamlib$onPlayerDisconnect(ServerPlayer player, CallbackInfo ci) {
		ServerConnectionEvents.DISCONNECT.invoke(d -> d.onDisconnect(player));
	}
}
