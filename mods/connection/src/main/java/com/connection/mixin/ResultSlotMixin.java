package com.connection.mixin;

import com.connection.item.ModItems;
import com.connection.recipe.FruitVegetablePowderRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public abstract class ResultSlotMixin {
	@Shadow
	@Final
	private CraftingContainer craftSlots;

	@Inject(method = "onTake", at = @At("HEAD"))
	private void connection$consumeExtraPoop(Player player, ItemStack result, CallbackInfo ci) {
		if (!result.is(ModItems.FRUIT_VEGETABLE_POWDER)) {
			return;
		}

		CraftingInput input = craftSlots.asCraftInput();

		if (!FruitVegetablePowderRecipe.matchesInput(input)) {
			return;
		}

		for (int slot = 0; slot < craftSlots.getContainerSize(); slot++) {
			ItemStack stack = craftSlots.getItem(slot);

			if (stack.is(ModItems.POOP) && stack.getCount() >= FruitVegetablePowderRecipe.POOP_COST) {
				craftSlots.removeItem(slot, FruitVegetablePowderRecipe.POOP_COST - 1);
				return;
			}
		}
	}
}
