package io.github.jamalam360.jamlib.client.config.gui.entry;

import io.github.jamalam360.jamlib.config.Slider;
import io.github.jamalam360.jamlib.config.WithinRange;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class NumberConfigEntry<T, V extends Number> extends ConfigEntry<T, V> {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
	private final Function<String, Number> parser;
	private final Pattern regex;
	@Nullable
	private SliderButton slider = null;
	@Nullable
	private EditBox editBox = null;

	public NumberConfigEntry(String modId, String configName, ConfigField<T, V> field) {
		super(modId, configName, field);

		Class<?> c = field.getElementType();

		if (c == float.class || c == Float.class) {
			this.parser = Float::parseFloat;
			this.regex = Pattern.compile("^-?\\d*\\.?\\d*$");
		} else if (c == double.class || c == Double.class) {
			this.parser = Double::parseDouble;
			this.regex = Pattern.compile("^-?\\d*\\.?\\d*$");
		} else if (c == int.class || c == Integer.class) {
			this.parser = Integer::parseInt;
			this.regex = Pattern.compile("^-?\\d*$");
		} else if (c == long.class || c == Long.class) {
			this.parser = Long::parseLong;
			this.regex = Pattern.compile("^-?\\d*$");
		} else {
			throw new IllegalArgumentException("Unsupported class for NumberConfigEntry " + c);
		}
	}

	@Override
	public List<AbstractWidget> createElementWidgets(int left, int width) {
		Number current = this.getFieldValue();

		if (this.field.isAnnotationPresent(Slider.class)) {
			WithinRange range = this.field.getAnnotation(WithinRange.class);

			if (current == null) {
				current = range.min();
			}

			this.slider = new SliderButton(
					left,
					0,
					width,
					20,
					this.getComponent(current),
					range.min(),
					range.max(),
					current.doubleValue(),
					value -> {
						//noinspection unchecked
						this.setFieldValue((V) value);
						return this.getComponent(value);
					}
			);
			return List.of(this.slider);
		} else {
			this.editBox = new EditBox(
					Minecraft.getInstance().font,
					left,
					0,
					width,
					20,
					CommonComponents.EMPTY
			);
			this.editBox.setValue(DECIMAL_FORMAT.format(current.doubleValue()));
			this.editBox.setFilter(s -> this.regex.matcher(s).matches());
			this.editBox.setResponder(s -> {
				if (!s.isEmpty()) {
					//noinspection unchecked
					this.setFieldValue((V) this.parser.apply(s));
				}
			});
			return List.of(this.editBox);
		}
	}

	private Component getComponent(Number value) {
		return Component.literal(DECIMAL_FORMAT.format(value.doubleValue()));
	}

	@Override
	public void resetToDefault() {
		super.resetToDefault();

		if (this.editBox != null) {
			this.editBox.setValue(DECIMAL_FORMAT.format(this.getFieldValue().doubleValue()));
		} else if (this.slider != null) {
			this.slider.setValue(this.getFieldValue().doubleValue());
		}
	}
}
