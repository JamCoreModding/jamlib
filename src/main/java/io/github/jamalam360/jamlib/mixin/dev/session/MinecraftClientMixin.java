package io.github.jamalam360.jamlib.mixin.dev.session;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.github.jamalam360.jamlib.Ducks;
import io.github.jamalam360.jamlib.JamLib;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.util.Session;
import net.minecraft.util.UuidUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * This class is adapted from <a href="https://github.com/GlassPane/mesh">Mesh</a>.
 * @author Jamalam
 */

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(
            method = "createUserApiService",
            at = @At("HEAD")
    )
    private void jamlib$useDevAuth(YggdrasilAuthenticationService authService, RunArgs runArgs, CallbackInfoReturnable<UserApiService> cir) {
        jamlib$tryLoadSession(runArgs.network.session).ifPresent(session -> {
            JamLib.LOGGER.info("Updating session to player with username: " + session.getUsername());
            ((Ducks.RunArgs$Network) runArgs.network).setSession(session);
        });
    }

    @Unique
    private static Optional<Session> jamlib$tryLoadSession(Session previous) {
        String username = System.getProperty("jamlib.dev.session.username");

        if (username != null) {
            String uuid = UuidUtil.getOfflinePlayerUuid(username).toString();
            String idString = System.getProperty("jamlib.dev.session.uuid");
            if (idString != null) {
                uuid = idString;
            }
            return Optional.of(new Session(username, uuid, previous.getAccessToken(), previous.getXuid(), previous.getClientId(), previous.getAccountType()));
        }

        return Optional.empty();
    }
}
