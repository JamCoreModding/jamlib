package io.github.jamalam360.jamlib.impl.pack.neoforge;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.neoforge.event.SortedReloadListenerEvent;

import java.util.Collection;

public record Listener(Identifier id, Collection<Identifier> dependencies, PreparableReloadListener listener) {
	public void register(SortedReloadListenerEvent event) {
		event.addListener(this.id(), this.listener());
		for (Identifier dep : this.dependencies()) {
			event.addDependency(this.id(), dep);
		}
	}
}
