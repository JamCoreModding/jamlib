<<<<<<<< HEAD:common/src/main/java/io/github/jamalam360/jamlib/api/events/Event.java
package io.github.jamalam360.jamlib.api.events;
========
package io.github.jamalam360.jamlib.api.events.core;
>>>>>>>> 2fca7fd (feat: interaction events):common/src/main/java/io/github/jamalam360/jamlib/api/events/core/Event.java

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import java.util.function.Consumer;

/**
 * A listenable event.
 * @param <L> The listener type.
 */
public class Event<L> {
	public static final int DEFAULT_PRIORITY = 1000;
	private final TreeMultimap<Integer, L> listeners;

	public Event() {
		this.listeners = TreeMultimap.create(Ordering.natural().reverse(), Ordering.arbitrary());
	}

    /**
	 * Listens for this event, using the default priority of 1000.
     * @param listener The listener.
     */
	public void listen(L listener) {
		this.listen(DEFAULT_PRIORITY, listener);
	}

    /**
	 * Listens for this event.
     * @param priority The priority of the listener - higher numbers are executed first.
     * @param listener The listener.
     */
	public void listen(int priority, L listener) {
		this.listeners.put(priority, listener);
	}

    /**
	 * Invokes all listeners for this event.
     * @param invoker The listener invoker.
     */
	public void invoke(Consumer<L> invoker) {
		for (int priority : this.listeners.keySet()) {
			for (L listener : this.listeners.get(priority)) {
				invoker.accept(listener);
			}
		}
	}

	protected TreeMultimap<Integer, L> getListeners() {
		return this.listeners;
	}
}
