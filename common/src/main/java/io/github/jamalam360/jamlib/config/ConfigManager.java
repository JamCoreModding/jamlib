package io.github.jamalam360.jamlib.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonObject;
import dev.architectury.platform.Platform;
import io.github.jamalam360.jamlib.JamLib;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

/**
 * Manages config files.
 *
 * @param <T> The config class.
 *
 * @see RequiresRestart
 */
public class ConfigManager<T> {

    @ApiStatus.Internal
    public static final Map<String, ConfigManager<?>> MANAGERS = new HashMap<>();
    private static final Jankson JANKSON = Jankson.builder().build();
    private final Path configPath;
    private final String modId;
    private final String configName;
    private final Class<T> configClass;
    private T config;

    /**
     * Creates a new config manager, either saving a default config or loading an existing one if one exists. This construct assumes the config name is the same as the
     * mod ID.
     *
     * @param modId       The mod ID
     * @param configClass The config class
     */
    public ConfigManager(String modId, Class<T> configClass) {
        this(modId, modId, configClass);
    }

    /**
     * Creates a new config manager, either saving a default config or loading an existing one if one exists.
     *
     * @param modId       The mod ID
     * @param configName  The name of the config, usually the mod ID
     * @param configClass The config class
     */
    public ConfigManager(String modId, String configName, Class<T> configClass) {
        MANAGERS.put(configName, this);
        this.configPath = Platform.getConfigFolder().resolve(configName + ".json5");
        this.configName = configName;
        this.modId = modId;
        this.configClass = configClass;

        this.validateConfigClass();

        if (!Files.exists(this.configPath)) {
            this.config = this.createDefaultConfig();
            this.save();
        }

        this.reloadFromDisk();
        // There is an extra save here in-case the config schema was updated.
        this.save();
    }

    /**
     * @return The current config object.
     *
     * @apiNote Do not store this object, as it will not update when the config is reloaded.
     */
    public T get() {
        return this.config;
    }

    /**
     * @return The name of the current config, as passed to the constructor.
     */
    public String getConfigName() {
        return this.configName;
    }

    /**
     * @return The class of the current config, as passed to the constructor.
     */
    public Class<T> getConfigClass() {
        return this.configClass;
    }

    /**
     * @return The mod ID of the current config, as passed to the constructor.
     */
    public String getModId() {
        return this.modId;
    }

    /**
     * Saves the current config to the config file.
     */
    public void save() {
        File f = this.configPath.toFile();
        JsonElement json = JANKSON.toJson(this.config);
        transformJsonBeforeSave(json);
        JsonGrammar grammar = JsonGrammar.builder().bareRootObject(false).bareSpecialNumerics(false).printCommas(true).printWhitespace(true).printUnquotedKeys(true).withComments(true).build();
        String stringifiedJson = json.toJson(grammar);

        try {
            f.createNewFile();
            Files.writeString(this.configPath, stringifiedJson);
            JamLib.LOGGER.info("Updated config file at " + this.configPath);
        } catch (IOException e) {
            JamLib.LOGGER.error("Failed to write config file at " + this.configPath, e);
        }
    }

    /**
     * Reloads the config from the config file.
     */
    public void reloadFromDisk() {
        try {
            JsonObject json = JANKSON.load(Files.readAllLines(this.configPath).stream().reduce((a, b) -> a + "\n" + b).orElse(""));
            this.config = JANKSON.fromJsonCarefully(json, this.configClass);
        } catch (Exception e) {
            JamLib.LOGGER.error("Failed to read config file at " + configPath, e);
            JamLib.LOGGER.error("Resetting to defaults; a backup will be written to " + configPath + ".broken");

            try {
                Files.move(configPath, configPath.resolveSibling(configPath.getFileName() + ".broken"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e2) {
                JamLib.LOGGER.error("Failed to write backup config file at " + configPath + ".broken", e2);
            }

            this.config = this.createDefaultConfig();
        }
    }

    private void validateConfigClass() {
        T defaultConfig = this.createDefaultConfig();

        try {
            for (Field field : this.configClass.getFields()) {
                if (field.get(defaultConfig) == null) {
                    throw new RuntimeException("Config field " + field.getName() + " is null by default. Config fields cannot be null by default.");
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            JamLib.LOGGER.error("Failed to validate config class " + this.configClass.getName(), e);
        }
    }

    private T createDefaultConfig() {
        try {
            return this.configClass.getConstructor().newInstance();
        } catch (Exception e) {
            JamLib.LOGGER.error("Failed to create default config for " + this.configClass.getName(), e);
        }

        return null;
    }

    private void transformJsonBeforeSave(JsonElement e) {
        if (!(e instanceof JsonObject root)) {
            throw new IllegalArgumentException("Config must be a JSON object");
        }

        T defaultConfig = this.createDefaultConfig();
        if (defaultConfig != null) {
            attachDefaultComments(this.configClass, defaultConfig, root);
        }
    }

    private void attachDefaultComments(Class<?> clazz, Object defaults, JsonObject obj) {
        for (String key : obj.keySet()) {
            JsonElement e = obj.get(key);
            if (e instanceof JsonObject) {
                attachDefaultComments(clazz, defaults, (JsonObject) e);
            } else {
                try {
                    Field field = clazz.getField(key);
                    String currentComment = obj.getComment(key);
                    StringBuilder comment = new StringBuilder();

                    Object defaultValue = field.get(defaults);
                    if (defaultValue != null) {
                        if (defaultValue instanceof String s) {
                            defaultValue = "\\\"" + s + "\\\"";
                        }

                        comment.append("- default: ").append(defaultValue);
                    }

                    if (field.isAnnotationPresent(RequiresRestart.class)) {
                        if (!comment.isEmpty()) {
                            comment.append("\n");
                        }

                        comment.append("- requires game restart");
                    }

                    if (field.isAnnotationPresent(MatchesRegex.class)) {
                        MatchesRegex annotation = field.getAnnotation(MatchesRegex.class);

                        if (!comment.isEmpty()) {
                            comment.append("\n");
                        }

                        comment.append("- must match regex: ").append(annotation.value());
                    }

                    if (field.isAnnotationPresent(WithinRange.class)) {
                        WithinRange annotation = field.getAnnotation(WithinRange.class);

                        if (!comment.isEmpty()) {
                            comment.append("\n");
                        }

                        comment.append("- must be between ").append(annotation.min()).append(" and ").append(annotation.max());
                    }

                    if (Enum.class.isAssignableFrom(field.getType())) {
                        if (!comment.isEmpty()) {
                            comment.append("\n");
                        }

                        comment.append("- must be one of: ").append(Arrays.stream(field.getType().getEnumConstants()).map(Object::toString).reduce((a, b) -> a + ", " + b).orElse(""));
                    }

                    String newComment = (currentComment == null ? "" : currentComment) + (currentComment == null ? "" : "\n") + comment;
                    obj.setComment(key, newComment);
                } catch (NoSuchFieldException | IllegalAccessException ignored) {
                    // This isn't critical functionality, so these exceptions doesn't need logging
                }
            }
        }
    }
}
