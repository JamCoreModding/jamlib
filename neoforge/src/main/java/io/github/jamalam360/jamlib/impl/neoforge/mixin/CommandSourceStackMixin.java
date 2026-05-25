package io.github.jamalam360.jamlib.impl.neoforge.mixin;

import io.github.jamalam360.jamlib.client.api.command.ClientCommandSourceStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommandSourceStack.class)
public abstract class CommandSourceStackMixin implements ClientCommandSourceStack {
	@Override
	public void client$sendSuccess(Component message) {
		((CommandSourceStack) (Object) this).sendSuccess(() -> message, false);
	}

	@Override
	public void client$sendFailure(Component message) {
		((CommandSourceStack) (Object) this).sendFailure(message);
	}

	@Override
	public LocalPlayer client$getPlayer() {
		return (LocalPlayer) ((CommandSourceStack) (Object) this).getEntity();
	}
}
