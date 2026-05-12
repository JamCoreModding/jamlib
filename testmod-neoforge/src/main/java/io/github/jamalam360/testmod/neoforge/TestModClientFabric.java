package io.github.jamalam360.testmod.neoforge;

import io.github.jamalam360.testmod.TestMod;
import io.github.jamalam360.testmod.TestModClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = TestMod.MOD_ID, dist = Dist.CLIENT)
public class TestModClientFabric {

    public TestModClientFabric() {
        TestModClient.init();
    }
}
