package io.github.jamalam360.jamlib.forge;

import io.github.jamalam360.jamlib.JamLib;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(JamLib.MOD_ID)
public class JamLibForge {
    public JamLibForge() {
        EventBuses.registerModEventBus(JamLib.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        JamLib.init();
    }
}
