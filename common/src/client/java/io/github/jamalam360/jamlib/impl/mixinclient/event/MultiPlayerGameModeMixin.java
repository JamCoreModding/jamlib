package io.github.jamalam360.jamlib.impl.mixinclient.event;

import io.github.jamalam360.jamlib.api.events.InteractionEvent;
import io.github.jamalam360.jamlib.api.events.core.EventResult;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Injection point credit to Fabric API: https://github.com/FabricMC/fabric-api/blob/05ed2daee8dab2367d68aacd75bf86d2277ec177/fabric-events-interaction-v0/src/client/java/net/fabricmc/fabric/mixin/event/interaction/client/MultiPlayerGameModeMixin.java#L99
@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
	@Shadow
	protected abstract void startPrediction(ClientLevel level, PredictiveAction action);

	@Inject(
			method = "useItemOn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startPrediction(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/multiplayer/prediction/PredictiveAction;)V"
			),
			cancellable = true
	)
	private void jamlib$onInteractBlock(LocalPlayer player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
		if (player.isSpectator()) {
			return;
		}

		EventResult<InteractionResult> result = InteractionEvent.USE_BLOCK.invokeCancellable((ev) -> ev.onUseBlock(player, hand, hitResult.getBlockPos(), hitResult.getDirection()));

		if (result.wasCancelled()) {
			if (result.getResult() != null && result.getResult().consumesAction()) {
				startPrediction((ClientLevel) player.level(), id -> new ServerboundUseItemOnPacket(hand, hitResult, id));
			}

			cir.setReturnValue(result.getResult());
		}
	}
}
