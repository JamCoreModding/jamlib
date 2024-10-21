package io.github.jamalam360.jamlib.events.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

/**
 * Events for client-side player lifecycle events.
 */
@Environment(EnvType.CLIENT)
public class ClientPlayLifecycleEvents {
	/**
	 * Called when the local player has joined a logical server.
	 */
	public static final Event<Join> JOIN = EventFactory.createLoop(Join.class);
	/**
	 * Called when the local player leaves a logical server.
	 */
	public static final Event<Leave> DISCONNECT = EventFactory.createLoop(Leave.class);

	@Environment(EnvType.CLIENT)
	@FunctionalInterface
	public interface Join {
		void onJoin(Minecraft client);
	}

	@Environment(EnvType.CLIENT)
	@FunctionalInterface
	public interface Leave {
		void onLeave(Minecraft client);
	}
}
