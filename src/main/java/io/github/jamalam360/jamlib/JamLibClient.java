package io.github.jamalam360.jamlib;

import io.github.jamalam360.jamlib.tick.TickScheduling;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

/**
 * @author Jamalam360
 */
public class JamLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_WORLD_TICK.register(TickScheduling::onEndTickClient);
        JamLib.getLogger("JamLibClientInit").info("JamLib has been initialized on the client");
    }
}
