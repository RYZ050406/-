package com.connection.recipe;

import com.connection.item.ModItems;
import com.connection.tags.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public final class FruitVegetablePowderRecipe extends CustomRecipe {
	public static final int POOP_COST = 87;
	private static final int FRUIT_SLOT_COUNT = 3;
	private static final int VEGETABLE_SLOT_COUNT = 3;
	private static final int POOP_SLOT_COUNT = 1;
	private static final int TOTAL_SLOT_COUNT = FRUIT_SLOT_COUNT + VEGETABLE_SLOT_COUNT + POOP_SLOT_COUNT;

	public FruitVegetablePowderRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		return matchesInput(input);
	}

	public static boolean matchesInput(CraftingInput input) {
		int fruits = 0;
		int vegetables = 0;
		int poopSlots = 0;
		int occupiedSlots = 0;

		for (ItemStack stack : input.items()) {
			if (stack.isEmpty()) {
				continue;
			}

			occupiedSlots++;

			if (stack.is(ModItems.POOP)) {
				if (stack.getCount() < POOP_COST) {
					return false;
				}

				poopSlots++;
				continue;
			}

			if (stack.is(ModItemTags.FRUITS)) {
				fruits++;
				continue;
			}

			if (stack.is(ModItemTags.VEGETABLES)) {
				vegetables++;
				continue;
			}

			return false;
		}

		return occupiedSlots == TOTAL_SLOT_COUNT
				&& fruits == FRUIT_SLOT_COUNT
				&& vegetables == VEGETABLE_SLOT_COUNT
				&& poopSlots == POOP_SLOT_COUNT;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		return new ItemStack(ModItems.FRUIT_VEGETABLE_POWDER);
	}

	@Override
	public RecipeSerializer<FruitVegetablePowderRecipe> getSerializer() {
		return ModRecipeSerializers.FRUIT_VEGETABLE_POWDER;
	}
}
