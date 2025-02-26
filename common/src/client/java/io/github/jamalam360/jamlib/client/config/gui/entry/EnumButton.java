package io.github.jamalam360.jamlib.client.config.gui.entry;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Consumer;

public class EnumButton<E extends Enum<E>> extends Button {
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

	protected E getValue() {
		return this.enumClass.getEnumConstants()[this.index];
	}

	protected void setValue(E value) {
		this.setIndex(value.ordinal());
	}

	protected void setIndex(int index) {
		this.index = index;
	}
}
