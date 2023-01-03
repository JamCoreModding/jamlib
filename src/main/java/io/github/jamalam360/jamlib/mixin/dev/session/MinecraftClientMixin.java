/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Jamalam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
            JamLib.LOGGER.info("Updating session to player with username:", session.getUsername());
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
