package io.github.jamalam360.jamlib.events;

import org.jetbrains.annotations.Nullable;

public class EventResult<T> {
	private final boolean cancelled;
	@Nullable
	private final T result;

	private EventResult(boolean cancelled, @Nullable T result) {
		this.cancelled = cancelled;
		this.result = result;
	}

	public static <T> EventResult<T> pass() {
		return new EventResult<>(false, null);
	}

	public static <T> EventResult<T> cancel(T result) {
		return new EventResult<>(true, result);
	}

	public boolean wasCancelled() {
		return this.cancelled;
	}

	@Nullable
	public T getResult() {
		return this.result;
	}
}
