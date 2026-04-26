package io.github.jamalam360.jamlib.client.impl;

import static io.github.jamalam360.jamlib.JamLib.JAR_RENAMING_CHECKER;

import io.github.jamalam360.jamlib.client.api.events.ClientPlayLifecycleEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

public class JamLibClient {
    @ApiStatus.Internal
    public static void init() {
        ClientPlayLifecycleEvents.JOIN.listen(JamLibClient::onPlayerJoin);
    }

    private static void onPlayerJoin(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        if (JAR_RENAMING_CHECKER.getSuspiciousJarsToNotifyAbout().isEmpty()) {
            return;
        }

        player.sendSystemMessage(Component.translatable("text.jamlib.renamed_1", Component.translatable("text.jamlib.renamed_1.1").withStyle(ChatFormatting.RED)));

        for (String jar : JAR_RENAMING_CHECKER.getSuspiciousJarsToNotifyAbout()) {
            player.sendSystemMessage(Component.literal(" - " + jar).withStyle(ChatFormatting.GRAY));
        }

        player.sendSystemMessage(Component.translatable("text.jamlib.renamed_2", Component.translatable("text.jamlib.renamed_2.1").withStyle(ChatFormatting.YELLOW), Component.translatable("text.jamlib.renamed_2.2").withStyle(ChatFormatting.RED), Component.translatable("text.jamlib.renamed_2.3").withStyle(ChatFormatting.RED), Component.translatable("text.jamlib.renamed_2.4").withStyle(ChatFormatting.GOLD), Component.translatable("text.jamlib.renamed_2.5").withStyle(ChatFormatting.GOLD)));
        player.sendSystemMessage(Component.translatable("text.jamlib.renamed_3", Component.translatable("text.jamlib.renamed_3.1").withStyle(ChatFormatting.GRAY)));
        JAR_RENAMING_CHECKER.afterNotify();
    }
}
