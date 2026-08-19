package com.connection.entity;

import com.connection.ConnectionMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {
	public static final EntityType<ThrownPoopEntity> THROWN_POOP = registerThrownPoop();

	private ModEntities() {
	}

	public static void initialize() {
	}

	private static EntityType<ThrownPoopEntity> registerThrownPoop() {
		ResourceKey<EntityType<?>> entityKey = ResourceKey.create(
				Registries.ENTITY_TYPE,
				Identifier.fromNamespaceAndPath(ConnectionMod.MOD_ID, "thrown_poop")
		);
		EntityType<ThrownPoopEntity> entityType = EntityType.Builder
				.<ThrownPoopEntity>of(ThrownPoopEntity::new, MobCategory.MISC)
				.sized(0.25F, 0.25F)
				.clientTrackingRange(4)
				.updateInterval(10)
				.noLootTable()
				.build(entityKey);

		return Registry.register(BuiltInRegistries.ENTITY_TYPE, entityKey, entityType);
	}
}
