package io.github.jamalam360.jamlib.impl.neoforge;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.config.ConfigManager;
import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.api.network.NetworkContext;
import io.github.jamalam360.jamlib.client.impl.JamLibClient;
import io.github.jamalam360.jamlib.client.impl.config.ConfigScreen;
import io.github.jamalam360.jamlib.client.impl.config.SelectConfigScreen;
import io.github.jamalam360.jamlib.client.impl.keymapping.neoforge.PlatformKeyMappingRegistryImpl;
import io.github.jamalam360.jamlib.impl.network.JamLibPacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

import java.util.List;

@Mod(value = JamLib.MOD_ID, dist = Dist.CLIENT)
public class JamLibNeoForgeClient {
	public JamLibNeoForgeClient(IEventBus bus) {
		JamLibClient.init();
		bus.addListener(PlatformKeyMappingRegistryImpl::registerMappings);
		bus.register(this);
	}

	@SubscribeEvent
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
			return (ignored, parent) -> new ConfigScreen<>(managers.getFirst(), parent);
		} else {
			return (ignored, parent) -> new SelectConfigScreen(parent, modId);
		}
	}

	@SubscribeEvent
	private void onRegisterClientPayloadHandlers(RegisterClientPayloadHandlersEvent ev) {
		ev.register(JamLibPacket.TYPE, (payload, ctx) -> Network.receive(Network.Direction.CLIENT_BOUND, new NetworkContext(ctx.player()), payload));
	}
}
