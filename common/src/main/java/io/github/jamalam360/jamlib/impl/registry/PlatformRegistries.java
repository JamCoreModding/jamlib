package io.github.jamalam360.jamlib.impl.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.registry.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.Map;

@SuppressWarnings("unused")
public class PlatformRegistries {
	@ExpectPlatform
	public static <T> void submitRegistryEntries(Registry<T> registry, Map<Identifier, RegistryObject<T>> entries) {
		JamLib.expectPlatform();
	}
}
