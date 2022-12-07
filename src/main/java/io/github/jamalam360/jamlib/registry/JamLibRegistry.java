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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

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

            Class<?> fClass = f.getType();
            Object fObj = null;

            try {
                fObj = f.get(null);
            } catch (IllegalAccessException ignored) {
            }

            Identifier fId = getIdentifier(modId, f);

            if (Item.class.isAssignableFrom(fClass)) {
                Registry.register(Registries.ITEM, fId, (Item) fObj);
            } else if (Block.class.isAssignableFrom(fClass)) {
                Registry.register(Registries.BLOCK, fId, (Block) fObj);

                if (!checkedForBlockItemCreator) {
                    for (Method method : registry.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(BlockItemFactory.class)) {
                            if (method.getParameterTypes().length != 1 || !Block.class.isAssignableFrom(method.getParameterTypes()[0]) || !Item.class.isAssignableFrom(method.getReturnType())) {
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
                        Registry.register(Registries.ITEM, fId, (Item) blockItemCreator.invoke(null, fObj));
                    } catch (Exception ignored) {
                    }
                }
            } else if (BlockEntityType.class.isAssignableFrom(fClass)) {
                Registry.register(Registries.BLOCK_ENTITY_TYPE, fId, (BlockEntityType<?>) fObj);
            } else if (EntityType.class.isAssignableFrom(fClass)) {
                Registry.register(Registries.ENTITY_TYPE, fId, (EntityType<?>) fObj);
            } else if (Enchantment.class.isAssignableFrom(fClass)) {
                Registry.register(Registries.ENCHANTMENT, fId, (Enchantment) fObj);
            } else if (ScreenHandlerType.class.isAssignableFrom(fClass)) {
                Registry.register(Registries.SCREEN_HANDLER_TYPE, fId, (ScreenHandlerType<?>) fObj);
            } else if (SoundEvent.class.isAssignableFrom(fClass)) {
                Registry.register(Registries.RECIPE_SERIALIZER, fId, (RecipeSerializer<?>) fObj);
            } else if (RecipeType.class.isAssignableFrom(fClass)) {
                Registry.register(Registries.RECIPE_TYPE, fId, (RecipeType<?>) fObj);
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
