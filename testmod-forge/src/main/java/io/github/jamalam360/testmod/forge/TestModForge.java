package io.github.jamalam360.testmod.forge;

import io.github.jamalam360.testmod.TestMod;
import net.minecraftforge.fml.common.Mod;

@Mod(TestMod.MOD_ID)
public class TestModForge {
	public TestModForge() {
		TestMod.init();
	}
}
