package io.github.jamalam360.jamlib.forge;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.client.JamLibClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(JamLib.MOD_ID)
public class JamLibForge {
	public JamLibForge() {
		JamLib.init();
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> JamLibClient::init);
		//noinspection removal
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onFmlLoadComplete);
	}

	public void onFmlLoadComplete(FMLLoadCompleteEvent ev) {
		ev.enqueueWork(() -> {
			if (FMLLoader.getDist() == Dist.CLIENT) {
				JamLibForgeClient.registerConfigScreens();
			}
		});
	}
}
