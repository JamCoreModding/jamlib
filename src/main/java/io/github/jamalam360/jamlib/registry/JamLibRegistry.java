/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 Jamalam
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
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.command.argument.ArgumentTypeInfo;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer.Builder;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.CatType;
import net.minecraft.entity.passive.FrogType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Instrument;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.entry.LootPoolEntryType;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.provider.nbt.LootNbtProviderType;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.loot.provider.score.LootScoreProviderType;
import net.minecraft.particle.ParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.StatType;
import net.minecraft.structure.StructurePlacementType;
import net.minecraft.structure.StructureType;
import net.minecraft.structure.piece.StructurePieceType;
import net.minecraft.structure.pool.StructurePoolElementType;
import net.minecraft.structure.rule.PosRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.FloatProviderType;
import net.minecraft.util.math.intprovider.IntProviderType;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSourceType;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.decorator.PlacementModifierType;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.size.FeatureSizeType;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.heightprovider.HeightProviderType;
import net.minecraft.world.gen.root.RootPlacerType;
import net.minecraft.world.gen.stateprovider.BlockStateProviderType;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import net.minecraft.world.poi.PointOfInterestType;

/**
 * <p>Used to automatically register fields in a 'registry class' via reflection.</p>
 *
 * <p>All vanilla registries are supported by default, and custom registries can also be registered.</p>
 */
@SuppressWarnings({"unused", "unchecked"})
public class JamLibRegistry {

    private static final Map<Class<?>, Registry<?>> REGISTRIES = new HashMap<>();

    static {
        addRegistry(GameEvent.class, Registry.GAME_EVENT);
        addRegistry(SoundEvent.class, Registry.SOUND_EVENT);
        addRegistry(Fluid.class, Registry.FLUID);
        addRegistry(StatusEffect.class, Registry.STATUS_EFFECT);
        addRegistry(Block.class, Registry.BLOCK);
        addRegistry(Enchantment.class, Registry.ENCHANTMENT);
        addRegistry(EntityType.class, Registry.ENTITY_TYPE);
        addRegistry(Item.class, Registry.ITEM);
        addRegistry(Potion.class, Registry.POTION);
        addRegistry(ParticleType.class, Registry.PARTICLE_TYPE);
        addRegistry(BlockEntityType.class, Registry.BLOCK_ENTITY_TYPE);
        addRegistry(PaintingVariant.class, Registry.PAINTING_VARIANT);
        addRegistry(Identifier.class, Registry.CUSTOM_STAT);
        addRegistry(ChunkStatus.class, Registry.CHUNK_STATUS);
        addRegistry(RuleTest.class, Registry.RULE_TEST);
        addRegistry(PosRuleTest.class, Registry.POS_RULE_TEST);
        addRegistry(ScreenHandlerType.class, Registry.SCREEN_HANDLER);
        addRegistry(RecipeType.class, Registry.RECIPE_TYPE);
        addRegistry(RecipeSerializer.class, Registry.RECIPE_SERIALIZER);
        addRegistry(EntityAttributes.class, Registry.ATTRIBUTE);
        addRegistry(PositionSourceType.class, Registry.POSITION_SOURCE_TYPE);
        addRegistry(ArgumentTypeInfo.class, Registry.COMMAND_ARGUMENT_TYPE);
        addRegistry(StatType.class, Registry.STAT_TYPE);
        addRegistry(VillagerType.class, Registry.VILLAGER_TYPE);
        addRegistry(VillagerProfession.class, Registry.VILLAGER_PROFESSION);
        addRegistry(PointOfInterestType.class, Registry.POINT_OF_INTEREST_TYPE);
        addRegistry(MemoryModuleType.class, Registry.MEMORY_MODULE_TYPE);
        addRegistry(SensorType.class, Registry.SENSOR_TYPE);
        addRegistry(Schedule.class, Registry.SCHEDULE);
        addRegistry(Activity.class, Registry.ACTIVITY);
        addRegistry(LootPoolEntryType.class, Registry.LOOT_POOL_ENTRY_TYPE);
        addRegistry(LootFunctionType.class, Registry.LOOT_FUNCTION_TYPE);
        addRegistry(LootConditionType.class, Registry.LOOT_CONDITION_TYPE);
        addRegistry(LootNumberProviderType.class, Registry.LOOT_NUMBER_PROVIDER_TYPE);
        addRegistry(LootNbtProviderType.class, Registry.LOOT_NBT_PROVIDER_TYPE);
        addRegistry(LootScoreProviderType.class, Registry.LOOT_SCORE_PROVIDER_TYPE);
        addRegistry(FloatProviderType.class, Registry.FLOAT_PROVIDER_TYPE);
        addRegistry(IntProviderType.class, Registry.INT_PROVIDER_TYPE);
        addRegistry(HeightProviderType.class, Registry.HEIGHT_PROVIDER_TYPE);
        addRegistry(BlockPredicateType.class, Registry.BLOCK_PREDICATE_TYPE);
        addRegistry(Carver.class, Registry.CARVER);
        addRegistry(Feature.class, Registry.FEATURE);
        addRegistry(StructurePlacementType.class, Registry.STRUCTURE_PLACEMENT_TYPE);
        addRegistry(StructurePieceType.class, Registry.STRUCTURE_PIECE);
        addRegistry(StructureType.class, Registry.STRUCTURE_TYPE);
        addRegistry(PlacementModifierType.class, Registry.PLACEMENT_MODIFIER_TYPE);
        addRegistry(BlockStateProviderType.class, Registry.BLOCK_STATE_PROVIDER_TYPE);
        addRegistry(FoliagePlacerType.class, Registry.FOLIAGE_PLACER_TYPE);
        addRegistry(TrunkPlacerType.class, Registry.TRUNK_PLACER_TYPE);
        addRegistry(RootPlacerType.class, Registry.ROOT_PLACER_TYPE);
        addRegistry(TreeDecoratorType.class, Registry.TREE_DECORATOR_TYPE);
        addRegistry(FeatureSizeType.class, Registry.FEATURE_SIZE_TYPE);
        addRegistry(TreeDecoratorType.class, Registry.TREE_DECORATOR_TYPE);
        addRegistry(StructurePoolElementType.class, Registry.STRUCTURE_POOL_ELEMENT);
        addRegistry(CatType.class, Registry.CAT_VARIANT);
        addRegistry(FrogType.class, Registry.FROG_VARIANT);
        addRegistry(BannerPattern.class, Registry.BANNER_PATTERN);
        addRegistry(Instrument.class, Registry.INSTRUMENT);
    }

