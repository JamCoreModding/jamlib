package io.github.jamalam360.jamlib.impl.mixin.event;

import io.github.jamalam360.jamlib.api.events.InteractionEvent;
import io.github.jamalam360.jamlib.api.events.core.EventResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {
	@Inject(
			method = "useItemOn",
			at = @At("HEAD"),
			cancellable = true
	)
	private void jamlib$onInteractBlock(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
		EventResult<InteractionResult> result = InteractionEvent.USE_BLOCK.invokeCancellable((ev) -> ev.onUseBlock(player, hand, hitResult.getBlockPos(), hitResult.getDirection()));

		if (result.wasCancelled()) {
			cir.setReturnValue(result.getResult());
		}
	}
}
