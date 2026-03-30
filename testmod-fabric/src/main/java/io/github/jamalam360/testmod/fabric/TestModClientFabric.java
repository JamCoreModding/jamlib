package io.github.jamalam360.testmod.fabric;

import io.github.jamalam360.testmod.TestModClient;
import net.fabricmc.api.ClientModInitializer;

public class TestModClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		TestModClient.init();
	}
}
