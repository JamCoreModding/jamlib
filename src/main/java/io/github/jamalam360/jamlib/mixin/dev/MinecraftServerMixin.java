package io.github.jamalam360.jamlib.mixin.dev;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Jamalam
 */

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(
            method = "isOnlineMode",
            at = @At("HEAD"),
            cancellable = true
    )
    public void jamlib$setNotOnlineInDev(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
