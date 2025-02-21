package io.github.jamalam360.jamlib.client.config.gui.entry;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Function;

public class SliderButton extends AbstractSliderButton {
	private final double min;
	private final double max;
	private final Function<Double, Component> onChange;

	protected SliderButton(int x, int y, int width, int height, Component message, double min, double max, double value, Function<Double, Component> onChange) {
		super(x, y, width, height, message, value);
		this.min = min;
		this.max = max;
		this.onChange = onChange;
		this.value = ((Mth.clamp((float) value, this.min, this.max) - this.min) / (this.max - this.min));
	}

	public void setValue(double value) {
		this.value = ((Mth.clamp((float) value, this.min, this.max) - this.min) / (this.max - this.min));
	}

	@Override
	protected void updateMessage() {
	}

	@Override
	protected void applyValue() {
		this.setMessage(this.onChange.apply(Mth.lerp(Mth.clamp(this.value, 0.0F, 1.0F), this.min, this.max)));
	}
}
