package io.github.jamalam360.testmod.fabric;

import io.github.jamalam360.testmod.TestMod;
import net.fabricmc.api.ModInitializer;

public class TestModFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        TestMod.init();
    }
}
