package io.github.jamalam360.jamlib.impl.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.config.ConfigManager;
import io.github.jamalam360.jamlib.api.network.PacketDirection;
import io.github.jamalam360.jamlib.client.api.command.ClientCommandRegistrationEvent;
import io.github.jamalam360.jamlib.client.api.command.ClientCommandSourceStack;
import io.github.jamalam360.jamlib.client.impl.JamLibClient;
import io.github.jamalam360.jamlib.client.impl.config.ConfigScreen;
import io.github.jamalam360.jamlib.client.impl.config.SelectConfigScreen;
import io.github.jamalam360.jamlib.client.impl.keymapping.neoforge.PlatformKeyMappingRegistryImpl;
import io.github.jamalam360.jamlib.impl.network.JamLibPacket;
import io.github.jamalam360.jamlib.impl.network.NetworkImpl;
import io.github.jamalam360.jamlib.impl.network.neoforge.NeoForgeNetworkContext;
import io.github.jamalam360.jamlib.impl.pack.neoforge.ClientPackReloadListenerRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;

@Mod(value = JamLib.MOD_ID, dist = Dist.CLIENT)
public class JamLibNeoForgeClient {
	public JamLibNeoForgeClient(IEventBus bus) {
		JamLibClient.init();
		NeoForge.EVENT_BUS.addListener(this::onRegisterClientCommands);
		bus.addListener(ClientPackReloadListenerRegistry::registerListeners);
		bus.addListener(PlatformKeyMappingRegistryImpl::registerMappings);
		bus.register(this);
	}

	// Commands
	@SuppressWarnings("unchecked")
	private void onRegisterClientCommands(RegisterClientCommandsEvent ev) {
		ClientCommandRegistrationEvent.EVENT.invoke(l -> l.register((CommandDispatcher<ClientCommandSourceStack>) (CommandDispatcher<?>) ev.getDispatcher(), ev.getBuildContext()));
	}

	// Config
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

	// Networking
	@SubscribeEvent
	private void onRegisterClientPayloadHandlers(RegisterClientPayloadHandlersEvent ev) {
		ev.register(JamLibPacket.TYPE, (payload, ctx) -> NetworkImpl.receive(PacketDirection.CLIENTBOUND, new NeoForgeNetworkContext(ctx), payload));
	}
}
