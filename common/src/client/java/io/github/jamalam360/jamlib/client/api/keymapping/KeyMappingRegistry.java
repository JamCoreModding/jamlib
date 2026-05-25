package io.github.jamalam360.jamlib.client.api.keymapping;

import io.github.jamalam360.jamlib.client.impl.keymapping.PlatformKeyMappingRegistry;
import io.github.jamalam360.jamlib.client.impl.keymapping.ScreenKeyMappingHelper;
import net.minecraft.client.KeyMapping;

/**
 * A registry for key mappings. Supports registering keymappings that will be detected in screens.
 */
public class KeyMappingRegistry {
	/**
	 * Register a keymapping and return it.
	 */
	public static KeyMapping register(KeyMapping mapping) {
		return register(mapping, false);
	}

	/**
	 * Register a keymapping and return it.
	 *
	 * @param detectInScreens whether the keymapping should additionally be enabled in screens.
	 */
	public static KeyMapping register(KeyMapping mapping, boolean detectInScreens) {
		PlatformKeyMappingRegistry.register(mapping);
		if (detectInScreens) {
			ScreenKeyMappingHelper.registerScreenKeyMapping(mapping);
		}

		return mapping;
	}
}
