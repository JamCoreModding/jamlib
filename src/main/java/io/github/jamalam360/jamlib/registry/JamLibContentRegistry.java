package io.github.jamalam360.jamlib.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * <p>Used to provide additional functionality for {@link io.github.jamalam360.jamlib.registry.annotation.ContentRegistry} classes.</p>
 *
 * <p>Not required.</p>
 */
public interface JamLibContentRegistry {

    /**
     * @param block The {@link Block} to create the {@link Item} for.
     *
     * @return An {@link Item} that should be registered alongside this {@link Block}.
     */
    default Item createBlockItem(Block block) {
        return new BlockItem(block, new Item.Settings());
    }

    /**
     * @param item The {@link Item}.
     *
     * @return The {@link ItemGroup} to use.
     */
    default ItemGroup getItemGroup(Item item) {
        return null;
    }
}
