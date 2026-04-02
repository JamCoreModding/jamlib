package io.github.jamalam360.jamlib.events;

import java.util.function.Function;

public class CancellableEvent<L, R> extends Event<L> {
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
