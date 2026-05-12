package io.github.jamalam360.jamlib.api.registry;

import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public interface RegistryObject<T> extends Supplier<T> {
	T create(ResourceKey<T> key);
}
