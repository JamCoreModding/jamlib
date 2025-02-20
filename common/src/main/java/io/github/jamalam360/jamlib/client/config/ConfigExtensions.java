package io.github.jamalam360.jamlib.client.config;

import io.github.jamalam360.jamlib.JamLib;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Implement this interface on your mod's config class for additional functionality.
 *
 * @param <T> The config class.
 */
public interface ConfigExtensions<T> {

	/**
	 * @return The links to display on your config screen.
	 * @implNote Be careful, returning too many links may cause overflow.
	 * @see Link
	 */
	default List<Link> getLinks() {
		return List.of();
	}

	/**
	 * Can be used to validate your config fields after they have been edited in the config screen. Remember that this function is only called when the user is using a
	 * config screen - if they are editing the file directly they are on their own.
	 *
	 * @return A list of the errors and warnings that occurred during validation.
	 * @implNote It is worth calling {@code super.getValidationErrors()} and adding your own errors to the list, since by default this method performs validation such as
	 * adding warnings when the field is marked with {@link RequiresRestart}.
	 */
	default List<ValidationError> getValidationErrors(ConfigManager<T> manager, FieldValidationInfo info) {
		List<ValidationError> result = new ArrayList<>();

		if (info.backingField().isAnnotationPresent(MatchesRegex.class)) {
			MatchesRegex annotation = info.backingField().getAnnotation(MatchesRegex.class);

			if (!info.value().toString().matches(annotation.value())) {
				result.add(new ValidationError(ValidationError.Type.ERROR, info, Component.translatable("config.jamlib.matches_regex_tooltip", annotation.value())));
			}
		}

		if (info.backingField().isAnnotationPresent(RequiresRestart.class) && !info.initialValue().equals(info.value())) {
			result.add(new ValidationError(ValidationError.Type.WARNING, info, Component.translatable("config.jamlib.requires_restart_tooltip")));
		}

		if (info.backingField().isAnnotationPresent(WithinRange.class)) {
			if (info.backingField().getType() != double.class && info.backingField().getType() != Double.class
					&& info.backingField().getType() != float.class && info.backingField().getType() != Float.class
					&& info.backingField().getType() != int.class && info.backingField().getType() != Integer.class
					&& info.backingField().getType() != long.class && info.backingField().getType() != Long.class
			) {
				JamLib.LOGGER.error("Field {} is annotated with @WithinRange but is not a number!", info.backingField().getName());
			} else {
				WithinRange annotation = info.backingField().getAnnotation(WithinRange.class);
				Number value = (Number) info.value();

				if (value.doubleValue() < annotation.min() || value.doubleValue() > annotation.max()) {
					result.add(new ValidationError(ValidationError.Type.ERROR, info, Component.translatable("config.jamlib.within_range_tooltip", annotation.min(), annotation.max())));
				}
			}
		}

		return result;
	}

	/**
	 * A link within your config screen. Displayed in the top right corner. Icons are displayed as 16x16 textures in 20x20 buttons.
	 *
	 * @see ConfigExtensions#getLinks()
	 */
	class Link {

		// I am not an artist but these are recognizable at least.
		public static final ResourceLocation DISCORD = JamLib.id("textures/gui/link_discord.png");
		public static final ResourceLocation GENERIC_LINK = JamLib.id("textures/gui/link_generic.png");
		public static final ResourceLocation GITHUB = JamLib.id("textures/gui/link_github.png");

		private final ResourceLocation texture;
		private final URL url;
		private final Component tooltip;

		public Link(ResourceLocation texture, String url, Component tooltip) {
			this.texture = texture;

			try {
				this.url = new URI(url).toURL();
			} catch (Exception e) {
				JamLib.LOGGER.error("Malformed URL for config screen link: {}", url);
				throw new RuntimeException(e);
			}

			this.tooltip = tooltip;
		}

		public Link(ResourceLocation texture, URL url, Component tooltip) {
			this.texture = texture;
			this.url = url;
			this.tooltip = tooltip;
		}

		public ResourceLocation getTexture() {
			return this.texture;
		}

		public URL getUrl() {
			return this.url;
		}

		public Component getTooltip() {
			return this.tooltip;
		}
	}

	/**
	 * A validation error within your config screen. Displayed next to the field.
	 *
	 * @param type    The type of error
	 * @param field   The field that caused the error
	 * @param message The message to display in the tooltip
	 * @see FieldValidationInfo
	 * @see ConfigExtensions#getValidationErrors(ConfigManager, FieldValidationInfo)
	 */
	record ValidationError(Type type, FieldValidationInfo field, Component message) {

		/**
		 * The type of error.
		 */
		public enum Type {
			/**
			 * A warning - the user may choose to ignore this.
			 */
			WARNING,
			/**
			 * An error - the user must fix this before they can save through the config file.
			 */
			ERROR;

			private final ResourceLocation texture;

			Type() {
				this.texture = JamLib.id("textures/gui/validation_" + this.name().toLowerCase() + ".png");
			}

			public ResourceLocation getTexture() {
				return this.texture;
			}
		}
	}

	/**
	 * Used to provide information about config fields, for the validator.
	 *
	 * @param name         The Java name of the field
	 * @param value        The new value of the field
	 * @param initialValue The value of the field before the config screen was opened
	 * @param backingField The Java field that corresponds to this config field
	 * @see ConfigExtensions#getValidationErrors(ConfigManager, FieldValidationInfo)
	 */
	record FieldValidationInfo(String name, Object value, Object initialValue, Field backingField) {
	}
}
