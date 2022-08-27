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

package io.github.jamalam360.jamlib.config.v2;

import io.github.jamalam360.jamlib.JamLib;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.InvalidIdentifierException;

import java.util.*;

/**
 * @author Jamalam
 */

@Environment(EnvType.CLIENT)
public class JamLibConfigScreen extends Screen {
    private final Screen parent;
    private final String modId;
    private final JamLibConfig config;
    private ListWidget list;
    private ButtonWidget done;
    private Map<ClickableWidget, Boolean> invalidEntries = new HashMap<>();

    public JamLibConfigScreen(Screen parent, String modId) {
        super(Text.translatable("config." + modId + ".title"));
        this.parent = parent;
        this.modId = modId;
        this.config = JamLibConfig.getConfig(modId);
    }

    private Text getTranslatableText(String path) {
        return Text.translatable("config." + this.modId + "." + path);
    }

    @Override
    public void tick() {
        super.tick();
        boolean hasInvalidEntries = this.invalidEntries.containsValue(true);
        this.done.active = !hasInvalidEntries;
    }

    @Override
    protected void init() {
        super.init();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 154, this.height - 28, 150, 20, ScreenTexts.CANCEL, button -> {
            try {
                this.config.readFromFile();
            } catch (Exception e) {
                JamLib.LOGGER.warn("Failed to read config file for mod " + this.modId);
            }

            Objects.requireNonNull(client).setScreen(parent);
        }));

        this.done = this.addDrawableChild(new ButtonWidget(this.width / 2 + 4, this.height - 28, 150, 20, ScreenTexts.DONE, (button) -> {
            System.out.println("E");
            this.config.updateEntries(this.config.config);
            JamLibConfig.writeToFile(this.modId);
            Objects.requireNonNull(client).setScreen(parent);
        }));

        this.list = new ListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);

        for (ConfigEntry entry : this.config.entries) {
            ButtonWidget resetButton = new ButtonWidget(width - 205, 0, 40, 20, Text.literal("Reset").formatted(Formatting.RED), (button -> {
                entry.setValue(entry.getDefaultValue());
                double scrollAmount = this.list.getScrollAmount();
                Objects.requireNonNull(client).setScreen(this);
                this.list.setScrollAmount(scrollAmount);
            }));

            ClickableWidget widget;

            if (entry.getField().getType() == Boolean.class || entry.getField().getType() == boolean.class) {
                boolean val = (boolean) entry.getValue();

                widget = new ButtonWidget(width - 160, 0, 40, 20, Text.literal(val ? "Yes" : "No").formatted(val ? Formatting.GOLD : Formatting.RED), (button -> {
                    entry.setValue(!(boolean) entry.getValue());
                    button.setMessage(Text.literal((boolean) entry.getValue() ? "Yes" : "No").formatted((boolean) entry.getValue() ? Formatting.GOLD : Formatting.RED));
                }));
            } else {
                widget = new TextFieldWidget(this.textRenderer, width - 160, 0, 150, 20, Text.of(entry.getValue().toString()));
                ((TextFieldWidget) widget).setText(entry.getValue().toString());
                ((TextFieldWidget) widget).setEditable(true);
                ((TextFieldWidget) widget).setChangedListener((newValue) -> {
                    try {
                        this.config.setEntryWithConverters(entry, newValue);
                        ((TextFieldWidget) widget).setEditableColor(Formatting.WHITE.getColorValue());
                        this.invalidEntries.put(widget, false);
                    } catch (InvalidIdentifierException | IllegalArgumentException e) {
                        ((TextFieldWidget) widget).setEditableColor(Formatting.RED.getColorValue());
                        this.invalidEntries.put(widget, true);
                    }
                });
            }

            this.list.addButton(List.of(
                    widget,
                    resetButton
            ), this.getTranslatableText("entry." + entry.getField().getName()));

            this.addSelectableChild(list);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.list.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, title, width / 2, 15, 0xFFFFFF);

        for (ConfigEntry entry : this.config.entries) {
            if (this.list.getHoveredButton(mouseX, mouseY).isPresent()) {
                ClickableWidget buttonWidget = this.list.getHoveredButton(mouseX, mouseY).get();
                Text text = ButtonEntry.buttonsWithText.get(buttonWidget);
                Text name = this.getTranslatableText("entry." + entry.getField().getName());
                String key = "config." + this.modId + ".entry." + entry.getField().getName() + ".tooltip";

                if (I18n.hasTranslation(key) && text.equals(name)) {
                    List<Text> list = new ArrayList<>();
                    for (String str : I18n.translate(key).split("\n")) {
                        list.add(Text.literal(str));
                    }

                    renderTooltip(matrices, list, mouseX, mouseY);
                }
            }
        }

        super.render(matrices, mouseX, mouseY, delta);
    }

    public static class ListWidget extends ElementListWidget<ButtonEntry> {
        TextRenderer textRenderer;

        public ListWidget(MinecraftClient minecraftClient, int i, int j, int k, int l, int m) {
            super(minecraftClient, i, j, k, l, m);
            this.centerListVertically = false;
            textRenderer = minecraftClient.textRenderer;
        }

        @Override
        public int getScrollbarPositionX() {
            return this.width - 7;
        }

        public void addButton(List<ClickableWidget> buttons, Text text) {
            this.addEntry(ButtonEntry.create(buttons, text));
        }

        @Override
        public int getRowWidth() {
            return 10000;
        }

        public Optional<ClickableWidget> getHoveredButton(double mouseX, double mouseY) {
            for (ButtonEntry buttonEntry : this.children()) {
                if (!buttonEntry.buttons.isEmpty() && buttonEntry.buttons.get(0).isMouseOver(mouseX, mouseY)) {
                    return Optional.of(buttonEntry.buttons.get(0));
                }
            }
            return Optional.empty();
        }
    }

    public static class ButtonEntry extends ElementListWidget.Entry<ButtonEntry> {
        public static final Map<ClickableWidget, Text> buttonsWithText = new HashMap<>();
        private static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        public final List<ClickableWidget> buttons;
        private final Text text;
        private final List<ClickableWidget> children = new ArrayList<>();

        private ButtonEntry(List<ClickableWidget> buttons, Text text) {
            if (!buttons.isEmpty()) buttonsWithText.put(buttons.get(0), text);
            this.buttons = buttons;
            this.text = text;
            children.addAll(buttons);
        }

        public static ButtonEntry create(List<ClickableWidget> buttons, Text text) {
            return new ButtonEntry(buttons, text);
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            buttons.forEach(b -> {
                b.y = y;
                b.render(matrices, mouseX, mouseY, tickDelta);
            });

            if (text != null && (!text.getString().contains("spacer") || !buttons.isEmpty())) {
                DrawableHelper.drawTextWithShadow(matrices, textRenderer, text, 12, y + 5, 0xFFFFFF);
            }
        }

        public List<? extends Element> children() {
            return children;
        }

        public List<? extends Selectable> selectableChildren() {
            return children;
        }
    }
}
