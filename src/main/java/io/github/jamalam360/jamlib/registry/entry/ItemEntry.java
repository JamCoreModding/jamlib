package io.github.jamalam360.jamlib.registry.entry;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ItemEntry {
    private final Identifier id;
    private final Item item;

    public ItemEntry(Identifier id, Item item) {
        this.id = id;
        this.item = item;
    }

    public Identifier getId() {
        return this.id;
    }

    public Item getItem() {
        return this.item;
    }
}
