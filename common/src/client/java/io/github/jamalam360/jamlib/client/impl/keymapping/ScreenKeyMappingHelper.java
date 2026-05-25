package io.github.jamalam360.jamlib.client.impl.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class ScreenKeyMappingHelper {
	private static final List<KeyMapping> MAPPINGS = new ArrayList<>();

	public static void registerScreenKeyMapping(KeyMapping mapping) {
		MAPPINGS.add(mapping);
	}

	public static boolean onKeyPressed(Screen screen, KeyEvent ev) {
		// Using a pattern variable here breaks Architectury Transformer.
		if (screen.getFocused() instanceof EditBox && ((EditBox) screen.getFocused()).canConsumeInput()) {
			return false;
		}

		for (KeyMapping mapping : MAPPINGS) {
			if (mapping.matches(ev)) {
				KeyMapping.click(InputConstants.Type.KEYSYM.getOrCreate(ev.key()));
				return true;
			}
		}

		return false;
	}

	public static boolean onMousePressed(MouseButtonEvent ev) {
		for (KeyMapping mapping : MAPPINGS) {
			if (mapping.matchesMouse(ev)) {
				KeyMapping.click(InputConstants.Type.MOUSE.getOrCreate(ev.input()));
				return true;
			}
		}

		return false;
	}
}
