package io.github.jamalam360.jamlib.api.registry;

import io.github.jamalam360.jamlib.impl.registry.MemoizedRegistryObject;
import io.github.jamalam360.jamlib.impl.registry.PlatformRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegistry<T> {
	public static <T> DeferredRegistry<T> create(String modId, Registry<T> registry) {
		return new DeferredRegistry<>(modId, registry);
	}

	private final String modId;
	private final Registry<T> registry;
	private final Map<Identifier, RegistryObject<T>> queue;

	private DeferredRegistry(String modId, Registry<T> registry) {
		this.modId = modId;
		this.registry = registry;
		this.queue = new HashMap<>();
	}

	public RegistryObject<T> register(String path, Supplier<T> object) {
		return this.register(Identifier.fromNamespaceAndPath(this.modId, path), (ignored) -> object.get());
	}

	public RegistryObject<T> register(Identifier identifier, Supplier<T> object) {
		return this.register(identifier, (ignored) -> object.get());
	}

	public RegistryObject<T> register(String path, Function<ResourceKey<T>, T> object) {
		return this.register(Identifier.fromNamespaceAndPath(this.modId, path), object);
	}

	public RegistryObject<T> register(Identifier identifier, Function<ResourceKey<T>, T> object) {
		RegistryObject<T> registryObject = new MemoizedRegistryObject<>(object);
		this.queue.put(identifier, registryObject);
		return registryObject;
	}

	public void registerEntries() {
		PlatformRegistries.submitRegistryEntries(this.registry, this.queue);
	}
}
