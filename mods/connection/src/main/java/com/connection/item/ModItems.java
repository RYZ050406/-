package com.connection.item;

import com.connection.ConnectionMod;
import java.util.function.Function;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
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
	public static final Item SUPER_POOP = register(
			"super_poop",
			SuperPoopItem::new,
			new Item.Properties()
					.stacksTo(64)
					.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
					.component(DataComponents.ITEM_NAME, Component.literal("超级答辩").withStyle(style -> style.withColor(0x8B5A2B).withBold(true)))
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
					entries.accept(SUPER_POOP);
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
