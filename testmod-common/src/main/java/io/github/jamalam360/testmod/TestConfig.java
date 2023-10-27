package io.github.jamalam360.testmod;

import blue.endless.jankson.Comment;
import io.github.jamalam360.jamlib.config.*;
import net.minecraft.network.chat.Component;

import java.util.List;

public class TestConfig implements ConfigExtensions<TestConfig> {
//public class TestConfig {
	@Comment("This is a boolean")
	@RequiresRestart
	public boolean testBoolean = true;

	@WithinRange(min = 0, max = 10)
	public float floatyValue = 3.2F;

	@WithinRange(min = 0, max = 10)
	@Slider
	public float floatyValueWithSlider = 3.2F;

	public String testString = "Hello World!";

	@MatchesRegex("^[a-z]+$")
	public String lowercase = "lowercase";

	public ConfigEnum testEnum = ConfigEnum.SECOND;

	public static enum ConfigEnum {
		FIRST,
		SECOND,
		THIRD;
	}

	@Override
	public List<Link> getLinks() {
		return List.of(
				new Link(Link.GENERIC_LINK, "https://www.youtube.com/watch?v=dQw4w9WgXcQ", Component.literal("Click here to watch a cool video!")),
				new Link(Link.GITHUB, "https://github.com/Jamalam360", Component.literal("GitHub")),
				new Link(Link.DISCORD, "https://discord.com", Component.literal("Discord"))
		);
	}
}