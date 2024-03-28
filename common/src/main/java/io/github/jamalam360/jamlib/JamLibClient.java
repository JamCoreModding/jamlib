package io.github.jamalam360.jamlib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import static io.github.jamalam360.jamlib.JamLib.JAR_RENAMING_CHECKER;

@Environment(EnvType.CLIENT)
public class JamLibClient {
	public static void onPlayerJoin(LocalPlayer player) {
		if (player != Minecraft.getInstance().player) {
			return;
		}

		if (JAR_RENAMING_CHECKER.getSuspiciousJarsToNotifyAbout().isEmpty()) {
			return;
		}

		player.displayClientMessage(Component.translatable("text.jamlib.renamed_1", Component.translatable("text.jamlib.renamed_1.1").withStyle(ChatFormatting.RED)), false);

		for (String jar : JAR_RENAMING_CHECKER.getSuspiciousJarsToNotifyAbout()) {
			player.displayClientMessage(Component.literal(" - " + jar).withStyle(ChatFormatting.GRAY), false);
		}

		player.displayClientMessage(Component.translatable("text.jamlib.renamed_2", Component.translatable("text.jamlib.renamed_2.1").withStyle(ChatFormatting.YELLOW), Component.translatable("text.jamlib.renamed_2.2").withStyle(ChatFormatting.RED), Component.translatable("text.jamlib.renamed_2.3").withStyle(ChatFormatting.RED), Component.translatable("text.jamlib.renamed_2.4").withStyle(ChatFormatting.GOLD), Component.translatable("text.jamlib.renamed_2.5").withStyle(ChatFormatting.GOLD)), false);
		player.displayClientMessage(Component.translatable("text.jamlib.renamed_3", Component.translatable("text.jamlib.renamed_3.1").withStyle(ChatFormatting.GRAY)), false);
		JAR_RENAMING_CHECKER.afterNotify();
	}
}
