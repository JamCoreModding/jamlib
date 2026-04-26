package io.github.jamalam360.jamlib.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.Minecraft;

@Deprecated(forRemoval = true)
public class ClientPlayLifecycleEvents {
	/**
	 * Called when the local player has joined a logical server.
	 */
	public static final Event<Join> JOIN = EventFactory.createLoop(Join.class);
	/**
	 * Called when the local player leaves a logical server.
	 */
	public static final Event<Leave> DISCONNECT = EventFactory.createLoop(Leave.class);

	static {
		io.github.jamalam360.jamlib.client.api.events.ClientPlayLifecycleEvents.JOIN.listen((minecraft) -> JOIN.invoker().onJoin(minecraft));
		io.github.jamalam360.jamlib.client.api.events.ClientPlayLifecycleEvents.DISCONNECT.listen((minecraft) -> DISCONNECT.invoker().onLeave(minecraft));
	}

	@FunctionalInterface
	public interface Join {
		void onJoin(Minecraft client);
	}

	@FunctionalInterface
	public interface Leave {
		void onLeave(Minecraft client);
	}
}
