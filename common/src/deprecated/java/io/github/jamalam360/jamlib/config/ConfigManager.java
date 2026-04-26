package io.github.jamalam360.jamlib.config;

@Deprecated(forRemoval = true)
public class ConfigManager<T> {
	private final io.github.jamalam360.jamlib.api.config.ConfigManager<T> delegate;

	public ConfigManager(String modId, Class<T> configClass) {
		this(modId, modId, configClass);
	}

	public ConfigManager(String modId, String configName, Class<T> configClass) {
		this.delegate = new io.github.jamalam360.jamlib.api.config.ConfigManager<>(modId, configName, configClass);
	}

	public T get() {
		return this.delegate.get();
	}

	public String getConfigName() {
		return this.delegate.getConfigName();
	}

	public Class<T> getConfigClass() {
		return this.delegate.getConfigClass();
	}

	public String getModId() {
		return this.delegate.getModId();
	}

	public void save() {
		this.delegate.save();
	}

	public void reloadFromDisk() {
		this.delegate.reloadFromDisk();
	}

	public record Key(String modId, String configName) {
	}
}
