package io.github.jamalam360.jamlib.api.pack;

import io.github.jamalam360.jamlib.impl.pack.PlatformPackReloadListenerRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.Collection;
import java.util.List;

public class PackReloadListenerRegistry {
	public static void register(PackType type, Identifier id, PreparableReloadListener listener) {
		register(type, id, List.of(), listener);
	}

	public static void register(PackType type, Identifier id, Collection<Identifier> dependencies, PreparableReloadListener listener) {
		PlatformPackReloadListenerRegistry.register(type, id, dependencies, listener);
	}
}
