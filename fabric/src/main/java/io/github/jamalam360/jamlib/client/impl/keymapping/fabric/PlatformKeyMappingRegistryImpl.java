package io.github.jamalam360.jamlib.client.impl.keymapping.fabric;

import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

public class PlatformKeyMappingRegistryImpl {
	public static void register(KeyMapping mapping) {
		KeyMappingHelper.registerKeyMapping(mapping);
	}
}
