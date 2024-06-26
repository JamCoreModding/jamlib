package io.github.jamalam360.jamlib.neoforge.mixin;

import io.github.jamalam360.jamlib.config.ConfigManager;
import io.github.jamalam360.jamlib.config.gui.ConfigScreen;
import io.github.jamalam360.jamlib.config.gui.SelectConfigScreen;
import java.util.List;
import java.util.Optional;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforgespi.language.IModInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = IConfigScreenFactory.class, remap = false)
public interface IConfigScreenFactoryMixin {

    @Inject(
          method = "getForMod",
          at = @At("HEAD"),
          cancellable = true,
          remap = false
    )
    private static void jamlib$cursed$injectScreenFactoriesForJamLibConfig(IModInfo selectedMod, CallbackInfoReturnable<Optional<IConfigScreenFactory>> cir) {
        List<ConfigManager<?>> managers = ConfigManager.MANAGERS.values().stream().filter(m -> m.getModId().equals(selectedMod.getModId())).toList();

        if (managers.size() == 1) {
            cir.setReturnValue(Optional.of((minecraft, parent) -> new ConfigScreen<>(managers.getFirst(), parent)));
        } else if (managers.size() > 1) {
            cir.setReturnValue(Optional.of((minecraft, parent) -> new SelectConfigScreen(parent, selectedMod.getModId())));
        }
    }
}
