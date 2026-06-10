package io.github.jamalam360.jamlib.impl.mixin.event;

import io.github.jamalam360.jamlib.api.events.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Inject(
			method = "runServer",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/MinecraftServer;buildServerStatus()Lnet/minecraft/network/protocol/status/ServerStatus;"
			)
	)
	private void jamlib$callStartedEvent(CallbackInfo info) {
		ServerLifecycleEvents.STARTED.invoke(l -> l.onStarted((MinecraftServer) (Object) this));
	}

	@Inject(
			method = "stopServer",
			at = @At("HEAD")
	)
	private void jamlib$callStoppingEvent(CallbackInfo info) {
		ServerLifecycleEvents.STOPPING.invoke(l -> l.onStopping((MinecraftServer) (Object) this));
	}

	@Inject(
			method = "stopServer",
			at = @At("TAIL")
	)
	private void jamlib$callStoppedEvent(CallbackInfo info) {
		ServerLifecycleEvents.STOPPED.invoke(l -> l.onStopped((MinecraftServer) (Object) this));
	}
}
