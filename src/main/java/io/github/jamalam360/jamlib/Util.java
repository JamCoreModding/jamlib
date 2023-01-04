/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Jamalam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.jamalam360.jamlib;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

/**
 * General utilities that aren't related to Minecraft.
 */
public class Util {

    /**
     * @param value    The nullable value.
     * @param consumer The consumer to run if {@code value} is not null.
     * @param <T>      The return type.
     */
    public static <T> void ifNotNull(@Nullable T value, Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    /**
     * @param value        The nullable value.
     * @param consumer     The consumer to run if {@code value} is not null.
     * @param elseCallback The callback to run if {@code value} is null.
     * @param <T>          The return type.
     */
    public static <T> void ifNotNull(@Nullable T value, Consumer<T> consumer, Runnable elseCallback) {
        if (value != null) {
            consumer.accept(value);
        } else {
            elseCallback.run();
        }
    }

    /**
     * @param value     The nullable value.
     * @param function  The function to run to calculate the return value if {@code value} is not null.
     * @param elseValue The value to return if {@code value} is null.
     * @param <T>       The value type.
     * @param <R>       The return type.
     *
     * @return The value computed by the {@code function} if {@code value} is not null, or else the {@code elseValue}.
     */
    public static <T, R> R ifNotNull(@Nullable T value, Function<T, R> function, R elseValue) {
        if (value != null) {
            return function.apply(value);
        }

        return elseValue;
    }

    /**
     * @param value        The nullable value.
     * @param function     The function to run to calculate the return value if {@code value} is not null.
     * @param elseFunction The function to run to calculate the return value if {@code value} is null.
     * @param <T>          The value type.
     * @param <R>          The return type.
     *
     * @return The value computed by the {@code function} if {@code value} is not null, or else the value computed by the {@code elseFunction}.
     */
    public static <T, R> R ifNotNull(@Nullable T value, Function<T, R> function, Supplier<R> elseFunction) {
        if (value != null) {
            return function.apply(value);
        }

        return elseFunction.get();
    }
}
