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
import io.github.jamalam360.jamlib.registry.annotation.ContentRegistry;
import io.github.jamalam360.jamlib.registry.annotation.WithIdentifier;
import io.github.jamalam360.jamlib.registry.annotation.WithoutBlockItem;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.entry.LootPoolEntryType;
import net.minecraft.loot.provider.nbt.LootNbtProviderType;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.loot.provider.score.LootScoreProviderType;
import net.minecraft.particle.ParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
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
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSourceType;
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
    private static final Map<ItemGroup, List<Item>> ITEM_GROUP_REGISTRATION_QUEUE = new HashMap<>();

    static {
        addRegistry(GameEvent.class, Registries.GAME_EVENT);
        addRegistry(SoundEvent.class, Registries.SOUND_EVENT);
        addRegistry(Fluid.class, Registries.FLUID);
        addRegistry(StatusEffect.class, Registries.STATUS_EFFECT);
        addRegistry(Block.class, Registries.BLOCK);
        addRegistry(Enchantment.class, Registries.ENCHANTMENT);
        addRegistry(EntityType.class, Registries.ENTITY_TYPE);
        addRegistry(Item.class, Registries.ITEM);
        addRegistry(Potion.class, Registries.POTION);
        addRegistry(ParticleType.class, Registries.PARTICLE_TYPE);
        addRegistry(BlockEntityType.class, Registries.BLOCK_ENTITY_TYPE);
        addRegistry(PaintingVariant.class, Registries.PAINTING_VARIANT);
        addRegistry(Identifier.class, Registries.CUSTOM_STAT);
        addRegistry(ChunkStatus.class, Registries.CHUNK_STATUS);
        addRegistry(RuleTest.class, Registries.RULE_TEST_TYPE);
        addRegistry(PosRuleTest.class, Registries.POS_RULE_TEST_TYPE);
        addRegistry(ScreenHandlerType.class, Registries.SCREEN_HANDLER_TYPE);
        addRegistry(RecipeType.class, Registries.RECIPE_TYPE);
        addRegistry(RecipeSerializer.class, Registries.RECIPE_SERIALIZER);
        addRegistry(EntityAttributes.class, Registries.ENTITY_ATTRIBUTE);
        addRegistry(PositionSourceType.class, Registries.POSITION_SOURCE_TYPE);
        addRegistry(ArgumentTypeInfo.class, Registries.COMMAND_ARGUMENT_TYPE);
        addRegistry(StatType.class, Registries.STAT_TYPE);
        addRegistry(VillagerType.class, Registries.VILLAGER_TYPE);
        addRegistry(VillagerProfession.class, Registries.VILLAGER_PROFESSION);
        addRegistry(PointOfInterestType.class, Registries.POINT_OF_INTEREST_TYPE);
        addRegistry(MemoryModuleType.class, Registries.MEMORY_MODULE_TYPE);
        addRegistry(SensorType.class, Registries.SENSOR_TYPE);
        addRegistry(Schedule.class, Registries.SCHEDULE);
        addRegistry(Activity.class, Registries.ACTIVITY);
        addRegistry(LootPoolEntryType.class, Registries.LOOT_POOL_ENTRY_TYPE);
        addRegistry(LootConditionType.class, Registries.LOOT_CONDITION_TYPE);
        addRegistry(LootNumberProviderType.class, Registries.LOOT_NUMBER_PROVIDER_TYPE);
        addRegistry(LootNbtProviderType.class, Registries.LOOT_NBT_PROVIDER_TYPE);
        addRegistry(LootScoreProviderType.class, Registries.LOOT_SCORE_PROVIDER_TYPE);
        addRegistry(FloatProviderType.class, Registries.FLOAT_PROVIDER_TYPE);
        addRegistry(IntProviderType.class, Registries.INT_PROVIDER_TYPE);
        addRegistry(HeightProviderType.class, Registries.HEIGHT_PROVIDER_TYPE);
        addRegistry(Carver.class, Registries.CARVER_WORLDGEN);
        addRegistry(Feature.class, Registries.FEATURE_WORLDGEN);
        addRegistry(StructurePlacementType.class, Registries.STRUCTURE_PLACEMENT_TYPE);
        addRegistry(StructurePieceType.class, Registries.STRUCTURE_PIECE_TYPE);
        addRegistry(StructureType.class, Registries.STRUCTURE_TYPE);
        addRegistry(PlacementModifierType.class, Registries.PLACEMENT_MODIFIER_TYPE);
        addRegistry(BlockStateProviderType.class, Registries.BLOCK_STATE_PROVIDER_TYPE);
        addRegistry(FoliagePlacerType.class, Registries.FOLIAGE_PLACER_TYPE);
        addRegistry(TrunkPlacerType.class, Registries.TRUNK_PLACER_TYPE);
        addRegistry(RootPlacerType.class, Registries.ROOT_PLACER_TYPE);
        addRegistry(TreeDecoratorType.class, Registries.TREE_DECORATOR_TYPE);
        addRegistry(FeatureSizeType.class, Registries.FEATURE_SIZE_TYPE);
        addRegistry(TreeDecoratorType.class, Registries.TREE_DECORATOR_TYPE);
        addRegistry(StructurePoolElementType.class, Registries.STRUCTURE_POOL_ELEMENT_TYPE);
        addRegistry(CatVariant.class, Registries.CAT_VARIANT);
        addRegistry(FrogVariant.class, Registries.FROG_VARIANT);
        addRegistry(BannerPattern.class, Registries.BANNER_PATTERN);
        addRegistry(Instrument.class, Registries.INSTRUMENT);
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
     * <p>Fields may have additional processing outside of just registering them, for example {@link Block}s can have items registered (via {@link JamLibContentRegistry}),
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
        JamLibContentRegistry jlcr = null;

        if (JamLibContentRegistry.class.isAssignableFrom(registry)) {
            try {
                jlcr = (JamLibContentRegistry) registry.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                JamLib.LOGGER.warn("Couldn't create instance of registry", registry.getName(), ", some features will not be available");
            }
        }

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

            if (jlcr != null && Item.class.isAssignableFrom(fClass)) {
                ItemGroup group = jlcr.getItemGroup((Item) fObj);
                if (group == null) {
                    continue;
                }

                List<Item> l = ITEM_GROUP_REGISTRATION_QUEUE.getOrDefault(group, new ArrayList<>());
                l.add((Item) fObj);
                ITEM_GROUP_REGISTRATION_QUEUE.put(group, l);
            }

            if (Block.class.isAssignableFrom(fClass)) {
                if (jlcr != null && !f.isAnnotationPresent(WithoutBlockItem.class)) {
                    Item item = jlcr.createBlockItem((Block) fObj);
                    if (item == null) {
                        continue;
                    }

                    Registry.register(Registries.ITEM, fId, item);

                    ItemGroup group = jlcr.getItemGroup(item);
                    if (group == null) {
                        continue;
                    }

                    List<Item> l = ITEM_GROUP_REGISTRATION_QUEUE.getOrDefault(group, new ArrayList<>());
                    l.add(item);
                    ITEM_GROUP_REGISTRATION_QUEUE.put(group, l);
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

        for (ItemGroup group : ITEM_GROUP_REGISTRATION_QUEUE.keySet()) {
            ItemGroupEvents.modifyEntriesEvent(group).register((content) -> {
                for (Item item : ITEM_GROUP_REGISTRATION_QUEUE.get(group)) {
                    content.addStack(item.getDefaultStack());
                }
            });
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
