package com.perseverance.item;

import com.perseverance.PerseveranceMod;
import java.util.function.Function;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public final class ModItems {
	public static final Item RED_NETHER_STAR = register(
			"red_nether_star",
			SummoningNetherStarItem::new,
			new Item.Properties().stacksTo(16)
	);
	public static final Item PERSEVERANCE_PROOF = register(
			"perseverance_proof",
			Item::new,
			new Item.Properties().stacksTo(64)
	);

	private ModItems() {
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
				.register(entries -> {
					entries.accept(RED_NETHER_STAR);
					entries.accept(PERSEVERANCE_PROOF);
				});
	}

	private static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties properties) {
		ResourceKey<Item> itemKey = ResourceKey.create(
				Registries.ITEM,
				Identifier.fromNamespaceAndPath(PerseveranceMod.MOD_ID, name)
		);
		T item = itemFactory.apply(properties.setId(itemKey));

		return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
	}
}
