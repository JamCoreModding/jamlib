package io.github.jamalam360.jamlib.impl.neoforge;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.api.events.CommandRegistrationEvent;
import io.github.jamalam360.jamlib.api.network.Network;
import io.github.jamalam360.jamlib.api.network.NetworkContext;
import io.github.jamalam360.jamlib.impl.network.JamLibPacket;
import io.github.jamalam360.jamlib.impl.pack.neoforge.PlatformPackReloadListenerRegistryImpl;
import io.github.jamalam360.jamlib.impl.registry.neoforge.PlatformRegistriesImpl;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(JamLib.MOD_ID)
public class JamLibNeoForge {
	public JamLibNeoForge(IEventBus bus) {
		JamLib.init();
		bus.addListener(PlatformPackReloadListenerRegistryImpl::registerListeners);
		bus.addListener(PlatformRegistriesImpl::register);
		bus.register(this);
	}

	// Commands
	@SubscribeEvent
	private void onRegisterCommands(RegisterCommandsEvent event) {
		CommandRegistrationEvent.EVENT.invoke(l -> l.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection()));
	}

	// Networking
	@SubscribeEvent
	private void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);
		registrar.playBidirectional(JamLibPacket.TYPE, JamLibPacket.CODEC, (payload, ctx) -> Network.receive(Network.Direction.SERVER_BOUND, new NetworkContext(ctx.player()), payload));
	}
}
