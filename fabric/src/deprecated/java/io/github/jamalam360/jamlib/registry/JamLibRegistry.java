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
import io.github.jamalam360.jamlib.registry.annotation.ContentRegistry;
import io.github.jamalam360.jamlib.registry.annotation.WithIdentifier;
import io.github.jamalam360.jamlib.registry.annotation.WithoutBlockItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.util.valueproviders.FloatProviderType;
import net.minecraft.util.valueproviders.IntProviderType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * <p>Used to automatically register fields in a 'registry class' via reflection.</p>
 *
 * <p>All vanilla registries are supported by default, and custom registries can also be registered.</p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Deprecated
public class JamLibRegistry {

	private static final Map<Class<?>, Registry<?>> REGISTRIES = new HashMap<>();
	private static final Map<ResourceKey<CreativeModeTab>, List<Item>> ITEM_GROUP_REGISTRATION_QUEUE = new HashMap<>();

	static {
		addRegistry(GameEvent.class, BuiltInRegistries.GAME_EVENT);
		addRegistry(SoundEvent.class, BuiltInRegistries.SOUND_EVENT);
		addRegistry(Fluid.class, BuiltInRegistries.FLUID);
		addRegistry(MobEffect.class, BuiltInRegistries.MOB_EFFECT);
		addRegistry(Block.class, BuiltInRegistries.BLOCK);
		addRegistry(Enchantment.class, BuiltInRegistries.ENCHANTMENT);
		addRegistry(EntityType.class, BuiltInRegistries.ENTITY_TYPE);
		addRegistry(Item.class, BuiltInRegistries.ITEM);
		addRegistry(Potion.class, BuiltInRegistries.POTION);
		addRegistry(ParticleType.class, BuiltInRegistries.PARTICLE_TYPE);
		addRegistry(BlockEntityType.class, BuiltInRegistries.BLOCK_ENTITY_TYPE);
		addRegistry(PaintingVariant.class, BuiltInRegistries.PAINTING_VARIANT);
		addRegistry(ResourceLocation.class, BuiltInRegistries.CUSTOM_STAT);
		addRegistry(ChunkStatus.class, BuiltInRegistries.CHUNK_STATUS);
		addRegistry(RuleTestType.class, BuiltInRegistries.RULE_TEST);
		addRegistry(PosRuleTestType.class, BuiltInRegistries.POS_RULE_TEST);
		addRegistry(MenuType.class, BuiltInRegistries.MENU);
		addRegistry(RecipeType.class, BuiltInRegistries.RECIPE_TYPE);
		addRegistry(RecipeSerializer.class, BuiltInRegistries.RECIPE_SERIALIZER);
		addRegistry(Attribute.class, BuiltInRegistries.ATTRIBUTE);
		addRegistry(PositionSourceType.class, BuiltInRegistries.POSITION_SOURCE_TYPE);
		addRegistry(ArgumentTypeInfo.class, BuiltInRegistries.COMMAND_ARGUMENT_TYPE);
		addRegistry(StatType.class, BuiltInRegistries.STAT_TYPE);
		addRegistry(VillagerType.class, BuiltInRegistries.VILLAGER_TYPE);
		addRegistry(VillagerProfession.class, BuiltInRegistries.VILLAGER_PROFESSION);
		addRegistry(PoiType.class, BuiltInRegistries.POINT_OF_INTEREST_TYPE);
		addRegistry(MemoryModuleType.class, BuiltInRegistries.MEMORY_MODULE_TYPE);
		addRegistry(SensorType.class, BuiltInRegistries.SENSOR_TYPE);
		addRegistry(Schedule.class, BuiltInRegistries.SCHEDULE);
		addRegistry(Activity.class, BuiltInRegistries.ACTIVITY);
		addRegistry(LootPoolEntryType.class, BuiltInRegistries.LOOT_POOL_ENTRY_TYPE);
		addRegistry(LootItemConditionType.class, BuiltInRegistries.LOOT_CONDITION_TYPE);
		addRegistry(LootNumberProviderType.class, BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE);
		addRegistry(LootNbtProviderType.class, BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE);
		addRegistry(LootScoreProviderType.class, BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE);
		addRegistry(FloatProviderType.class, BuiltInRegistries.FLOAT_PROVIDER_TYPE);
		addRegistry(IntProviderType.class, BuiltInRegistries.INT_PROVIDER_TYPE);
		addRegistry(HeightProviderType.class, BuiltInRegistries.HEIGHT_PROVIDER_TYPE);
		addRegistry(WorldCarver.class, BuiltInRegistries.CARVER);
		addRegistry(Feature.class, BuiltInRegistries.FEATURE);
		addRegistry(StructurePlacementType.class, BuiltInRegistries.STRUCTURE_PLACEMENT);
		addRegistry(StructurePieceType.class, BuiltInRegistries.STRUCTURE_PIECE);
		addRegistry(StructureType.class, BuiltInRegistries.STRUCTURE_TYPE);
		addRegistry(PlacementModifierType.class, BuiltInRegistries.PLACEMENT_MODIFIER_TYPE);
		addRegistry(BlockStateProviderType.class, BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE);
		addRegistry(FoliagePlacerType.class, BuiltInRegistries.FOLIAGE_PLACER_TYPE);
		addRegistry(TrunkPlacerType.class, BuiltInRegistries.TRUNK_PLACER_TYPE);
		addRegistry(RootPlacerType.class, BuiltInRegistries.ROOT_PLACER_TYPE);
		addRegistry(TreeDecoratorType.class, BuiltInRegistries.TREE_DECORATOR_TYPE);
		addRegistry(FeatureSizeType.class, BuiltInRegistries.FEATURE_SIZE_TYPE);
		addRegistry(TreeDecoratorType.class, BuiltInRegistries.TREE_DECORATOR_TYPE);
		addRegistry(StructurePoolElementType.class, BuiltInRegistries.STRUCTURE_POOL_ELEMENT);
		addRegistry(CatVariant.class, BuiltInRegistries.CAT_VARIANT);
		addRegistry(FrogVariant.class, BuiltInRegistries.FROG_VARIANT);
		addRegistry(BannerPattern.class, BuiltInRegistries.BANNER_PATTERN);
		addRegistry(Instrument.class, BuiltInRegistries.INSTRUMENT);
	}

