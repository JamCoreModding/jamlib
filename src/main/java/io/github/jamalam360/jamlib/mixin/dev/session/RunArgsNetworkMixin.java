package io.github.jamalam360.jamlib.mixin.dev.session;

import io.github.jamalam360.jamlib.Ducks;
import net.minecraft.client.RunArgs;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author Jamalam
 */

@Mixin(RunArgs.Network.class)
public class RunArgsNetworkMixin implements Ducks.RunArgs$Network {

    @Mutable
    @Shadow @Final public Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }
}
