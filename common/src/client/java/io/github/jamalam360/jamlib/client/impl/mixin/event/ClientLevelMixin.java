package io.github.jamalam360.jamlib.client.impl.mixin.event;

import io.github.jamalam360.jamlib.client.api.events.ClientLevelTickEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
	@Inject(
			method = "tickEntities",
			at = @At("HEAD")
	)
	private void jamlib$preClientLevelTick(CallbackInfo ci) {
		ClientLevelTickEvents.PRE_TICK.invoke(l -> l.onPreTick((ClientLevel) (Object) this));
	}

	@Inject(
		method = "tickEntities",
		at = @At("RETURN")
	)
	private void jamlib$postClientLevelTick(CallbackInfo ci) {
		ClientLevelTickEvents.POST_TICK.invoke(l -> l.onPostTick((ClientLevel) (Object) this));
	}
}