	/**
	 * Registers a custom {@link Registry} so that it can be recognized when registering fields. All vanilla registries are supported by default.
	 *
	 * @param type     The type of the fields that should be registered to this {@link Registry} (e.g. {@code Block.class}).
	 * @param registry The {@link Registry} these fields should be registered to.
	 */
	@Deprecated
	public static <T> void addRegistry(Class<T> type, Registry<? extends T> registry) {
		REGISTRIES.put(type, registry);
	}

	/**
	 * Shorthand for processing multiple registry classes.
	 *
	 * @param registries A list of registry classes to process.
	 */
	@Deprecated
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
	@Deprecated
	public static void register(Class<?> registry) {
		if (!registry.isAnnotationPresent(ContentRegistry.class)) {
			JamLib.LOGGER.warn("@ContentRegistry annotation not present on registry class {}", registry.getName());
			return;
		}

		String modId = registry.getAnnotation(ContentRegistry.class).value();
		JamLibContentRegistry jlcr = null;

		if (JamLibContentRegistry.class.isAssignableFrom(registry)) {
			try {
				jlcr = (JamLibContentRegistry) registry.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				JamLib.LOGGER.warn("Couldn't create instance of registry {}, some features will not be available", registry.getName());
			}
		}

		for (Field f : registry.getFields()) {
			if (!f.canAccess(null)) {
				JamLib.LOGGER.warn("Cannot access field {} in registry class {}", f.getName(), registry.getName());
				continue;
			}

			Class<?> fClass = f.getType();
			Object fObj = null;

			try {
				fObj = f.get(null);
			} catch (IllegalAccessException ignored) {
			}

			ResourceLocation fId = getIdentifier(modId, f);

			for (Map.Entry<Class<?>, Registry<?>> e : REGISTRIES.entrySet()) {
				if (e.getKey().isAssignableFrom(fClass)) {
					Registry.register((Registry<Object>) e.getValue(), fId, fObj);
					break;
				}
			}

			if (jlcr != null && Item.class.isAssignableFrom(fClass)) {
				ResourceKey<CreativeModeTab> group = jlcr.getItemGroup((Item) fObj);
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

					Registry.register(BuiltInRegistries.ITEM, fId, item);

					ResourceKey<CreativeModeTab> group = jlcr.getItemGroup(item);
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
			}

			for (ResourceKey<CreativeModeTab> group : ITEM_GROUP_REGISTRATION_QUEUE.keySet()) {
				ItemGroupEvents.modifyEntriesEvent(group).register((content) -> {
					for (Item item : ITEM_GROUP_REGISTRATION_QUEUE.get(group)) {
						content.accept(item.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
					}
				});
			}
		}
	}

	private static ResourceLocation getIdentifier(String modId, Field f) {
		String path;

		if (f.isAnnotationPresent(WithIdentifier.class)) {
			path = f.getAnnotation(WithIdentifier.class).value();
		} else {
			path = f.getName().toLowerCase(Locale.ROOT);
		}

		return new ResourceLocation(modId, path);
	}
}
