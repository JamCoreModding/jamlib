package io.github.jamalam360.jamlib.client.config.gui;

import dev.architectury.platform.Platform;
import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.client.mixinsupport.MutableSpriteImageWidget$Sprite;
import io.github.jamalam360.jamlib.config.*;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * A screen for editing a config managed through a {@link ConfigManager}.
 */
@ApiStatus.Internal
public class ConfigScreen<T> extends Screen {

    protected final ConfigManager<T> manager;
    private final Screen parent;
    private final List<GuiEntry> entries;
    private final List<GuiEntry> changedFields;
    private Button doneButton;

    public ConfigScreen(ConfigManager<T> manager, Screen parent) {
        super(createTitle(manager));
        this.manager = manager;
        this.parent = parent;
        this.entries = new ArrayList<>();
        this.changedFields = new ArrayList<>();
    }

    protected static String createTranslationKey(String modId, String configName, String path) {
        if (modId.equals(configName)) {
            return "config." + modId + "." + path;
        } else {
            return "config." + modId + "." + configName + "." + path;
        }
    }

    protected static Component createTitle(ConfigManager<?> manager) {
        String translationKey = createTranslationKey(manager.getModId(), manager.getConfigName(), "title");

        if (I18n.exists(translationKey)) {
            return Component.translatable(translationKey);
        } else {
            return Component.literal(Platform.getMod(manager.getModId()).getName());
        }
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> {
            this.manager.reloadFromDisk();
            Objects.requireNonNull(this.minecraft).setScreen(this.parent);
        }).pos(this.width / 2 - 154, this.height - 28).size(150, 20).build());

        this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
            if (!this.changedFields.isEmpty()) {
                this.manager.save();
            }

            Objects.requireNonNull(this.minecraft).setScreen(this.parent);
        }).pos(this.width / 2 + 4, this.height - 28).size(150, 20).build());

        SpriteIconButton editManuallyButton = this.addRenderableWidget(
                SpriteIconButton.builder(Component.translatable("config.jamlib.edit_manually"), button -> {
                        if (!this.changedFields.isEmpty()) {
                            this.manager.save();
                        }

                        Util.getPlatform().openFile(Platform.getConfigFolder().resolve(this.manager.getConfigName() + ".json5").toFile());
                        Objects.requireNonNull(this.minecraft).setScreen(this.parent);
                }, true).sprite(JamLib.id("writable_book"), 16, 16).size(20, 20).build()
        );
        editManuallyButton.setX(7);
        editManuallyButton.setY(7);

        ConfigEntryList list = new ConfigEntryList(this.minecraft, this.width, this.height - 64, 32, 25);

        if (this.entries.isEmpty()) {
            for (Field field : this.manager.getConfigClass().getFields()) {
                if (field.isAnnotationPresent(HiddenInGui.class)) {
                    continue;
                }
                this.entries.add(new GuiEntry(this.manager.getModId(), this.manager.getConfigName(), field));
            }
        }

        for (GuiEntry entry : this.entries) {
            list.addEntry(entry);
        }

        this.addRenderableWidget(list);

        if (this.manager.get() instanceof ConfigExtensions<?> ext) {
            List<ConfigExtensions.Link> links = ext.getLinks();

            for (int i = 0; i < links.size(); i++) {
                ConfigExtensions.Link link = links.get(i);
                SpriteIconButton linkButton = this.addRenderableWidget(
                        SpriteIconButton.builder(link.getTooltip(), button -> {
                            try {
                                Util.getPlatform().openUri(link.getUrl().toURI());
                            } catch (Exception e) {
                                JamLib.LOGGER.error("Failed to open link", e);
                            }
                        }, true).sprite(link.getTexture(), 16, 16).size(20, 20).build()

                );
                linkButton.setX(this.width - 30 - (28 * i));
                linkButton.setY(5);
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(Minecraft.getInstance().font, this.title, this.width / 2, 12, 0xFFFFFF);
    }

    private boolean canExit() {
        return this.entries.stream().allMatch(GuiEntry::isValid);
    }

    @Override
    public void tick() {
        super.tick();
        boolean canExit = this.canExit();

        if (this.doneButton.active != canExit) {
            this.doneButton.active = canExit;
        }
    }

    private class GuiEntry {
        private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
        private final Type type;
        private final String translationKey;
        private final List<FormattedCharSequence> tooltip;
        private final Field field;
        private final Object initialValue;
        private boolean isValid = true;

        @SuppressWarnings("unchecked")
        protected GuiEntry(String modId, String configName, Field field) {
            this.type = Type.fromField(field);
            this.field = field;
            this.initialValue = this.getFieldValue((ConfigManager<T>) ConfigManager.MANAGERS.get(configName));
            this.translationKey = ConfigScreen.createTranslationKey(modId, configName, field.getName());

            String tooltipTranslationKey = this.translationKey + ".tooltip";

            if (I18n.exists(tooltipTranslationKey)) {
                this.tooltip = Minecraft.getInstance().font.split(Component.translatable(tooltipTranslationKey), 200);
            } else {
                this.tooltip = null;
            }
        }

        private static Component getBooleanComponent(boolean v) {
            return Component.literal(v ? "Yes" : "No").withStyle(s -> s.withColor(v ? ChatFormatting.GREEN : ChatFormatting.RED));
        }

        private static Component getEnumComponent(ConfigManager<?> manager, Field field, Enum<?> enumValue) {
            String translationKey = ConfigScreen.createTranslationKey(manager.getModId(), manager.getConfigName(), field.getName() + "." + enumValue.name().toLowerCase());

            if (I18n.exists(translationKey)) {
                return Component.translatable(translationKey);
            } else {
                return Component.literal(enumValue.name());
            }
        }

        protected java.util.List<AbstractWidget> createWidget(ConfigManager<T> manager, int width) {
            java.util.List<AbstractWidget> widgets = new ArrayList<>();

            ImageWidget validationIcon = ImageWidget.sprite(20, 20, JamLib.id("validation_warning"));
            validationIcon.setX(width - 212);
            validationIcon.setY(0);
            validationIcon.setTooltip(Tooltip.create(Component.translatable("config.jamlib.requires_restart_tooltip")));
            validationIcon.visible = false;
            widgets.add(validationIcon);

            switch (this.type) {
                case BOOLEAN:
                    widgets.add(Button.builder(getBooleanComponent(Boolean.TRUE.equals(getFieldValue(manager))), (button) -> {
                        this.setFieldValue(manager, !(Boolean.TRUE.equals(this.getFieldValue(manager))));
                        button.setMessage(handleUpdatesOnChange(manager, widgets, ConfigScreen.this.changedFields));
                    }).pos(width - 188, 0).size(150, 20).build());
                    break;
                case FLOAT:
                    if (this.field.isAnnotationPresent(Slider.class)) {
                        widgets.add(this.createSlider(widgets));
                    } else {
                        widgets.add(this.createEditBox(widgets, Pattern.compile("^-?\\d*\\.?\\d*$"), Float::parseFloat));
                    }

                    break;
                case DOUBLE:
                    if (this.field.isAnnotationPresent(Slider.class)) {
                        widgets.add(this.createSlider(widgets));
                    } else {
                        widgets.add(this.createEditBox(widgets, Pattern.compile("^-?\\d*\\.?\\d*$"), Double::parseDouble));
                    }

                    break;
                case INTEGER:
                    if (this.field.isAnnotationPresent(Slider.class)) {
                        widgets.add(this.createSlider(widgets));
                    } else {
                        widgets.add(this.createEditBox(widgets, Pattern.compile("^-?\\d*$"), Integer::parseInt));
                    }

                    break;
                case LONG:
                    if (this.field.isAnnotationPresent(Slider.class)) {
                        widgets.add(this.createSlider(widgets));
                    } else {
                        widgets.add(this.createEditBox(widgets, Pattern.compile("^-?\\d*$"), Long::parseLong));
                    }

                    break;
                case STRING:
                    widgets.add(this.createEditBox(widgets, null, Function.identity()));
                    break;
                case ENUM:
                    @SuppressWarnings("unchecked") EnumButton<?> button = new EnumButton<>(
                          width - 188,
                          0,
                          150,
                          20,
                          CommonComponents.EMPTY.copy(),
                          (Class<Enum<?>>) this.field.getType(),
                          (b) -> {
                              this.setFieldValue(manager, b.getValue());
                              b.setMessage(handleUpdatesOnChange(manager, widgets, ConfigScreen.this.changedFields));
                          }
                    );

                    button.setValue(Objects.requireNonNull(this.getFieldValue(manager)));
                    button.setMessage(getEnumComponent(manager, this.field, button.getValue()));
                    widgets.add(button);
                    break;
                case LIST:
                    break;
            }

            SpriteIconButton resetButton = SpriteIconButton.builder(Component.translatable("config.jamlib.reset"), (button) -> {
                this.setFieldValue(manager, this.initialValue);
                widgets.get(1).setMessage(handleUpdatesOnChange(manager, widgets, ConfigScreen.this.changedFields));

                if (widgets.get(1) instanceof EditBox box) {
                    box.setValue(String.valueOf(this.initialValue));
                } else if (widgets.get(1) instanceof SliderButton slider) {
                    slider.setValue(((Number) this.initialValue).doubleValue());
                }
            }, true).sprite(JamLib.id("reset"), 16, 16).size(20, 20).build();
            resetButton.setX(width - 30);
            resetButton.setY(0);
            widgets.add(resetButton);

            validate(manager, widgets);

            return widgets;
        }

        private <V> EditBox createEditBox(List<AbstractWidget> widgets, Pattern filter, Function<String, V> parse) {
            EditBox box = new EditBox(
                  Minecraft.getInstance().font,
                  width - 188,
                  0,
                  150,
                  20,
                  CommonComponents.EMPTY
            );

            Object value = this.getFieldValue(manager);

            if (value instanceof Number number) {
                box.setValue(DECIMAL_FORMAT.format(number.doubleValue()));
            } else if (value instanceof String string) {
                box.setValue(string);
            } else if (value != null) {
                box.setValue(value.toString());
            } else {
                box.setValue("");
            }

            if (filter != null) {
                box.setFilter(s -> filter.matcher(s).matches());
            }

            box.setResponder(s -> {
                try {
                    this.setFieldValue(manager, parse.apply(s));
                } catch (Exception ignored) {
                }

                box.setMessage(handleUpdatesOnChange(manager, widgets, ConfigScreen.this.changedFields));
            });

            return box;
        }

        private SliderButton createSlider(List<AbstractWidget> widgets) {
            WithinRange rangeAnnot = this.field.getAnnotation(WithinRange.class);
            Number current = this.getFieldValue(manager);

            if (current == null) {
                current = rangeAnnot.min();
            }

	        return new SliderButton(
	              width - 188,
	              0,
	              150,
	              20,
                    Component.literal(DECIMAL_FORMAT.format(current.doubleValue())),
	              rangeAnnot.min(),
	              rangeAnnot.max(),
	              current.doubleValue(),
	                value -> {
	                    this.setFieldValue(manager, (Number) value);
	                    return handleUpdatesOnChange(manager, widgets, ConfigScreen.this.changedFields);
	              }
	        );
        }

        private Component handleUpdatesOnChange(ConfigManager<T> manager, List<AbstractWidget> widgets, List<GuiEntry> changedFields) {
            Object newValue = this.getFieldValue(manager);

            if (changedFields.contains(this) && this.initialValue.equals(newValue)) {
                changedFields.remove(this);
            } else if (!changedFields.contains(this) && !this.initialValue.equals(newValue)) {
                changedFields.add(this);
            }

            this.validate(manager, widgets);

            Class<?> c = this.field.getType();

            if (c == boolean.class) {
                return getBooleanComponent(Boolean.TRUE.equals(newValue));
            }

            if (newValue instanceof Number number) {
                return Component.literal(DECIMAL_FORMAT.format(number.doubleValue()));
            } else if (newValue instanceof Enum<?> enumValue) {
                return getEnumComponent(manager, this.field, enumValue);
            } else if (newValue instanceof Boolean boolValue) {
                return getBooleanComponent(boolValue);
            } else if (newValue != null) {
                return Component.literal(newValue.toString());
            } else {
                return Component.literal("");
            }
        }

        @SuppressWarnings("unchecked")
        private void validate(ConfigManager<T> manager, List<AbstractWidget> widgets) {
            Object newValue = this.getFieldValue(manager);

            if (manager.get() instanceof ConfigExtensions<?> ext) {
                List<ConfigExtensions.ValidationError> errors = ((ConfigExtensions<T>) ext).getValidationErrors(manager, new ConfigExtensions.FieldValidationInfo(this.field.getName(), newValue, this.initialValue, this.field));
                errors.sort((o1, o2) -> o2.type().ordinal() - o1.type().ordinal());

                ImageWidget validationIcon = (ImageWidget) widgets.getFirst();
                if (!errors.isEmpty()) {
                    this.isValid = errors.getFirst().type() != ConfigExtensions.ValidationError.Type.ERROR;
                    validationIcon.visible = true;

                    ((MutableSpriteImageWidget$Sprite) validationIcon).setSprite(errors.getFirst().type().getTexture());
                    validationIcon.setTooltip(Tooltip.create(errors.getFirst().message()));
                } else {
                    this.isValid = true;
                    validationIcon.visible = false;
                }
            }
        }

        protected Component getName() {
            return Component.translatable(this.translationKey);
        }

        protected List<FormattedCharSequence> getTooltip() {
            return this.tooltip;
        }

        protected boolean isValid() {
            return this.isValid;
        }

        @SuppressWarnings("unchecked")
        @Nullable
        private <V> V getFieldValue(ConfigManager<T> manager) {
            try {
                return (V) this.field.get(manager.get());
            } catch (IllegalAccessException e) {
                JamLib.LOGGER.error("Failed to access field for config {}", manager.getConfigName(), e);
                return null;
            }
        }

        private <V> void setFieldValue(ConfigManager<T> manager, V v) {
            Object realValue = v;

            if (v instanceof Number n) {
                Class<?> c = this.field.getType();

                if (c == double.class || c == Double.class) {
                    realValue = n.doubleValue();
                } else if (c == float.class || c == Float.class) {
                    realValue = n.floatValue();
                } else if (c == int.class || c == Integer.class) {
                    realValue = n.intValue();
                } else if (c == long.class || c == Long.class) {
                    realValue = n.longValue();
                }
            }

            try {
                this.field.set(manager.get(), realValue);
            } catch (IllegalAccessException e) {
                JamLib.LOGGER.error("Failed to access field for config {}", manager.getConfigName(), e);
            }
        }

        private enum Type {
            BOOLEAN,
            FLOAT,
            DOUBLE,
            INTEGER,
            LONG,
            STRING,
            ENUM,
            LIST;

            private static Type fromField(Field field) {
                Class<?> c = field.getType();

                if (c == boolean.class) {
                    return BOOLEAN;
                } else if (c == float.class) {
                    return FLOAT;
                } else if (c == double.class) {
                    return DOUBLE;
                } else if (c == int.class) {
                    return INTEGER;
                } else if (c == long.class) {
                    return LONG;
                } else if (c == String.class) {
                    return STRING;
                } else if (c.isEnum()) {
                    return ENUM;
                } else if (java.util.List.class.isAssignableFrom(c)) {
                    return LIST;
                } else {
                    throw new IllegalArgumentException("Unsupported config type: " + c);
                }
            }
        }
    }

    private class ConfigEntryList extends SelectionList {
        public ConfigEntryList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
            super(minecraft, width, height, y, itemHeight);
        }

        protected void addEntry(GuiEntry entry) {
            this.addEntry(new SelectionListEntry(entry.getName(), entry.getTooltip(), entry.createWidget(ConfigScreen.this.manager, this.width)));
        }
    }
}
