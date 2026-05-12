package io.github.jamalam360.jamlib.client.api.keymapping;

import io.github.jamalam360.jamlib.client.impl.keymapping.PlatformKeyMappingRegistry;
import net.minecraft.client.KeyMapping;

public class KeyMappingRegistry {
	public static KeyMapping register(KeyMapping mapping) {
		PlatformKeyMappingRegistry.register(mapping);
		return mapping;
	}
}
