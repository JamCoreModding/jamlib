package io.github.jamalam360.jamlib.impl.registry;

import io.github.jamalam360.jamlib.api.registry.RegistryObject;
import net.minecraft.resources.ResourceKey;

import java.util.function.Function;

public class MemoizedRegistryObject<T> implements RegistryObject<T> {
	private Function<ResourceKey<T>, T> supplier;
	private T value;

	public MemoizedRegistryObject(Function<ResourceKey<T>, T> supplier) {
		this.supplier = supplier;
	}

	@Override
	public T create(ResourceKey<T> key) {
		if (this.supplier == null) {
			throw new IllegalStateException("MemoizedRegistryObject has already been created");
		}

		this.value = this.supplier.apply(key);
		this.supplier = null;
		return this.value;
	}

	@Override
	public T get() {
		if (this.value == null) {
			throw new IllegalStateException("MemoizedRegistryObject has not been created value");
		}

		return this.value;
	}
}
