package io.github.jamalam360.jamlib.config.gui;

import dev.architectury.platform.Platform;
import io.github.jamalam360.jamlib.config.ConfigManager;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SelectConfigScreen extends Screen {

    private final String modId;
    private final Screen parent;

    public SelectConfigScreen(Screen parent, String modId) {
        super(getTitleComponent(modId));
        this.parent = parent;
        this.modId = modId;
    }

    private static Component getTitleComponent(String modId) {
        String translationKey = "config." + modId + ".title";

        if (I18n.exists(translationKey)) {
            return Component.translatable(translationKey);
        } else {
            return Component.translatable("config.jamlib.selection_screen_title", Platform.getMod(modId).getName());
        }
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> Objects.requireNonNull(this.minecraft).setScreen(this.parent)).pos(this.width / 2 - 75, this.height - 28).size(150, 20).build());

        ConfigSelectionList list = new ConfigSelectionList(this.minecraft, this.width, this.height - 64, 32, 25);
        ConfigManager.MANAGERS.values().stream().filter(m -> m.getModId().equals(this.modId)).forEach(list::addEntry);
        this.addRenderableWidget(list);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(Minecraft.getInstance().font, this.title, this.width / 2, 12, 0xFFFFFF);
    }

    private static class ConfigSelectionList extends SelectionList {

        public ConfigSelectionList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
            super(minecraft, width, height, y, itemHeight);
        }

        private void addEntry(ConfigManager<?> manager) {
            List<FormattedCharSequence> tooltip = null;
            String tooltipTranslationKey = ConfigScreen.createTranslationKey(manager.getModId(), manager.getConfigName(), "tooltip");

            if (I18n.exists(tooltipTranslationKey)) {
                tooltip = Minecraft.getInstance().font.split(Component.translatable(tooltipTranslationKey), 200);
            }

            this.addEntry(new SelectionListEntry(
                  ConfigScreen.createTitle(manager),
                  tooltip,
                  List.of(
                        Button.builder(Component.translatable("config.jamlib.open"), b -> this.minecraft.setScreen(new ConfigScreen<>(manager, this.minecraft.screen))).pos(this.width - 160, this.height - 28).size(150, 20).build()
                  )
            ));
        }
    }
}
