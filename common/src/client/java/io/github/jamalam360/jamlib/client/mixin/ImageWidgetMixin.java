package io.github.jamalam360.jamlib.client.mixin;

import io.github.jamalam360.jamlib.client.mixinsupport.MutableSpriteImageWidget;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ImageWidget.class)
public class ImageWidgetMixin implements MutableSpriteImageWidget {
	@Mutable
	@Shadow
	@Final
	private ResourceLocation imageLocation;

	@Override
	public void setSprite(ResourceLocation sprite) {
		this.imageLocation = sprite;
	}
}
