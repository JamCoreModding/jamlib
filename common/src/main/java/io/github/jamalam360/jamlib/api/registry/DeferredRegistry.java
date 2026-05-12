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

/**
 * Cross-platform deferred registry system.
 *
 * @param <T> The registry type.
 */
public class DeferredRegistry<T> {
	/**
	 * Creates a new deferred registry.
	 *
	 * @param modId The mod ID, used for creating identifiers.
	 * @param registry The registry.
	 * @param <T> The type of objects within the registry.
	 * @return A new deferred registry.
	 */
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

	/**
	 * Queues an object for registration.
	 * @param path The identifier path for the object.
	 * @param object The object supplier.
	 * @return A registry object holding the object.
	 */
	public RegistryObject<T> register(String path, Supplier<T> object) {
		return this.register(Identifier.fromNamespaceAndPath(this.modId, path), (ignored) -> object.get());
	}

	/**
	 * Queues an object for registration.
	 * @param identifier The identifier for the object.
	 * @param object The object supplier.
	 * @return A registry object holding the object.
	 */
	public RegistryObject<T> register(Identifier identifier, Supplier<T> object) {
		return this.register(identifier, (ignored) -> object.get());
	}

	/**
	 * Queues an object for registration.
	 * @param path The identifier path for the object.
	 * @param object The object supplier.
	 * @return A registry object holding the object.
	 */
	public RegistryObject<T> register(String path, Function<ResourceKey<T>, T> object) {
		return this.register(Identifier.fromNamespaceAndPath(this.modId, path), object);
	}

	/**
	 * Queues an object for registration.
	 * @param identifier The identifier for the object.
	 * @param object The object supplier.
	 * @return A registry object holding the object.
	 */
	public RegistryObject<T> register(Identifier identifier, Function<ResourceKey<T>, T> object) {
		RegistryObject<T> registryObject = new MemoizedRegistryObject<>(object);
		this.queue.put(identifier, registryObject);
		return registryObject;
	}

	/**
	 * Registers all queued objects. This should be called during mod initialization.
	 */
	public void registerEntries() {
		PlatformRegistries.submitRegistryEntries(this.registry, this.queue);
	}
}
