package io.github.jamalam360.jamlib.client.config.gui;

import dev.architectury.platform.Platform;
import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.client.config.gui.entry.ConfigEntry;
import io.github.jamalam360.jamlib.client.gui.WidgetList;
import io.github.jamalam360.jamlib.config.ConfigExtensions;
import io.github.jamalam360.jamlib.config.ConfigManager;
import io.github.jamalam360.jamlib.config.HiddenInGui;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A screen for editing a config managed through a {@link ConfigManager}.
 */
@ApiStatus.Internal
public class ConfigScreen<T> extends Screen {

    protected final ConfigManager<T> manager;
    private final Screen parent;
    private final List<ConfigEntry> entries;
    private Button doneButton;

    public ConfigScreen(ConfigManager<T> manager, Screen parent) {
        super(createTitle(manager));
        this.manager = manager;
        this.parent = parent;
        this.entries = new ArrayList<>();
    }

    @ApiStatus.Internal
    public static String createTranslationKey(String modId, String configName, String path) {
        if (modId.equals(configName)) {
            return "config." + modId + "." + path;
        } else {
            return "config." + modId + "." + configName + "." + path;
        }
    }

    protected static Component createTitle(ConfigManager<?> manager) {
        String translationKey = createTranslationKey(manager.getModId(), manager.getConfigName(), "title");

        if (I18n.exists(translationKey)) {
            return Component.translatable(translationKey);
        } else {
            return Component.literal(Platform.getMod(manager.getModId()).getName());
        }
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> {
            this.manager.reloadFromDisk();
            Objects.requireNonNull(this.minecraft).setScreen(this.parent);
        }).pos(this.width / 2 - 154, this.height - 28).size(150, 20).build());

        this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
            if (this.hasChanges()) {
                this.manager.save();
            }

            Objects.requireNonNull(this.minecraft).setScreen(this.parent);
        }).pos(this.width / 2 + 4, this.height - 28).size(150, 20).build());

        SpriteIconButton editManuallyButton = this.addRenderableWidget(
                SpriteIconButton.builder(Component.translatable("config.jamlib.edit_manually"), button -> {
                    if (this.hasChanges()) {
                            this.manager.save();
                        }

                        Util.getPlatform().openFile(Platform.getConfigFolder().resolve(this.manager.getConfigName() + ".json5").toFile());
                        Objects.requireNonNull(this.minecraft).setScreen(this.parent);
                }, true).sprite(JamLib.id("writable_book"), 16, 16).size(20, 20).build()
        );
        editManuallyButton.setX(7);
        editManuallyButton.setY(7);
        WidgetList list = new WidgetList(this.minecraft, this.width, this.height - 64, 32);

        if (this.entries.isEmpty()) {
            for (Field field : this.manager.getConfigClass().getFields()) {
                if (field.isAnnotationPresent(HiddenInGui.class)) {
                    continue;
                }

                this.entries.add(ConfigEntry.createFromField(this.manager.getModId(), this.manager.getConfigName(), field));
            }
        }

        for (ConfigEntry entry : this.entries) {
            list.addWidgetGroup(entry.createWidgets(this.width));
        }

        this.addRenderableWidget(list);

        if (this.manager.get() instanceof ConfigExtensions<?> ext) {
            List<ConfigExtensions.Link> links = ext.getLinks();

            for (int i = 0; i < links.size(); i++) {
                ConfigExtensions.Link link = links.get(i);
                SpriteIconButton linkButton = this.addRenderableWidget(
                        SpriteIconButton.builder(link.getTooltip(), button -> {
                            try {
                                Util.getPlatform().openUri(link.getUrl().toURI());
                            } catch (Exception e) {
                                JamLib.LOGGER.error("Failed to open link", e);
                            }
                        }, true).sprite(link.getTexture(), 16, 16).size(20, 20).build()

                );
                linkButton.setX(this.width - 30 - (28 * i));
                linkButton.setY(5);
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(Minecraft.getInstance().font, this.title, this.width / 2, 12, 0xFFFFFF);
    }

    private boolean canExit() {
        return this.entries.stream().allMatch(ConfigEntry::isValid);
    }

    private boolean hasChanges() {
        return this.entries.stream().anyMatch(ConfigEntry::hasChanged);
    }

    @Override
    public void tick() {
        super.tick();
        boolean canExit = this.canExit();

        if (this.doneButton.active != canExit) {
            this.doneButton.active = canExit;
        }
    }
}
