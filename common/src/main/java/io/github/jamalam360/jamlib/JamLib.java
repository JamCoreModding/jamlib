package io.github.jamalam360.jamlib;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
public class JamLib {
	public static final String MOD_ID = "jamlib";
	public static final String MOD_NAME = "JamLib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
	private static final JarRenamingChecker JAR_RENAMING_CHECKER = new JarRenamingChecker();

	public static void init() {
		LOGGER.info("Initializing JamLib on " + JamLibPlatform.getPlatform());
		checkForJarRenaming(JamLib.class);

		EnvExecutor.runInEnv(EnvType.CLIENT, () -> () -> ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(JamLib::onPlayerJoin));
	}

	public static void checkForJarRenaming(Class<?> anyModClass) {
		JAR_RENAMING_CHECKER.checkJar(anyModClass);
	}

	private static void onPlayerJoin(LocalPlayer player) {
		if (player != Minecraft.getInstance().player) {
			return;
		}

		if (JAR_RENAMING_CHECKER.getSuspiciousJarsToNotifyAbout().isEmpty()) {
			return;
		}

		player.displayClientMessage(Component.translatable("text.jamlib.renamed_1"), false);

		for (String jar : JAR_RENAMING_CHECKER.getSuspiciousJarsToNotifyAbout()) {
			player.displayClientMessage(Component.literal(" - " + jar), false);
		}

		player.displayClientMessage(Component.translatable("text.jamlib.renamed_2"), false);
		player.displayClientMessage(Component.translatable("text.jamlib.renamed_3"), false);

		JAR_RENAMING_CHECKER.afterNotify();
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
