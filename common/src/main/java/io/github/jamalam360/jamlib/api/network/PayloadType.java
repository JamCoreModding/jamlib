package io.github.jamalam360.jamlib.api.network;

import net.minecraft.resources.Identifier;

/**
 * Uniquely identifies a {@link NetworkPayloadType}.
 * @param id A unique identifier for the payload type.
 */
public record PayloadType<T>(Identifier id) {
}
