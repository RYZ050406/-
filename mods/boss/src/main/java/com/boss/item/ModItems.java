package com.boss.item;

import com.boss.BossMod;
import com.boss.block.ModBlocks;
import java.util.function.Function;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;

public final class ModItems {
	private static final float TOTAL_ATTACK_DAMAGE = 5.0F;
	private static final float DOG_KING_ATTACK_DAMAGE = 10.0F;
	private static final float WOOD_BASE_ATTACK_DAMAGE = 1.0F;
	private static final float NO_COOLDOWN_ATTACK_SPEED = 1024.0F;
	private static final float VANILLA_SWORD_ATTACK_SPEED = -2.4F;
	private static final double DOG_KING_SPEED_BONUS = 0.05D;
	private static final double LEATHER_ARMOR_BONUS = 6.0D;
	private static final double NETHERITE_ARMOR_BONUS = 20.0D;
	private static final double LEATHER_SPEED_BONUS = 0.03D;
	private static final double NETHERITE_SPEED_BONUS = 0.05D;
	private static final double JUMP_TWO_BLOCKS_BONUS = 0.26D;

	public static final Item SWORD_ONE = register(
			"sword_one",
			properties -> new BossSwordItem(ModBlocks.FAKE_EMERALD_BLOCK, properties),
			swordProperties("剑一", ChatFormatting.GREEN)
	);
	public static final Item SWORD_TWO = register(
			"sword_two",
			properties -> new BossSwordItem(ModBlocks.FAKE_REDSTONE_BLOCK, properties),
			swordProperties("剑二", ChatFormatting.RED)
	);
	public static final Item SWORD_THREE = register(
			"sword_three",
			properties -> new BossSwordItem(ModBlocks.FAKE_AMETHYST_BLOCK, properties),
			swordProperties("剑三", ChatFormatting.LIGHT_PURPLE)
	);
	public static final Item SWORD_FOUR = register(
			"sword_four",
			properties -> new BossSwordItem(ModBlocks.FAKE_PINK_WOOL, properties),
			swordProperties("剑四", ChatFormatting.LIGHT_PURPLE)
	);
	public static final Item SWORD_FIVE = register(
			"sword_five",
			properties -> new BossSwordItem(ModBlocks.FAKE_OBSIDIAN, properties),
			swordProperties("剑五", ChatFormatting.BLACK)
	);
	public static final Item DOG_KING_SWORD = register(
			"dog_king_sword",
			DogKingSwordItem::new,
			dogKingSwordProperties()
	);
	public static final Item LEAPING_LEATHER_BOOTS = register(
			"leaping_leather_boots",
			Item::new,
			bootsProperties(
					Component.literal("飞跃皮革靴子").withStyle(ChatFormatting.GREEN),
					ArmorMaterials.LEATHER,
					LEATHER_ARMOR_BONUS,
					LEATHER_SPEED_BONUS
			).component(DataComponents.DYED_COLOR, new DyedItemColor(0x2fbf62))
	);
	public static final Item GOD_NETHERITE_BOOTS = register(
			"god_netherite_boots",
			Item::new,
			bootsProperties(
					Component.literal("神速下界合金靴子").withStyle(ChatFormatting.DARK_PURPLE),
					ArmorMaterials.NETHERITE,
					NETHERITE_ARMOR_BONUS,
					NETHERITE_SPEED_BONUS
			).fireResistant()
	);
	public static final Item BOSS_MODE_STAR = register(
			"boss_mode_star",
			BossModeStarItem::new,
			new Item.Properties()
					.stacksTo(1)
					.component(DataComponents.ITEM_NAME, Component.literal("古希腊掌管起飞的神").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD))
	);

	private ModItems() {
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
				.register(entries -> {
					entries.accept(SWORD_ONE);
					entries.accept(SWORD_TWO);
					entries.accept(SWORD_THREE);
					entries.accept(SWORD_FOUR);
					entries.accept(SWORD_FIVE);
					entries.accept(DOG_KING_SWORD);
					entries.accept(LEAPING_LEATHER_BOOTS);
					entries.accept(GOD_NETHERITE_BOOTS);
				});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
				.register(entries -> entries.accept(BOSS_MODE_STAR));
	}

	private static Item.Properties swordProperties(String name, ChatFormatting color) {
		return new Item.Properties()
				.sword(ToolMaterial.WOOD, TOTAL_ATTACK_DAMAGE - WOOD_BASE_ATTACK_DAMAGE, NO_COOLDOWN_ATTACK_SPEED)
				.component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
				.component(DataComponents.MINIMUM_ATTACK_CHARGE, 0.0F)
				.component(DataComponents.ITEM_NAME, Component.literal(name).withStyle(color, ChatFormatting.BOLD));
	}

	private static Item.Properties dogKingSwordProperties() {
		return new Item.Properties()
				.sword(ToolMaterial.WOOD, DOG_KING_ATTACK_DAMAGE - WOOD_BASE_ATTACK_DAMAGE, VANILLA_SWORD_ATTACK_SPEED)
				.attributes(dogKingSwordAttributes())
				.component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
				.component(DataComponents.ITEM_NAME, Component.literal("狗王剑")
						.withStyle(style -> style.withColor(0x8B5A2B).withBold(true).withItalic(true)));
	}

	private static ItemAttributeModifiers dogKingSwordAttributes() {
		return ItemAttributeModifiers.builder()
				.add(
						Attributes.ATTACK_DAMAGE,
						new AttributeModifier(Identifier.fromNamespaceAndPath(BossMod.MOD_ID, "dog_king_attack_damage"), DOG_KING_ATTACK_DAMAGE - WOOD_BASE_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND
				)
				.add(
						Attributes.ATTACK_SPEED,
						new AttributeModifier(Identifier.fromNamespaceAndPath(BossMod.MOD_ID, "dog_king_attack_speed"), VANILLA_SWORD_ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND
				)
				.add(
						Attributes.MOVEMENT_SPEED,
						new AttributeModifier(Identifier.fromNamespaceAndPath(BossMod.MOD_ID, "dog_king_movement_speed"), DOG_KING_SPEED_BONUS, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND
				)
				.build();
	}

	private static Item.Properties bootsProperties(Component name, net.minecraft.world.item.equipment.ArmorMaterial material, double armor, double speed) {
		return new Item.Properties()
				.humanoidArmor(material, ArmorType.BOOTS)
				.attributes(bootsAttributes(armor, speed))
				.component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
				.component(DataComponents.ITEM_NAME, name);
	}

	private static ItemAttributeModifiers bootsAttributes(double armor, double speed) {
		return ItemAttributeModifiers.builder()
				.add(
						Attributes.ARMOR,
						new AttributeModifier(Identifier.fromNamespaceAndPath(BossMod.MOD_ID, "boots_armor"), armor, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.FEET
				)
				.add(
						Attributes.JUMP_STRENGTH,
						new AttributeModifier(Identifier.fromNamespaceAndPath(BossMod.MOD_ID, "boots_jump_strength"), JUMP_TWO_BLOCKS_BONUS, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.FEET
				)
				.add(
						Attributes.MOVEMENT_SPEED,
						new AttributeModifier(Identifier.fromNamespaceAndPath(BossMod.MOD_ID, "boots_movement_speed"), speed, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.FEET
				)
				.build();
	}

	private static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties properties) {
		ResourceKey<Item> itemKey = ResourceKey.create(
				Registries.ITEM,
				Identifier.fromNamespaceAndPath(BossMod.MOD_ID, name)
		);
		T item = itemFactory.apply(properties.setId(itemKey));

		return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
	}
}