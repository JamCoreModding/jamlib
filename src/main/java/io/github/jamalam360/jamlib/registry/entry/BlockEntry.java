package io.github.jamalam360.jamlib.registry.entry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class BlockEntry {
    private final Identifier id;
    private final Block block;
    private BlockItem item;

    public BlockEntry(Identifier id, Block block) {
        this.id = id;
        this.block = block;
    }

    public BlockEntry withItem(ItemGroup group) {
        this.item = new BlockItem(this.block, new Item.Settings().group(group));
        return this;
    }

    public BlockEntry withItem(Item.Settings settings) {
        this.item = new BlockItem(this.block, settings);
        return this;
    }

    public BlockEntry withItem(BlockItem item) {
        this.item = item;
        return this;
    }

    public Identifier getId() {
        return this.id;
    }

    public Block getBlock() {
        return this.block;
    }

    public BlockItem getItem() {
        return this.item;
    }
}
