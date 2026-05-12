package io.github.jamalam360.jamlib.client.api.events;

import io.github.jamalam360.jamlib.api.events.core.Event;
import net.minecraft.client.multiplayer.ClientLevel;

/**
 * Events for client-side level ticking.
 */
public class ClientLevelTickEvents {
	public static final Event<Pre> PRE_TICK = new Event<>();
	public static final Event<Post> POST_TICK = new Event<>();

	public interface Pre {
		void onPreTick(ClientLevel level);
	}

	public interface Post {
		void onPostTick(ClientLevel level);
	}
}
