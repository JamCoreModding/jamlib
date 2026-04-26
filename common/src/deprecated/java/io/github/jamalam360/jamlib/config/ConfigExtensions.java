package io.github.jamalam360.jamlib.config;

import io.github.jamalam360.jamlib.JamLib;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Deprecated(forRemoval = true)
public interface ConfigExtensions<T> {
	default List<Link> getLinks() {
		return List.of();
	}

	default void afterSave() { }

	default List<ValidationError> getValidationErrors(ConfigManager<T> manager, FieldValidationInfo info) {
		return List.of();
	}

	@Deprecated(forRemoval = true)
	class Link {
		@Deprecated(forRemoval = true)
		public static final Identifier DISCORD = JamLib.id("link_discord");
		@Deprecated(forRemoval = true)
		public static final Identifier GENERIC_LINK = JamLib.id("link_generic");
		@Deprecated(forRemoval = true)
		public static final Identifier GITHUB = JamLib.id("link_github");

		private final Identifier texture;
		private final URL url;
		private final Component tooltip;

		public Link(Identifier texture, String url, Component tooltip) {
			this.texture = texture;

			try {
				this.url = new URI(url).toURL();
			} catch (Exception e) {
				JamLib.LOGGER.error("Malformed URL for config screen link: {}", url);
				throw new RuntimeException(e);
			}

			this.tooltip = tooltip;
		}

		public Link(Identifier texture, URL url, Component tooltip) {
			this.texture = texture;
			this.url = url;
			this.tooltip = tooltip;
		}

		public Identifier getTexture() {
			return this.texture;
		}

		public URL getUrl() {
			return this.url;
		}

		public Component getTooltip() {
			return this.tooltip;
		}
	}

	@Deprecated(forRemoval = true)
	record ValidationError(Type type, FieldValidationInfo field, Component message) {

		@Deprecated(forRemoval = true)
		public enum Type {
			WARNING,
			ERROR;

			private final Identifier texture;

			Type() {
				this.texture = JamLib.id("validation_" + this.name().toLowerCase());
			}

			public Identifier getTexture() {
				return this.texture;
			}
		}
	}

	@Deprecated(forRemoval = true)
	record FieldValidationInfo(String name, Object value, Object initialValue, Field backingField) {
	}
}
