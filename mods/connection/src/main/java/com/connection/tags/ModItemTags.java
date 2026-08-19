package com.connection.tags;

import com.connection.ConnectionMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModItemTags {
	public static final TagKey<Item> FRUITS = create("fruits");
	public static final TagKey<Item> VEGETABLES = create("vegetables");

	private ModItemTags() {
	}

	private static TagKey<Item> create(String name) {
		return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ConnectionMod.MOD_ID, name));
	}
}