    /**
     * Registers a custom {@link Registry} so that it can be recognized when registering fields. All vanilla registries are supported by default.
     *
     * @param type     The type of the fields that should be registered to this {@link Registry} (e.g. {@code Block.class}).
     * @param registry The {@link Registry} these fields should be registered to.
     */
    public static void addRegistry(Class<?> type, Registry<?> registry) {
        REGISTRIES.put(type, registry);
    }

    /**
     * Shorthand for processing multiple registry classes.
     *
     * @param registries A list of registry classes to process.
     */
    public static void register(Class<?>... registries) {
        for (Class<?> clazz : registries) {
            register(clazz);
        }
    }

    /**
     * <p>Processes a registry class, registering all valid fields in it. This should be called at mod initialization.</p>
     *
     * <p>Fields may have additional processing outside of just registering them, for example {@link Block}s can have items registered (via {@link BlockItemFactory}),
     * and {@link EntityType}'s can have their attributes automatically registered if they define a method that takes no parameters and returns an
     * {@link DefaultAttributeContainer.Builder}.</p>
     *
     * <p>The {@code registry} must be annotated with {@link ContentRegistry}.</p>
     *
     * @param registry The registry class to process.
     */
    public static void register(Class<?> registry) {
        if (!registry.isAnnotationPresent(ContentRegistry.class)) {
            JamLib.LOGGER.warn("@ContentRegistry annotation not present on registry class", registry.getName());
            return;
        }

        String modId = registry.getAnnotation(ContentRegistry.class).value();
        boolean checkedForBlockItemCreator = false;
        Method blockItemCreator = null;

        for (Field f : registry.getFields()) {
            if (!f.canAccess(null)) {
                JamLib.LOGGER.warn("Cannot access field", f.getName(), "in registry class", registry.getName());
                continue;
            }

            Class<?> fClass = f.getType();
            Object fObj = null;

            try {
                fObj = f.get(null);
            } catch (IllegalAccessException ignored) {
            }

            Identifier fId = getIdentifier(modId, f);

            for (Map.Entry<Class<?>, Registry<?>> e : REGISTRIES.entrySet()) {
                if (e.getKey().isAssignableFrom(fClass)) {
                    Registry.register((Registry<Object>) e.getValue(), fId, fObj);
                    break;
                }
            }

            if (Block.class.isAssignableFrom(fClass)) {
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
                        Registry.register(Registry.ITEM, fId, (Item) blockItemCreator.invoke(null, fObj));
                    } catch (Exception ignored) {
                    }
                }
            }

            if (EntityType.class.isAssignableFrom(fClass)) {
                Class<? extends Entity> entityClass = null;
                try {
                    entityClass = (Class<? extends Entity>) Class.forName(((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0].getTypeName());
                } catch (ClassNotFoundException e) {
                    JamLib.LOGGER.warn("Encountered error registering entity attributes. This should absolutely be reported. Reflection was a mistake...");
                    JamLib.LOGGER.warn(e.toString());
                }

                if (entityClass != null) {
                    for (Method method : entityClass.getMethods()) {
                        if (method.getDeclaringClass() == entityClass && Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0 && method.getReturnType() == DefaultAttributeContainer.Builder.class) {
                            JamLib.DEV_LOGGER.info("Registering entity attributes for", entityClass.getName(), "automatically");

                            try {
                                FabricDefaultAttributeRegistry.register((EntityType<? extends LivingEntity>) fObj, (Builder) method.invoke(null));
                            } catch (Exception e) {
                                JamLib.LOGGER.warn("Failed to register entity attributes for", entityClass.getName());
                                JamLib.LOGGER.warn(e.toString());
                            }

                            break;
                        }
                    }
                }
            }
        }
    }

    private static Identifier getIdentifier(String modId, Field f) {
        String path;

        if (f.isAnnotationPresent(WithIdentifier.class)) {
            path = f.getAnnotation(WithIdentifier.class).value();
        } else {
            path = f.getName().toLowerCase(Locale.ROOT);
        }

        return new Identifier(modId, path);
    }
}
