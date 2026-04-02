package io.github.jamalam360.jamlib.events;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import java.util.function.Consumer;

public class Event<L> {
	public static final int DEFAULT_PRIORITY = 1000;
	private final TreeMultimap<Integer, L> listeners;

	public Event() {
		this.listeners = TreeMultimap.create(Ordering.natural().reverse(), Ordering.arbitrary());
	}

	public void listen(L listener) {
		this.listen(DEFAULT_PRIORITY, listener);
	}

	public void listen(int priority, L listener) {
		this.listeners.put(priority, listener);
	}

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
