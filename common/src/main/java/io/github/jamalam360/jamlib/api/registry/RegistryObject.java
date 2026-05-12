package io.github.jamalam360.jamlib.api.registry;

import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

/**
 * An object within a registry.
 *
 * @param <T> The registry type.
 */
public interface RegistryObject<T> extends Supplier<T> {
	/**
	 * @param key The registry key of the object being created.
	 * @return The created object.
	 */
	T create(ResourceKey<T> key);
}
