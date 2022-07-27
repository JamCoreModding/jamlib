package io.github.jamalam360.jamlib.mixin.dev;

import net.minecraft.server.dedicated.EulaReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Jamalam
 */

@Mixin(EulaReader.class)
public class EulaReaderMixin {
    @Inject(
            method = "isEulaAgreedTo",
            at = @At("HEAD"),
            cancellable = true
    )
    public void jamlib$setEulaAlwaysAgreedToInDev(CallbackInfoReturnable<Boolean> cir) {
        if (System.getProperty("jamlib.dev.disable-eula-auto-agree") == null) {
            cir.setReturnValue(true);
        }
    }
}
