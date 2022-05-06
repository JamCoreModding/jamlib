package io.github.jamalam360.jamlib.registry.entry;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class BlockWithEntityEntry<T extends BlockEntity> extends BlockEntry {
    private BlockEntityType<T> blockEntityType;

    public BlockWithEntityEntry(Identifier id, Block block) {
        super(id, block);
    }

    public BlockWithEntityEntry<T> withBlockEntityType(BlockEntityType<T> blockEntityType) {
        this.blockEntityType = blockEntityType;
        return this;
    }

    public BlockEntityType<T> getBlockEntityType() {
        return blockEntityType;
    }
}
