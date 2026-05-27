package io.github.jamalam360.jamlib.client.impl.mixin.event;

import io.github.jamalam360.jamlib.client.api.events.ClientConnectionEvents;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class ConnectionMixin {
	@Inject(
			method = "channelInactive",
			at = @At("HEAD")
	)
	private void jamlib$callDisconnectEvent(ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
		ClientConnectionEvents.DISCONNECT.invoke((listener) -> listener.onDisconnect(Minecraft.getInstance()));
	}

	@Inject(
			method = "handleDisconnection",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/PacketListener;onDisconnect(Lnet/minecraft/network/DisconnectionDetails;)V"
			)
	)
	private void jamlib$callDisconnectEvent(CallbackInfo ci) {
		ClientConnectionEvents.DISCONNECT.invoke((listener) -> listener.onDisconnect(Minecraft.getInstance()));
	}
}
