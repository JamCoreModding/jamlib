package io.github.jamalam360.jamlib;


/**
 * Provides common platform agnostic methods.
 *
 * @see dev.architectury.platform.Platform
 */
public class JamLibPlatform {

    /**
     * @return The current mod loader.
     */
    public static Platform getPlatform() {
        if (dev.architectury.platform.Platform.isModLoaded("quilt_loader")) {
            return Platform.QUILT;
        } else if (dev.architectury.platform.Platform.isForge()) {
            return Platform.FORGE;
        } else {
            return Platform.FABRIC;
        }
    }

    /**
     * A mod loader.
     */
    public enum Platform {
        FABRIC,
        FORGE,
        QUILT;

        public boolean isFabric() {
            return this == FABRIC;
        }

        public boolean isForge() {
            return this == FORGE;
        }

        public boolean isQuilt() {
            return this == QUILT;
        }

        public boolean isFabricLike() {
            return this == FABRIC || this == QUILT;
        }

        @Override
        public String toString() {
            return switch (this) {
                case FABRIC -> "Fabric";
                case FORGE -> "Forge";
                case QUILT -> "Quilt";
            };
        }
    }
}
