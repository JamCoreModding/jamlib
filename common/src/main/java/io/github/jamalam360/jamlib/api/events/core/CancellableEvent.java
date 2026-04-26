package io.github.jamalam360.jamlib.api.events.core;

import java.util.function.Function;

/**
 * A {@link Event} that can be canceled.
 * @param <L> The listener type.
 * @param <R> The return type.
 */
public class CancellableEvent<L, R> extends Event<L> {
    /**
	 * Invokes all listeners for this event.
     * @param invoker The listener invoker.
     * @return The result of the first listener to return a non-passing {@link EventResult}, or a passing {@link EventResult} if all listeners pass.
     */
	public EventResult<R> invokeCancellable(Function<L, EventResult<R>> invoker)  {
		for (int priority : this.getListeners().keySet()) {
			for (L listener : this.getListeners().get(priority)) {
				EventResult<R> result = invoker.apply(listener);

				if (result.wasCancelled()) {
					return result;
				}
			}
		}

		return EventResult.pass();
	}
}
