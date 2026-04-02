package io.github.jamalam360.jamlib.api.events;

import org.jetbrains.annotations.Nullable;

/**
 * The result of a {@link CancellableEvent}. Either passes or returns a value.
 * @param <T> The returned value type.
 */
public class EventResult<T> {
	private final boolean cancelled;
	@Nullable
	private final T result;

	private EventResult(boolean cancelled, @Nullable T result) {
		this.cancelled = cancelled;
		this.result = result;
	}

    /**
	 * Create a passing {@link EventResult}.
     * @return A passing {@link EventResult}.
     */
	public static <T> EventResult<T> pass() {
		return new EventResult<>(false, null);
	}

    /**
	 * Create a cancelling {@link EventResult} with a result.
     * @param result The result.
     * @return A cancelling {@link EventResult} with the given result.
     */
	public static <T> EventResult<T> cancel(T result) {
		return new EventResult<>(true, result);
	}

    /**
     * @return Whether this {@link EventResult} indicates a cancellation.
     */
	public boolean wasCancelled() {
		return this.cancelled;
	}

    /**
     * @return The result of this {@link EventResult}, or null if it was not canceled.
     */
	@Nullable
	public T getResult() {
		return this.result;
	}
}
