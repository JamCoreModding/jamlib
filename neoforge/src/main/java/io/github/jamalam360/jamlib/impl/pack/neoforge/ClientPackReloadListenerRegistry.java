package io.github.jamalam360.jamlib.impl.pack.neoforge;

import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientPackReloadListenerRegistry {
	private static List<Listener> queue = new ArrayList<>();

	public static void registerClientResourcesListener(Listener listener) {
		if (queue == null) {
			throw new IllegalStateException("Reload listener cannot be registered after mod initialization");
		}

		queue.add(listener);
	}

	public static void registerListeners(AddClientReloadListenersEvent event) {
		queue.forEach((l) -> l.register(event));
		queue = null;
	}
}
