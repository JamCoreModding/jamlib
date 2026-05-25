package io.github.jamalam360.jamlib.impl.pack.fabric;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.Collection;

public class PlatformPackReloadListenerRegistryImpl {
	public static void register(PackType type, Identifier id, Collection<Identifier> dependencies, PreparableReloadListener listener) {
		ResourceLoader loader = ResourceLoader.get(type);
		loader.registerReloadListener(id, listener);
		for (Identifier dep : dependencies) {
			loader.addListenerOrdering(dep, id);
		}
	}
}
