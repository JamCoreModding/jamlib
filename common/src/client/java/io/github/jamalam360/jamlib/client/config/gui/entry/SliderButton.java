package io.github.jamalam360.jamlib.client.config.gui.entry;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Consumer;
import java.util.function.Function;

public class SliderButton extends AbstractSliderButton {
	private final double min;
	private final double max;
	private final Consumer<Double> onChange;
	private final Function<Double, Component> toString;

	protected SliderButton(int x, int y, int width, int height, Component message, double min, double max, double value, Consumer<Double> onChange, Function<Double, Component> toString) {
		super(x, y, width, height, message, ((Mth.clamp((float) value, min, max) - min) / (max - min)));
		this.min = min;
		this.max = max;
		this.onChange = onChange;
		this.toString = toString;
	}

	public void updateValue(double value) {
		this.value = ((Mth.clamp((float) value, this.min, this.max) - this.min) / (this.max - this.min));
		this.updateMessage();
	}

	@Override
	protected void updateMessage() {
		this.setMessage(this.toString.apply(Mth.lerp(Mth.clamp(this.value, 0.0F, 1.0F), this.min, this.max)));
	}

	@Override
	protected void applyValue() {
		this.onChange.accept(Mth.lerp(Mth.clamp(this.value, 0.0F, 1.0F), this.min, this.max));
	}
}
