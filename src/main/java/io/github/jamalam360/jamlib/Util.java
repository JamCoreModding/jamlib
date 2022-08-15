package io.github.jamalam360.jamlib;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Jamalam
 */
public class Util {
    public static <T> void ifNotNull(T value, Consumer<T> consumer) {
        if (value != null) consumer.accept(value);
    }

    public static <T> void ifNotNull(T value, Consumer<T> consumer, Callback elseConsumer) {
        if (value != null) consumer.accept(value);
        else elseConsumer.accept();
    }

    public static <T, R> R ifNotNull(T value, Function<T, R> function, R elseValue) {
        if (value != null) return function.apply(value);
        return elseValue;
    }
}
