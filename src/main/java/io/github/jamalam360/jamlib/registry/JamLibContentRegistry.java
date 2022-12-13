package io.github.jamalam360.jamlib.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;

public interface JamLibContentRegistry {
    default Item createBlockItem(Block block) {
        return new BlockItem(block, new Settings());
    }

    default ItemGroup getItemGroup(Item item) {
        return null;
    }
}
