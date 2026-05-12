package io.github.jamalam360.jamlib.impl.registry.neoforge;

import io.github.jamalam360.jamlib.api.registry.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;

public class PlatformRegistriesImpl {
	private static final Map<Registry<?>, Map<Identifier, RegistryObject<?>>> REGISTRIES = new HashMap<>();

	public static <T> void submitRegistryEntries(Registry<T> registry, Map<Identifier, RegistryObject<T>> entries) {
		if (!REGISTRIES.containsKey(registry)) {
			REGISTRIES.put(registry, new HashMap<>());
		}

		REGISTRIES.get(registry).putAll(entries);
	}

	public static void register(RegisterEvent event) {
		for (Registry<?> registry : REGISTRIES.keySet()) {
			event.register(registry.key(), r -> registerEntries(registry, r, REGISTRIES.get(registry)));
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> void registerEntries(Registry<?> registry, RegisterEvent.RegisterHelper<?> helper, Map<Identifier, RegistryObject<?>> entries) {
		for (Map.Entry<Identifier, RegistryObject<?>> entry : entries.entrySet()) {
			ResourceKey<T> key = (ResourceKey<T>) ResourceKey.create(registry.key(), entry.getKey());
			T object = ((RegistryObject<T>) entry.getValue()).create(key);
			((RegisterEvent.RegisterHelper<T>) helper).register(entry.getKey(), object);
		}
	}
}
