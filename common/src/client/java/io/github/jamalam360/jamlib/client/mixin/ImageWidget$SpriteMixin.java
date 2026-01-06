package io.github.jamalam360.jamlib.client.mixin;

import io.github.jamalam360.jamlib.client.mixinsupport.MutableSpriteImageWidget$Sprite;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.client.gui.components.ImageWidget$Sprite")
public class ImageWidget$SpriteMixin implements MutableSpriteImageWidget$Sprite {
	@Mutable
	@Shadow
	private Identifier sprite;

	@Override
	public void setSprite(Identifier sprite) {
		this.sprite = sprite;
	}
}
