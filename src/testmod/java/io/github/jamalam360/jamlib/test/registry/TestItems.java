package io.github.jamalam360.jamlib.test.registry;

import io.github.jamalam360.jamlib.registry.annotation.ContentRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * @author Jamalam
 */

@ContentRegistry("jamlib-test")
public class TestItems {
    public static final Item TEST_ITEM = new Item(new FabricItemSettings().group(ItemGroup.MISC));
}
