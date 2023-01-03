package io.github.jamalam360.jamlib.test;

import net.fabricmc.api.ModInitializer;

public class TestCompatibilityModule implements ModInitializer {

    @Override
    public void onInitialize() {
        JamLibTest.LOGGER.info("The Fabric API compatibility module has been enabled!");
    }
}
