package com.perseverance.entity;

import com.perseverance.PerseveranceMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {
	public static final EntityType<TakeoffSupermanEntity> TAKEOFF_SUPERMAN = registerTakeoffSuperman();

	private ModEntities() {
	}

	public static void initialize() {
		FabricDefaultAttributeRegistry.register(TAKEOFF_SUPERMAN, TakeoffSupermanEntity.createAttributes());
	}

	private static EntityType<TakeoffSupermanEntity> registerTakeoffSuperman() {
		ResourceKey<EntityType<?>> entityKey = ResourceKey.create(
				Registries.ENTITY_TYPE,
				Identifier.fromNamespaceAndPath(PerseveranceMod.MOD_ID, "takeoff_superman")
		);
		EntityType<TakeoffSupermanEntity> entityType = EntityType.Builder
				.of(TakeoffSupermanEntity::new, MobCategory.MONSTER)
				.sized(0.6F, 1.8F)
				.eyeHeight(1.62F)
				.clientTrackingRange(10)
				.updateInterval(3)
				.noLootTable()
				.build(entityKey);

		return Registry.register(BuiltInRegistries.ENTITY_TYPE, entityKey, entityType);
	}
}
