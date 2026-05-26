package io.github.jamalam360.testmod.neoforge;

import io.github.jamalam360.testmod.TestMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = TestMod.MOD_ID, dist = Dist.CLIENT)
public class TestModClientNeoForge {

    public TestModClientNeoForge() {
        TestMod.init();
    }
}
