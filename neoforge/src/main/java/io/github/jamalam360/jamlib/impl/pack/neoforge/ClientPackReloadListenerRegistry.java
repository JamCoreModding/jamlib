package io.github.jamalam360.jamlib.impl.pack.neoforge;

import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientPackReloadListenerRegistry {
	private static final List<Listener> LISTENERS = new ArrayList<>();

	public static void registerClientResourcesListener(Listener listener) {
		LISTENERS.add(listener);
	}

	public static void registerListeners(AddClientReloadListenersEvent event) {
		LISTENERS.forEach((l) -> l.register(event));
	}
}
