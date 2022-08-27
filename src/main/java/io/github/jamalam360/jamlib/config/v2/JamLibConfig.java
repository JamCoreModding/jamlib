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

package io.github.jamalam360.jamlib.config.v2;

import io.github.jamalam360.jamlib.JamLib;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.quiltmc.json5.JsonReader;
import org.quiltmc.json5.JsonToken;
import org.quiltmc.json5.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Jamalam
 */
public class JamLibConfig {
    private static final Map<Class<?>, JamLibConfig> CONFIGS = new java.util.HashMap<>();
    protected final Class<?> config;
    protected final List<ConfigEntry> entries = new ArrayList<>();
    protected final Map<Class<?>, List<ConfigEntry>> nestedClassEntries = new HashMap<>();
    private final String modId;
    private final Path configFile;

    private JamLibConfig(Class<?> clazz, String modId) {
        this.config = clazz;
        this.modId = modId;
        this.configFile = FabricLoader.getInstance().getConfigDir().resolve(modId + ".json5");
        this.populateEntries();
    }

    public static void register(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Config.class)) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not annotated with @Config");
        }

        Config config = clazz.getAnnotation(Config.class);
        String modId = config.value();

        CONFIGS.put(clazz, new JamLibConfig(clazz, modId));
        JamLib.LOGGER.info("Registered config for mod " + modId);
    }

    public static void writeToFile(String modId) {
        Optional<Map.Entry<Class<?>, JamLibConfig>> config = CONFIGS.entrySet().stream().filter((e) -> e.getValue().modId.equals(modId)).findFirst();

        if (config.isEmpty()) {
            throw new IllegalArgumentException("No config registered for mod " + modId);
        }

        config.get().getValue().writeToFile();
    }

    protected static JamLibConfig getConfig(String modId) {
        Optional<Map.Entry<Class<?>, JamLibConfig>> config = CONFIGS.entrySet().stream().filter((e) -> e.getValue().modId.equals(modId)).findFirst();

        if (config.isEmpty()) {
            throw new IllegalArgumentException("No config registered for mod " + modId);
        }

        return config.get().getValue();
    }

    private static Map<String, Object> parseObject(JsonReader reader) throws IOException {
        reader.beginObject();

        Map<String, Object> object = new LinkedHashMap<>();

        while (reader.hasNext() && reader.peek() == JsonToken.NAME) {
            object.put(reader.nextName(), parseElement(reader));
        }

        reader.endObject();

        return object;
    }

    private static List<Object> parseArray(JsonReader reader) throws IOException {
        reader.beginArray();

        List<Object> array = new ArrayList<>();

        while (reader.hasNext() && reader.peek() != JsonToken.END_ARRAY) {
            array.add(parseElement(reader));
        }

        reader.endArray();

        return array;
    }

    private static Object parseElement(JsonReader reader) throws IOException {
        switch (reader.peek()) {
            case END_ARRAY:
                throw new IllegalStateException("Unexpected end of array");
            case BEGIN_OBJECT:
                return parseObject(reader);
            case BEGIN_ARRAY:
                return parseArray(reader);
            case END_OBJECT:
                throw new IllegalStateException("Unexpected end of object");
            case NAME:
                throw new IllegalStateException("Unexpected name");
            case STRING:
                return reader.nextString();
            case NUMBER:
                return reader.nextNumber();
            case BOOLEAN:
                return reader.nextBoolean();
            case NULL:
                reader.nextNull();
                return null;
            case END_DOCUMENT:
                throw new IllegalStateException("Unexpected end of file");
        }

        throw new IllegalStateException("Encountered unknown JSON token");
    }

    private void populateEntries() {
        if (this.entries.size() > 0) {
            throw new IllegalStateException("Config entries already populated");
        }

        entries.addAll(getEntries(this.config));
        nestedClassEntries.putAll(getNestedConfigs(this.config));

        try {
            this.readFromFile();
        } catch (IOException e) {
            JamLib.LOGGER.info("Could not read config file for mod " + this.modId + "; creating new config");
            this.writeToFile();
        }
    }

    private Map<Class<?>, List<ConfigEntry>> getNestedConfigs(Class<?> clazz) {
        Map<Class<?>, List<ConfigEntry>> map = new HashMap<>();

        for (Class<?> nested : clazz.getDeclaredClasses()) {
            if (nested.isAnnotationPresent(Excluded.class)) continue;
            if (!Modifier.isStatic(nested.getModifiers())) continue;
            if (!nested.isAnnotationPresent(NestedConfig.class)) continue;

            map.put(nested, getEntries(nested));
            map.putAll(getNestedConfigs(nested));
        }

        return map;
    }

    private ArrayList<ConfigEntry> getEntries(Class<?> clazz) {
        ArrayList<ConfigEntry> l = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Excluded.class)) continue;
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (Modifier.isFinal(field.getModifiers())) continue;
            if (!field.canAccess(null)) continue;

            Object value;
            try {
                value = field.get(null);
            } catch (IllegalAccessException e) {
                JamLib.LOGGER.error("Could not access field " + field.getName() + " in config " + this.modId);
                continue;
            }

            l.add(new ConfigEntry(field, value, value));
        }

        return l;
    }

    protected void readFromFile() throws IOException {
        try (JsonReader reader = JsonReader.json5(this.configFile)) {
            Map<String, Object> values = parseObject(reader);
            boolean mustRewrite = false;

            for (ConfigEntry entry : this.entries) {
                String fieldName = entry.getField().getName();
                Object value = values.get(fieldName);

                if (readEntry(value, entry)) {
                    mustRewrite = true;
                }
            }

            if (readNestedClasses(this.config, values)) {
                mustRewrite = true;
            }

            if (mustRewrite) {
                this.writeToFile();
            }
        }

        updateEntries(this.config);
    }

    protected void updateEntries(Class<?> clazz) {
        List<ConfigEntry> entries;

        if (clazz == this.config) {
            entries = this.entries;
        } else {
            entries = this.nestedClassEntries.get(clazz);
        }

        for (ConfigEntry entry : entries) {
            try {
                entry.getField().set(null, entry.getValue());
            } catch (IllegalAccessException e) {
                JamLib.LOGGER.error("Could not access field " + entry.getField().getName() + " in config " + this.modId);
            }
        }

        this.nestedClassEntries.entrySet().stream().filter((e) -> e.getKey().getDeclaringClass() == clazz).forEach((e) -> this.updateEntries(e.getKey()));
    }

    @SuppressWarnings("unchecked")
    private boolean readNestedClasses(Class<?> parent, Map<String, Object> values) {
        boolean mustRewrite = false;

        for (Class<?> nestedClass : parent.getDeclaredClasses()) {
            if (this.nestedClassEntries.containsKey(nestedClass)) {
                List<ConfigEntry> nestedEntries = this.nestedClassEntries.get(nestedClass);
                NestedConfig nestedConfig = nestedClass.getAnnotation(NestedConfig.class);
                Map<String, Object> nestedValues = (Map<String, Object>) values.get(nestedConfig.value());

                if (nestedValues == null) {
                    JamLib.LOGGER.warn("Could not find value for nested config " + nestedConfig.value() + " in config " + this.modId);
                    mustRewrite = true;
                    continue;
                }

                for (ConfigEntry nestedEntry : nestedEntries) {
                    String fieldName = nestedEntry.getField().getName();
                    Object value = nestedValues.get(fieldName);

                    if (readEntry(value, nestedEntry)) {
                        mustRewrite = true;
                    }
                }

                if (readNestedClasses(nestedClass, nestedValues)) {
                    mustRewrite = true;
                }
            }
        }

        return mustRewrite;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean readEntry(Object value, ConfigEntry entry) {
        String fieldName = entry.getField().getName();

        if (value == null) {
            JamLib.LOGGER.warn("Could not find value for field " + fieldName + " in config " + this.modId);
            return true;
        }

        if (!entry.getField().getType().isAssignableFrom(value.getClass()) && !entry.getField().getType().isPrimitive()) {
            if (value instanceof String && entry.getField().getType() == Identifier.class) {
                value = new Identifier((String) value);
            } else if (value instanceof String && entry.getField().getType().isEnum()) {
                value = Enum.valueOf((Class<Enum>) entry.getField().getType(), (String) value);
            } else {
                JamLib.LOGGER.warn("Value for field " + fieldName + " in config " + this.modId + " is of type " + value.getClass().getName() + " but field is of type " + entry.getField().getType().getName());
                return true;
            }
        } else if (!entry.getField().getType().isAssignableFrom(value.getClass()) && entry.getField().getType().isPrimitive()) {
            // Convert BigDecimal to primitive types
            if (value instanceof BigDecimal bd) {
                if (entry.getField().getType() == int.class) {
                    value = bd.intValue();
                } else if (entry.getField().getType() == long.class) {
                    value = bd.longValue();
                } else if (entry.getField().getType() == float.class) {
                    value = bd.floatValue();
                } else if (entry.getField().getType() == double.class) {
                    value = bd.doubleValue();
                } else if (entry.getField().getType() == short.class) {
                    value = bd.shortValue();
                } else if (entry.getField().getType() == byte.class) {
                    value = bd.byteValue();
                } else {
                    JamLib.LOGGER.warn("Value for field " + fieldName + " in config " + this.modId + " is of type " + value.getClass().getName() + " but field is of type " + entry.getField().getType().getName());
                    return true;
                }
            }
        }

        entry.setValue(value);
        return false;
    }

    private void writeToFile() {
        try {
            if (!Files.exists(this.configFile)) {
                Files.createFile(this.configFile);
            }

            try (JsonWriter writer = JsonWriter.json5(this.configFile)) {
                writer.comment("** Config file for mod " + this.modId + " **");
                writer.comment("** Managed by JamLib **");
                writer.comment("** This file can be edited both manually and in-game **");

                if (this.config.isAnnotationPresent(Description.class)) {
                    Description description = this.config.getAnnotation(Description.class);
                    writer.comment(description.value());
                }

                writer.beginObject();

                for (ConfigEntry entry : this.entries) {
                    System.out.println("Writing entry " + entry.getField().getName() + " with value " + entry.getValue());
                    writeEntry(entry, writer);
                }

                this.nestedClassEntries.entrySet().stream().filter((e) -> e.getKey().getDeclaringClass() == this.config).forEach((e) -> {
                    try {
                        writeNestedClass(e.getKey(), writer);
                    } catch (IOException err) {
                        JamLib.LOGGER.error("Could not write nested config for class " + this.config.getName());
                        err.printStackTrace();
                    }
                });

                writer.endObject();
            }
        } catch (IOException e) {
            JamLib.LOGGER.error("Could not write config file for mod " + this.modId);
            e.printStackTrace();
        }
    }

    private void writeNestedClass(Class<?> nestedClass, JsonWriter writer) throws IOException {
        if (!this.nestedClassEntries.containsKey(nestedClass)) return;
        if (!nestedClass.isAnnotationPresent(NestedConfig.class)) return;

        if (nestedClass.isAnnotationPresent(Description.class)) {
            Description description = nestedClass.getAnnotation(Description.class);
            writer.comment(description.value());
        }

        NestedConfig nestedConfig = nestedClass.getAnnotation(NestedConfig.class);
        writer.name(nestedConfig.value());
        writer.beginObject();

        for (ConfigEntry nestedEntry : this.nestedClassEntries.get(nestedClass)) {
            writeEntry(nestedEntry, writer);
        }

        this.nestedClassEntries.entrySet().stream().filter((e) -> e.getKey().getDeclaringClass() == nestedClass).forEach((e) -> {
            try {
                writeNestedClass(e.getKey(), writer);
            } catch (IOException err) {
                JamLib.LOGGER.error("Could not write nested config for class " + nestedClass.getName());
                err.printStackTrace();
            }
        });

        writer.endObject();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void setEntryWithConverters(ConfigEntry entry, Object value) {
        if (!entry.getField().getType().isAssignableFrom(value.getClass()) && !entry.getField().getType().isPrimitive()) {
            if (value instanceof String && entry.getField().getType() == Identifier.class) {
                value = new Identifier((String) value);
            } else if (value instanceof String && entry.getField().getType().isEnum()) {
                value = Enum.valueOf((Class<Enum>) entry.getField().getType(), (String) value);
            } else {
                JamLib.LOGGER.warn("Value for field " + entry.getField().getName() + " in config " + this.modId + " is of type " + value.getClass().getName() + " but field is of type " + entry.getField().getType().getName());
                return;
            }
        } else if (!entry.getField().getType().isAssignableFrom(value.getClass()) && entry.getField().getType().isPrimitive()) {
            // Convert BigDecimal to primitive types
            if (value instanceof BigDecimal bd) {
                if (entry.getField().getType() == int.class) {
                    value = bd.intValue();
                } else if (entry.getField().getType() == long.class) {
                    value = bd.longValue();
                } else if (entry.getField().getType() == float.class) {
                    value = bd.floatValue();
                } else if (entry.getField().getType() == double.class) {
                    value = bd.doubleValue();
                } else if (entry.getField().getType() == short.class) {
                    value = bd.shortValue();
                } else if (entry.getField().getType() == byte.class) {
                    value = bd.byteValue();
                } else {
                    JamLib.LOGGER.warn("Value for field " + entry.getField().getName() + " in config " + this.modId + " is of type " + value.getClass().getName() + " but field is of type " + entry.getField().getType().getName());
                    return;
                }
            }
        }

        entry.setValue(value);
    }

    private void writeEntry(ConfigEntry entry, JsonWriter writer) throws IOException {
        if (entry.getField().isAnnotationPresent(Excluded.class)) return;

        if (entry.getField().isAnnotationPresent(Description.class)) {
            Description description = entry.getField().getAnnotation(Description.class);
            writer.comment(description.value());
        }

        writer.comment("Default value: " + entry.getDefaultValue());

        if (entry.getValue().getClass().isEnum()) {
            StringBuilder opts = new StringBuilder("Options: ");

            for (Object obj : entry.getField().getType().getEnumConstants()) {
                opts.append(obj.toString()).append(", ");
            }

            opts.delete(opts.length() - 2, opts.length());

            writer.comment(opts.toString());
        }

        writer.name(entry.getField().getName());
        write(writer, entry.getValue());
    }

    private void write(JsonWriter writer, Object value) throws IOException {
        if (value instanceof Integer) {
            writer.value((Integer) value);
        } else if (value instanceof Long) {
            writer.value((Long) value);
        } else if (value instanceof Float) {
            writer.value((Float) value);
        } else if (value instanceof Double) {
            writer.value((Double) value);
        } else if (value instanceof BigDecimal) {
            writer.value((BigDecimal) value);
        } else if (value instanceof Boolean) {
            writer.value((Boolean) value);
        } else if (value instanceof String) {
            writer.value((String) value);
        } else if (value instanceof Identifier) {
            writer.value(value.toString());
        } else if (value instanceof List) {
            writer.beginArray();
            for (Object item : (List<?>) value) {
                this.write(writer, item);
            }
            writer.endArray();
        } else if (value instanceof Map) {
            writer.beginObject();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                writer.name(entry.getKey().toString());
                this.write(writer, entry.getValue());
            }
            writer.endObject();
        } else if (value == null) {
            writer.nullValue();
        } else if (value.getClass().isEnum()) {
            writer.value(((Enum<?>) value).name());
            // nested classes
        } else if (value.getClass() == Object.class) {
            for (Field f : value.getClass().getDeclaredFields()) {
                if (f.isAnnotationPresent(Excluded.class)) continue;
                if (!Modifier.isStatic(f.getModifiers())) continue;
                if (Modifier.isFinal(f.getModifiers())) continue;
                if (!f.canAccess(null)) continue;
                try {
                    Object fieldValue = f.get(value);
                    writer.name(f.getName());
                    this.write(writer, fieldValue);
                } catch (IllegalAccessException e) {
                    JamLib.LOGGER.error("Could not access field " + f.getName() + " in config " + this.modId);
                }
            }
        } else {
            throw new IllegalArgumentException("Cannot serialize value of type " + value.getClass().getName());
        }
    }
}
