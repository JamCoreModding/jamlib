package io.github.jamalam360.jamlib.api.platform;

/**
 * A mod loader.
 */
public enum ModLoader {
	FABRIC,
	QUILT,
	FORGE,
	NEOFORGE;

	public boolean isFabric() {
		return this == FABRIC;
	}

	public boolean isQuilt() {
		return this == QUILT;
	}

	public boolean isForge() {
		return this == FORGE;
	}

	public boolean isNeoForge() {
		return this == NEOFORGE;
	}

	public boolean isFabricLike() {
		return this.isFabric() || this.isQuilt();
	}

	public boolean isForgeLike() {
		return this.isForge() || this.isNeoForge();
	}

	@Override
	public String toString() {
		return switch (this) {
			case FABRIC -> "Fabric";
			case QUILT -> "Quilt";
			case FORGE -> "Forge";
			case NEOFORGE -> "NeoForge";
		};
	}
}
