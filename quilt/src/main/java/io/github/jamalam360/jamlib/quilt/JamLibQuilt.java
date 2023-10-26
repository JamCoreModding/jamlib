package io.github.jamalam360.jamlib.quilt;

import io.github.jamalam360.jamlib.fabriclike.JamLibFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class JamLibQuilt implements ModInitializer {
	@Override
	public void onInitialize(ModContainer mod) {
		JamLibFabricLike.init();
	}
}
