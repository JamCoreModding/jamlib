package io.github.jamalam360.jamlib.fabric;

import io.github.jamalam360.jamlib.client.JamLibClient;
import net.fabricmc.api.ClientModInitializer;

public class JamLibClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		JamLibClient.init();
	}
}
