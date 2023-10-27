package io.github.jamalam360.jamlib.config.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.platform.Platform;
import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.config.*;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
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
			if (this.changedFields.size() > 0) {
				this.manager.save();
			}

			Objects.requireNonNull(this.minecraft).setScreen(this.parent);
		}).pos(this.width / 2 + 4, this.height - 28).size(150, 20).build());

		this.addRenderableWidget(
				new ButtonWithTextureWidget(
						7, 7, 20, 20, Component.translatable("config.jamlib.edit_manually"), new ResourceLocation("textures/item/writable_book.png"), 16, 16,
						button -> {
							if (this.changedFields.size() > 0) {
								this.manager.save();
							}

							Util.getPlatform().openFile(Platform.getConfigFolder().resolve(this.manager.getConfigName() + ".json5").toFile());
							Objects.requireNonNull(this.minecraft).setScreen(this.parent);
						}
				)
		);

		ConfigEntryList list = new ConfigEntryList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);

		if (this.entries.size() == 0) {
			for (Field field : this.manager.getConfigClass().getDeclaredFields()) {
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
				this.addRenderableWidget(
						new ButtonWithTextureWidget(
								this.width - 30 - (28 * i), 5, 20, 20, (MutableComponent) link.getTooltip(), link.getTexture(), 16, 16,
								button -> Util.getPlatform().openUrl(link.getUrl())
						)
				);
			}
		}
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		super.render(graphics, mouseX, mouseY, delta);
		graphics.drawCenteredString(Minecraft.getInstance().font, this.title, this.width / 2, 12, 0xFFFFFF);
	}

	@Override
	public void renderBackground(GuiGraphics graphics, int i, int j, float f) {
		this.renderDirtBackground(graphics);
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

	/**
	 * A copy of {@link ImageWidget}{@code .Texture} that allows you to pass in an x and y value
	 */
	private static class TextureWidget extends AbstractWidget {
		private ResourceLocation texture;

		public TextureWidget(int x, int y, int width, int height, ResourceLocation texture) {
			super(x, y, width, height, CommonComponents.EMPTY);
			this.texture = texture;
		}

		@Override
		public boolean isMouseOver(double d, double e) {
			return this.visible && d >= (double) this.getX() && e >= (double) this.getY() && d < (double) (this.getX() + this.width) && e < (double) (this.getY() + this.height);
		}

		public void setTexture(ResourceLocation texture) {
			this.texture = texture;
		}

		@Override
		public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
			graphics.blit(this.texture, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0.0F, 0.0F, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput _ignored) {
		}

		@Override
		public void playDownSound(SoundManager _ignored) {
		}

		@Override
		public boolean isActive() {
			return false;
		}

		@Override
		@Nullable
		public ComponentPath nextFocusPath(FocusNavigationEvent _ignored) {
			return null;
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

			TextureWidget validationIcon = new TextureWidget(width - 212, 0, 20, 20, JamLib.id("textures/gui/validation_warning.png"));
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
					Pattern pattern = this.field.isAnnotationPresent(MatchesRegex.class) ? Pattern.compile(this.field.getAnnotation(MatchesRegex.class).value()) : null;
					widgets.add(this.createEditBox(widgets, pattern, Function.identity()));
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

			widgets.add(new ButtonWithTextureWidget(width - 30, 0, 20, 20, Component.translatable("config.jamlib.reset"), JamLib.id("textures/gui/reset.png"), 16, 16, button -> {
				this.setFieldValue(manager, this.initialValue);
				widgets.get(1).setMessage(handleUpdatesOnChange(manager, widgets, ConfigScreen.this.changedFields));

				if (widgets.get(1) instanceof EditBox box) {
					box.setValue(String.valueOf(this.initialValue));
				} else if (widgets.get(1) instanceof SliderButton slider) {
					slider.setValue(((Number) this.initialValue).doubleValue());
				}
			}));

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
			} else {
				if (value instanceof String) {
					box.setValue((String) value);
				} else {
					box.setValue(value.toString());
				}
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

			SliderButton slider = new SliderButton(
					width - 188,
					0,
					150,
					20,
					CommonComponents.EMPTY,
					rangeAnnot.min(),
					rangeAnnot.max(),
					this.<Number>getFieldValue(manager).doubleValue(),
					s -> {
						this.setFieldValue(manager, (Number) s.getValue());
						s.setMessage(handleUpdatesOnChange(manager, widgets, ConfigScreen.this.changedFields));
					}
			);

			slider.setMessage(Component.literal(DECIMAL_FORMAT.format(slider.getValue())));

			return slider;
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
			} else if (newValue instanceof Enum enumValue) {
				return getEnumComponent(manager, this.field, enumValue);
			} else if (newValue instanceof Boolean boolValue) {
				return getBooleanComponent(boolValue);
			} else  {
				return Component.literal(newValue.toString());
			}
		}

		@SuppressWarnings("unchecked")
		private void validate(ConfigManager<T> manager, List<AbstractWidget> widgets) {
			Object newValue = this.getFieldValue(manager);

			if (manager.get() instanceof ConfigExtensions<?> ext) {
				List<ConfigExtensions.ValidationError> errors = ((ConfigExtensions<T>) ext).getValidationErrors(manager, new ConfigExtensions.FieldValidationInfo(this.field.getName(), newValue, this.initialValue, this.field));
				errors.sort((o1, o2) -> o2.type().ordinal() - o1.type().ordinal());

				TextureWidget validationIcon = (TextureWidget) widgets.get(0);
				if (errors.size() > 0) {
					this.isValid = errors.get(0).type() != ConfigExtensions.ValidationError.Type.ERROR;
					validationIcon.visible = true;
					validationIcon.setTexture(errors.get(0).type().getTexture());
					validationIcon.setTooltip(Tooltip.create(errors.get(0).message()));
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
		private <V> V getFieldValue(ConfigManager<T> manager) {
			try {
				return (V) this.field.get(manager.get());
			} catch (IllegalAccessException e) {
				JamLib.LOGGER.error("Failed to access field for config " + manager.getConfigName(), e);
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
				JamLib.LOGGER.error("Failed to access field for config " + manager.getConfigName(), e);
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
				} else if (c == java.util.List.class) {
					return LIST;
				} else {
					throw new IllegalArgumentException("Unsupported config type: " + c);
				}
			}
		}
	}

	private class ConfigEntryList extends SelectionList {
		public ConfigEntryList(Minecraft minecraft, int i, int j, int k, int l, int m) {
			super(minecraft, i, j, k, l, m);
		}

		protected void addEntry(GuiEntry entry) {
			this.addEntry(new SelectionListEntry(entry.getName(), entry.getTooltip(), entry.createWidget(ConfigScreen.this.manager, this.width)));
		}
	}

	/**
	 * Had to be copied from {@link AbstractSliderButton} because that was too private to extend properly
	 */
	private static class SliderButton extends AbstractWidget {
		private static final ResourceLocation SLIDER_SPRITE = new ResourceLocation("widget/slider");
		private static final ResourceLocation HIGHLIGHTED_SPRITE = new ResourceLocation("widget/slider_highlighted");
		private static final ResourceLocation SLIDER_HANDLE_SPRITE = new ResourceLocation("widget/slider_handle");
		private static final ResourceLocation SLIDER_HANDLE_HIGHLIGHTED_SPRITE = new ResourceLocation("widget/slider_handle_highlighted");
		private final double min;
		private final double max;
		private final Consumer<SliderButton> onChange;
		protected double value;
		private boolean canChangeValue;

		public SliderButton(int x, int y, int width, int height, Component message, double min, double max, double current, Consumer<SliderButton> onChange) {
			super(x, y, width, height, message);
			this.value = current;
			this.min = min;
			this.max = max;
			this.onChange = onChange;
		}

		private ResourceLocation getSprite() {
			return this.isFocused() && !this.canChangeValue ? HIGHLIGHTED_SPRITE : SLIDER_SPRITE;
		}

		private ResourceLocation getHandleSprite() {
			return !this.isHovered && !this.canChangeValue ? SLIDER_HANDLE_SPRITE : SLIDER_HANDLE_HIGHLIGHTED_SPRITE;
		}

		protected MutableComponent createNarrationMessage() {
			return Component.translatable("gui.narrate.slider", this.getMessage());
		}

		public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
			narrationElementOutput.add(NarratedElementType.TITLE, this.createNarrationMessage());

			if (this.active) {
				if (this.isFocused()) {
					narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.slider.usage.focused"));
				} else {
					narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.slider.usage.hovered"));
				}
			}
		}

		public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
			Minecraft minecraft = Minecraft.getInstance();
			guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			guiGraphics.blitSprite(this.getSprite(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
			double position = (this.value) / (this.max - this.min);
			double handleX = this.getX() + position * (this.getWidth() - 8);
			guiGraphics.blitSprite(this.getHandleSprite(), (int) handleX, this.getY(), 8, this.getHeight());
			guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
			int k = this.active ? 16777215 : 10526880;
			this.renderScrollingString(guiGraphics, minecraft.font, 2, k | Mth.ceil(this.alpha * 255.0F) << 24);
		}

		public void onClick(double d, double e) {
			this.setValueFromMouse(d);
		}

		public void setFocused(boolean bl) {
			super.setFocused(bl);

			if (!bl) {
				this.canChangeValue = false;
			} else {
				InputType inputType = Minecraft.getInstance().getLastInputType();

				if (inputType == InputType.MOUSE || inputType == InputType.KEYBOARD_TAB) {
					this.canChangeValue = true;
				}
			}
		}

		public boolean keyPressed(int i, int j, int k) {
			if (CommonInputs.selected(i)) {
				this.canChangeValue = !this.canChangeValue;
				return true;
			} else {
				if (this.canChangeValue) {
					boolean bl = i == 263;
					if (bl || i == 262) {
						float f = bl ? -1.0F : 1.0F;
						double step = (this.max - this.min) / (this.width / 8F);
						this.setValue(Mth.clamp(this.value + step * f, this.min, this.max));
						return true;
					}
				}

				return false;
			}
		}

		private void setValueFromMouse(double d) {
			double position = (d - (double) (this.getX() + 4)) / (double) (this.width - 8);
			this.setValue(Mth.clamp(position * (this.max - this.min) + this.min, this.min, this.max));
		}

		private void setValue(double d) {
			double e = this.value;
			this.value = Mth.clamp(d, this.min, this.max);
			if (e != this.value) {
				this.onChange.accept(this);
			}
		}

		protected void onDrag(double d, double e, double f, double g) {
			this.setValueFromMouse(d);
			super.onDrag(d, e, f, g);
		}

		@Override
		public void playDownSound(SoundManager soundManager) {
		}

		@Override
		public void onRelease(double d, double e) {
			super.playDownSound(Minecraft.getInstance().getSoundManager());
		}

		protected double getValue() {
			return this.value;
		}
	}

	private static class EnumButton<E extends Enum<E>> extends Button {
		private final Class<E> enumClass;
		private final Consumer<EnumButton<E>> onChange;
		private int index;

		@SuppressWarnings("unchecked")
		protected EnumButton(int x, int y, int width, int height, MutableComponent description, Class<Enum<?>> enumClass, Consumer<EnumButton<E>> onChange) {
			super(x, y, width, height, CommonComponents.EMPTY, b -> {
				((EnumButton<E>) b).setIndex((((EnumButton<E>) b).index + 1) % ((EnumButton<E>) b).enumClass.getEnumConstants().length);
				((EnumButton<E>) b).onChange.accept(((EnumButton<E>) b));
			}, s -> description);
			this.enumClass = (Class<E>) enumClass;
			this.onChange = onChange;
			this.index = 0;
		}

		private E getValue() {
			return this.enumClass.getEnumConstants()[this.index];
		}

		private void setValue(E value) {
			this.setIndex(value.ordinal());
		}

		private void setIndex(int index) {
			this.index = index;
		}
	}
}
