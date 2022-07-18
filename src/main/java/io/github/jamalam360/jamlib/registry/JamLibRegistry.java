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

package io.github.jamalam360.jamlib.registry;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.registry.annotation.BlockItemFactory;
import io.github.jamalam360.jamlib.registry.annotation.ContentRegistry;
import io.github.jamalam360.jamlib.registry.annotation.WithIdentifier;
import io.github.jamalam360.jamlib.registry.annotation.WithoutBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class JamLibRegistry {
    public static void register(Class<?>... registries) {
        for (Class<?> clazz : registries) {
            register(clazz);
        }
    }

    public static void register(Class<?> registry) {
        if (!registry.isAnnotationPresent(ContentRegistry.class)) {
            JamLib.LOGGER.warn("@ContentRegistry annotation not present on registry class " + registry.getName());
            return;
        }

        String modId = registry.getAnnotation(ContentRegistry.class).value();
        boolean checkedForBlockItemCreator = false;
        Method blockItemCreator = null;

        for (Field f : registry.getFields()) {
            if (!f.canAccess(null)) {
                JamLib.LOGGER.warn("Cannot access field " + f.getName() + " in registry class " + registry.getName());
                continue;
            }


            Class<?> fClass = f.getDeclaringClass();

            Object fObj = null;

            try {
                fObj = f.get(null);
            } catch (IllegalAccessException ignored) {
            }

            Identifier fId = getIdentifier(modId, f);

            if (fClass.isAssignableFrom(Item.class)) {
                Registry.register(Registry.ITEM, fId, (Item) fObj);
            } else if (fClass.isAssignableFrom(Block.class)) {
                Registry.register(Registry.BLOCK, fId, (Block) fObj);

                if (!checkedForBlockItemCreator) {
                    for (Method method : registry.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(BlockItemFactory.class)) {
                            if (method.getParameterTypes().length != 1 || !method.getParameterTypes()[0].isAssignableFrom(Block.class) || !method.getReturnType().isAssignableFrom(Item.class)) {
                                throw new IllegalArgumentException("@BlockItemFactory method " + method.getName() + " in registry class " + registry.getName() + " has invalid parameters or return type.");
                            }

                            if (!method.canAccess(null)) {
                                throw new IllegalArgumentException("Cannot access @BlockItemFactory method " + method.getName() + " in registry class " + registry.getName());
                            }

                            blockItemCreator = method;
                            break;
                        }
                    }

                    checkedForBlockItemCreator = true;
                }

                if (blockItemCreator != null && !f.isAnnotationPresent(WithoutBlockItem.class)) {
                    try {
                        Registry.register(Registry.ITEM, fId, (Item) blockItemCreator.invoke(null, fObj));
                    } catch (Exception ignored) {
                    }
                }
            } else if (fClass.isAssignableFrom(BlockEntityType.class)) {
                Registry.register(Registry.BLOCK_ENTITY_TYPE, fId, (BlockEntityType<?>) fObj);
            } else if (fClass.isAssignableFrom(EntityType.class)) {
                Registry.register(Registry.ENTITY_TYPE, fId, (EntityType<?>) fObj);
            } else if (fClass.isAssignableFrom(Enchantment.class)) {
                Registry.register(Registry.ENCHANTMENT, fId, (Enchantment) fObj);
            } else if (fClass.isAssignableFrom(ScreenHandlerType.class)) {
                Registry.register(Registry.SCREEN_HANDLER, fId, (ScreenHandlerType<?>) fObj);
            }
        }
    }

    private static Identifier getIdentifier(String modId, Field f) {
        String path;

        if (f.isAnnotationPresent(WithIdentifier.class)) {
            path = f.getAnnotation(WithIdentifier.class).value();
        } else {
            path = f.getName().toLowerCase();
        }

        return new Identifier(modId, path);
    }
}
