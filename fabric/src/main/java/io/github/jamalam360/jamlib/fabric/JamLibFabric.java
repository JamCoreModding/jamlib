package io.github.jamalam360.jamlib.fabric;

import io.github.jamalam360.jamlib.JamLib;
import net.fabricmc.api.ModInitializer;

public class JamLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        JamLib.init();
    }
}
