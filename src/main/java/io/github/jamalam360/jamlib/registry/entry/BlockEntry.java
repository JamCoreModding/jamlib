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
