package com.connection.recipe;

import com.connection.ConnectionMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class ModRecipeSerializers {
	public static final RecipeSerializer<FruitVegetablePowderRecipe> FRUIT_VEGETABLE_POWDER = Registry.register(
			BuiltInRegistries.RECIPE_SERIALIZER,
			Identifier.fromNamespaceAndPath(ConnectionMod.MOD_ID, "fruit_vegetable_powder"),
			new CustomRecipe.Serializer<>(FruitVegetablePowderRecipe::new)
	);

	private ModRecipeSerializers() {
	}

	public static void initialize() {
	}
}
