package io.github.jamalam360.jamlib.client.api.command;

import com.mojang.brigadier.CommandDispatcher;
import io.github.jamalam360.jamlib.api.events.core.Event;
import net.minecraft.commands.CommandBuildContext;

/**
 * Event for registering client commands.
 */
public class ClientCommandRegistrationEvent {
	/**
	 * Event called when registering client commands.
	 */
	public static Event<Register> EVENT = new Event<>();

	public interface Register {
		void register(CommandDispatcher<ClientCommandSourceStack> dispatcher, CommandBuildContext context);
	}
}
