package io.github.jamalam360.jamlib.neoforge;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.config.ConfigManager;
import io.github.jamalam360.jamlib.config.gui.ConfigScreen;
import io.github.jamalam360.jamlib.config.gui.SelectConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.List;

@Mod(value = JamLib.MOD_ID, dist = Dist.CLIENT)
public class JamLibNeoForgeClient {

	public JamLibNeoForgeClient(IEventBus bus) {
		bus.addListener(this::onFmlLoadComplete);
	}

	private void onFmlLoadComplete(FMLLoadCompleteEvent ev) {
		ev.enqueueWork(() -> {
			JamLib.LOGGER.info("Registering config screens for mods");
			ModList.get().forEachModContainer((modId, mod) -> {
				List<ConfigManager<?>> managers = ConfigManager.MANAGERS.values().stream().filter(m -> m.getModId().equals(modId)).toList();

				if (mod.getCustomExtension(IConfigScreenFactory.class).isPresent() || managers.isEmpty()) {
					return;
				}

				mod.registerExtensionPoint(IConfigScreenFactory.class, createConfigScreenFactory(modId, managers));
			});
		});
	}
	
	private IConfigScreenFactory createConfigScreenFactory(String modId, List<ConfigManager<?>> managers) {
		if (managers.size() == 1) {
			return (_mod, parent) -> new ConfigScreen<>(managers.getFirst(), parent);
		} else {
			return (_mod, parent) -> new SelectConfigScreen(parent, modId);
		}
	}
}
