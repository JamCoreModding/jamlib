package io.github.jamalam360.jamlib.client.api.command;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

/**
 * A command source stack for client-side commands.
 */
public interface ClientCommandSourceStack extends SharedSuggestionProvider {
	void client$sendSuccess(Component message);
	void client$sendFailure(Component message);
	LocalPlayer client$getPlayer();
}
