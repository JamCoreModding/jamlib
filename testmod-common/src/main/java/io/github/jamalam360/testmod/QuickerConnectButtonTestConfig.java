package io.github.jamalam360.testmod;

import blue.endless.jankson.Comment;
import io.github.jamalam360.jamlib.config.ConfigExtensions;
import io.github.jamalam360.jamlib.config.MatchesRegex;
import io.github.jamalam360.jamlib.config.WithinRange;
import net.minecraft.network.chat.Component;

import java.util.List;

public class QuickerConnectButtonTestConfig implements ConfigExtensions<QuickerConnectButtonTestConfig> {
	@MatchesRegex("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$")
	@Comment("Leave empty to disable the quick connect button")
	public String ip = "";
	@WithinRange(min = 0, max = 65535)
	public int port = 25565;
	public boolean replaceMultiplayerButton = false;
	public String text = "";

	public boolean enabled() {
		return this.ip != null && !this.ip.equals("");
	}

	public Component getButtonText() {
		if (this.text != null && !this.text.equals("")) {
			return Component.literal(this.text);
		} else {
			return Component.translatable("menu.quickerconnectbutton.connect");
		}
	}

	@Override
	public List<ConfigExtensions.Link> getLinks() {
		return List.of(
				new ConfigExtensions.Link(ConfigExtensions.Link.DISCORD, "https://jamalam.tech/Discord", Component.translatable("config.quickerconnectbutton.discord")),
				new ConfigExtensions.Link(ConfigExtensions.Link.GITHUB, "https://github.com/JamCoreModding/quicker-connect-button", Component.translatable("config.quickerconnectbutton.github")),
				new ConfigExtensions.Link(ConfigExtensions.Link.GENERIC_LINK, "https://modrinth.com/mod/quicker-connect-button", Component.translatable("config.quickerconnectbutton.modrinth"))
		);
	}
}
