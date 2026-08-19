package com.connection.item;

import com.connection.ConnectionMod;
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
	public static final Item POOP = register(
			"poop",
			PoopItem::new,
			new Item.Properties().stacksTo(87)
	);
	public static final Item FRUIT_VEGETABLE_POWDER = register(
			"fruit_vegetable_powder",
			Item::new,
			new Item.Properties().stacksTo(64)
	);

	private ModItems() {
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
				.register(entries -> {
					entries.accept(POOP);
					entries.accept(FRUIT_VEGETABLE_POWDER);
				});
	}

	private static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties properties) {
		ResourceKey<Item> itemKey = ResourceKey.create(
				Registries.ITEM,
				Identifier.fromNamespaceAndPath(ConnectionMod.MOD_ID, name)
		);
		T item = itemFactory.apply(properties.setId(itemKey));

		return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
	}
}
