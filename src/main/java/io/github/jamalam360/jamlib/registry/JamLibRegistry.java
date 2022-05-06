package io.github.jamalam360.jamlib.registry;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.registry.entry.BlockEntry;
import io.github.jamalam360.jamlib.registry.entry.BlockWithEntityEntry;
import io.github.jamalam360.jamlib.registry.entry.ItemEntry;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;

@SuppressWarnings("unused")
public class JamLibRegistry {
    private static final Logger LOGGER = JamLib.getLogger("Registry");

    public void register(List<Object> registries) {
        for (Object registry : registries) {
            register(registry);
        }
    }

    public void register(Object registry) {
        for (Field field : registry.getClass().getDeclaredFields()) {
            try {
                if (field.getType().isAssignableFrom(BlockEntry.class)) {
                    BlockEntry entry = (BlockEntry) field.get(registry);

                    Registry.register(
                            Registry.BLOCK,
                            entry.getId(),
                            entry.getBlock()
                    );

                    if (entry.getItem() != null) {
                        Registry.register(
                                Registry.ITEM,
                                entry.getId(),
                                entry.getItem()
                        );
                    }

                    if (entry instanceof BlockWithEntityEntry blockEntityEntry) {
                        Registry.register(
                                Registry.BLOCK_ENTITY_TYPE,
                                entry.getId(),
                                blockEntityEntry.getBlockEntityType()
                        );
                    }
                } else if (field.getType().isAssignableFrom(ItemEntry.class)) {
                    ItemEntry entry = (ItemEntry) field.get(registry);

                    Registry.register(
                            Registry.ITEM,
                            entry.getId(),
                            entry.getItem()
                    );
                }
            } catch (IllegalAccessException e) {
                LOGGER.warn("Failed to access field: " + e.getMessage());
            }
        }
    }
}
