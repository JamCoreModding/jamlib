package io.github.jamalam360.jamlib.impl.fabric.mixin;

import io.github.jamalam360.jamlib.client.api.command.ClientCommandSourceStack;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FabricClientCommandSource.class)
public interface FabricClientCommandSourceMixin extends ClientCommandSourceStack {
	@Override
	default void client$sendSuccess(Component message) {
		((FabricClientCommandSource) this).sendFeedback(message);
	}

	@Override
	default void client$sendFailure(Component message) {
		((FabricClientCommandSource) this).sendError(message);
	}

	@Override
	default LocalPlayer client$getPlayer() {
		return ((FabricClientCommandSource) this).getPlayer();
	}
}
