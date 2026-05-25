package io.github.jamalam360.jamlib.impl.pack.neoforge;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlatformPackReloadListenerRegistryImpl {
	private static List<Listener> queue = new ArrayList<>();

	public static void register(PackType type, Identifier id, Collection<Identifier> dependencies, PreparableReloadListener listener) {
		Listener listenerObj = new Listener(id, dependencies, listener);
		switch (type) {
			case CLIENT_RESOURCES -> ClientPackReloadListenerRegistry.registerClientResourcesListener(listenerObj);
			case SERVER_DATA -> registerServerDataListener(listenerObj);
		}
	}

	private static void registerServerDataListener(Listener listener) {
		if (queue == null) {
			throw new IllegalStateException("Reload listener cannot be registered after mod initialization");
		}

		queue.add(listener);
	}

	public static void registerListeners(AddServerReloadListenersEvent event) {
		queue.forEach((l) -> l.register(event));
		queue = null;
	}
}
