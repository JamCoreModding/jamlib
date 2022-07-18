package io.github.jamalam360.jamlib.test.registry;

import io.github.jamalam360.jamlib.registry.annotation.BlockItemFactory;
import io.github.jamalam360.jamlib.registry.annotation.ContentRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * @author Jamalam
 */

@ContentRegistry("jamlib-test")
public class TestBlocks {
    public static final Block TEST_BLOCK = new Block(FabricBlockSettings.of(Material.STONE));

    @BlockItemFactory
    public static Item createBlockItem(Block block) {
        return new BlockItem(block, new FabricItemSettings().group(ItemGroup.MISC));
    }
}
