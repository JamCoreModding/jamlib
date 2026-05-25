package io.github.jamalam360.jamlib.api.events;

import com.mojang.brigadier.CommandDispatcher;
import io.github.jamalam360.jamlib.api.events.core.Event;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

/**
 * Event for registering commands.
 */
public class CommandRegistrationEvent {
	/**
	 * Event called when registering commands.
	 */
	public static Event<Register> EVENT = new Event<>();

	public interface Register {
		void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection);
	}
}
