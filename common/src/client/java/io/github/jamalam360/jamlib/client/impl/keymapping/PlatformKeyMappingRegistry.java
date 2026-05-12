package io.github.jamalam360.jamlib.client.impl.keymapping;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.jamalam360.jamlib.JamLib;
import net.minecraft.client.KeyMapping;

public class PlatformKeyMappingRegistry {
	@ExpectPlatform
	public static void register(KeyMapping mapping) {
		JamLib.expectPlatform();
	}
}
