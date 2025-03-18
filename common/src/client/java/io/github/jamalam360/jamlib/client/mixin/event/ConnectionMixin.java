package io.github.jamalam360.jamlib.client.mixin.event;

import io.github.jamalam360.jamlib.events.client.ClientPlayLifecycleEvents;
import io.netty.channel.ChannelHandlerContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
@Environment(EnvType.CLIENT)
public class ConnectionMixin {
	@Inject(
			method = "channelInactive",
			at = @At("HEAD")
	)
	private void jamlib$callDisconnectEvent(ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
		ClientPlayLifecycleEvents.DISCONNECT.invoker().onLeave(Minecraft.getInstance());
	}

	@Inject(
			method = "handleDisconnection",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/PacketListener;onDisconnect(Lnet/minecraft/network/chat/Component;)V"
			)
	)
	private void jamlib$callDisconnectEvent(CallbackInfo ci) {
		ClientPlayLifecycleEvents.DISCONNECT.invoker().onLeave(Minecraft.getInstance());
	}
}
