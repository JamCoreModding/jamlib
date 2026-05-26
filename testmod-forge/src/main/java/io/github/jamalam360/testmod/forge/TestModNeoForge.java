package io.github.jamalam360.testmod.forge;

import io.github.jamalam360.testmod.TestMod;
import io.github.jamalam360.testmod.TestModClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(TestMod.MOD_ID)
public class TestModNeoForge {

    public TestModNeoForge() {
        TestMod.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TestModClient::init);
    }
}
