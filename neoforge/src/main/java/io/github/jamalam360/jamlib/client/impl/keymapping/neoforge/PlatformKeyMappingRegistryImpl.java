package io.github.jamalam360.jamlib.client.impl.keymapping.neoforge;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import java.util.ArrayList;
import java.util.List;

public class PlatformKeyMappingRegistryImpl {
	private static List<KeyMapping> queue = new ArrayList<>();

	public static void register(KeyMapping mapping) {
		if (queue == null) {
			throw new IllegalStateException("KeyMapping cannot be registered after mod initialization");
		}

		queue.add(mapping);
	}

	public static void registerMappings(RegisterKeyMappingsEvent event) {
		queue.forEach(event::register);
		queue = null;
	}
}
