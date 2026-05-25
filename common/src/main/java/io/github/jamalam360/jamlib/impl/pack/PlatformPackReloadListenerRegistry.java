package io.github.jamalam360.jamlib.impl.pack;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.jamalam360.jamlib.JamLib;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.Collection;

public class PlatformPackReloadListenerRegistry {
	@ExpectPlatform
	public static void register(PackType type, Identifier id, Collection<Identifier> dependencies, PreparableReloadListener listener) {
		JamLib.expectPlatform();
	}
}
