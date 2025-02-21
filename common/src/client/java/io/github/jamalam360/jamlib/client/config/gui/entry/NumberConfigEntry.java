package io.github.jamalam360.jamlib.client.config.gui.entry;

import io.github.jamalam360.jamlib.config.Slider;
import io.github.jamalam360.jamlib.config.WithinRange;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class NumberConfigEntry extends ConfigEntry {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
	private final Function<String, Object> parser;
	private final Pattern regex;
	@Nullable
	private EditBox editBox = null;

	public NumberConfigEntry(String modId, String configName, Field field) {
		super(modId, configName, field);

		Class<?> c = field.getType();

		if (c == float.class) {
			this.parser = Float::parseFloat;
			this.regex = Pattern.compile("^-?\\d*\\.?\\d*$");
		} else if (c == double.class) {
			this.parser = Double::parseDouble;
			this.regex = Pattern.compile("^-?\\d*\\.?\\d*$");
		} else if (c == int.class) {
			this.parser = Integer::parseInt;
			this.regex = Pattern.compile("^-?\\d*$");
		} else if (c == long.class) {
			this.parser = Long::parseLong;
			this.regex = Pattern.compile("^-?\\d*$");
		} else {
			throw new IllegalArgumentException("Unsupported class for NumberConfigEntry " + c);
		}
	}

	@Override
	public List<AbstractWidget> createElementWidgets(int width) {
		Number current = (Number) this.getFieldValue();

		if (this.field.isAnnotationPresent(Slider.class)) {
			WithinRange range = this.field.getAnnotation(WithinRange.class);

			if (current == null) {
				current = range.min();
			}

			SliderButton slider = new SliderButton(
					width - 188,
					0,
					150,
					20,
					this.getComponent(current),
					range.min(),
					range.max(),
					current.doubleValue(),
					value -> {
						this.setFieldValue(value);
						return this.getComponent(value);
					}
			);
			return List.of(slider);
		} else {
			this.editBox = new EditBox(
					Minecraft.getInstance().font,
					width - 188,
					0,
					150,
					20,
					CommonComponents.EMPTY
			);
			this.editBox.setValue(DECIMAL_FORMAT.format(current.doubleValue()));
			this.editBox.setFilter(s -> this.regex.matcher(s).matches());
			this.editBox.setResponder(s -> {
				if (!s.isEmpty()) {
					this.setFieldValue(this.parser.apply(s));
				}
			});
			return List.of(this.editBox);
		}
	}

	@Override
	public void onChange() {
		super.onChange();

		if (this.editBox != null) {
			this.editBox.setMessage(this.getComponent((Number) this.getFieldValue()));
		}
	}

	private Component getComponent(Number value) {
		return Component.literal(DECIMAL_FORMAT.format(value.doubleValue()));
	}
}
