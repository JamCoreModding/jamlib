package io.github.jamalam360.jamlib.client.mixin.event;

import io.github.jamalam360.jamlib.events.client.ClientPlayLifecycleEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
@Environment(EnvType.CLIENT)
public class ClientPacketListenerMixin {
	/**
	 * Injection point taken from Fabric API - <a href="https://github.com/FabricMC/fabric/blob/1.21.2/fabric-networking-api-v1/src/client/java/net/fabricmc/fabric/mixin/networking/client/ClientPlayNetworkHandlerMixin.java#L54">...</a>
	 */
	@Inject(
			method = "handleLogin",
			at = @At("RETURN")
	)
	private void jamlib$joinServer(ClientboundLoginPacket packet, CallbackInfo ci) {
		ClientPlayLifecycleEvents.JOIN.invoker().onJoin(Minecraft.getInstance());
	}
}
