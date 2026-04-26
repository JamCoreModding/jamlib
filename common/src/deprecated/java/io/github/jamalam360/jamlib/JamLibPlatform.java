package io.github.jamalam360.jamlib;

@Deprecated(forRemoval = true)
public class JamLibPlatform {
    public static Platform getPlatform() {
        return switch (io.github.jamalam360.jamlib.api.platform.Platform.getModLoader()) {
	        case FABRIC -> Platform.FABRIC;
	        case QUILT -> Platform.QUILT;
	        case FORGE -> throw new IllegalStateException("Forge is not supported past 1.20.1");
	        case NEOFORGE -> Platform.NEOFORGE;
        };
    }

    public enum Platform {
        FABRIC,
        NEOFORGE,
        QUILT;

        public boolean isFabric() {
            return this == FABRIC;
        }

        public boolean isNeoForge() {
            return this == NEOFORGE;
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
                case NEOFORGE -> "NeoForge";
                case QUILT -> "Quilt";
            };
        }
    }
}
