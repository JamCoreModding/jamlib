package io.github.jamalam360.jamlib;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
public class JamLib {
	public static final String MOD_ID = "jamlib";
	public static final String MOD_NAME = "JamLib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static void init() {
		LOGGER.info("Initializing JamLib on " + JamLibPlatform.getPlatform());
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
