package io.github.jamalam360.jamlib.impl.registry.fabric;

import io.github.jamalam360.jamlib.api.registry.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

public class PlatformRegistriesImpl {
	public static <T> void submitRegistryEntries(Registry<T> registry, Map<Identifier, RegistryObject<T>> entries) {
		for (Map.Entry<Identifier, RegistryObject<T>> object : entries.entrySet()) {
			Registry.register(registry, object.getKey(), object.getValue().create(ResourceKey.create(registry.key(), object.getKey())));
		}
	}
}
