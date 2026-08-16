package com.flyable.item;

import com.flyable.FlyableMod;
import com.flyable.power.FlightPower;
import java.util.function.Function;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public final class ModItems {
	public static final Item FLIGHT_SLIME_BALL = register(
			"flight_slime_ball",
			FlightSlimeBallItem::new,
			new Item.Properties().stacksTo(1)
	);

	private ModItems() {
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
				.register(entries -> entries.accept(FLIGHT_SLIME_BALL));
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			if (!player.getItemInHand(hand).is(FLIGHT_SLIME_BALL)) {
				return InteractionResult.PASS;
			}

			if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
				FlightPower.toggle(serverPlayer);
			}

			return InteractionResult.SUCCESS;
		});
	}

	private static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties properties) {
		ResourceKey<Item> itemKey = ResourceKey.create(
				Registries.ITEM,
				Identifier.fromNamespaceAndPath(FlyableMod.MOD_ID, name)
		);
		T item = itemFactory.apply(properties.setId(itemKey));

		return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
	}
}
