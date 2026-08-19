package com.boss.block;

import com.boss.BossMod;
import java.util.function.Function;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class ModBlocks {
	public static final Block FAKE_EMERALD_BLOCK = registerFakeBlock(
			"fake_emerald_block",
			FakeEffectBlock.EffectKind.SLOWNESS,
			Blocks.EMERALD_BLOCK
	);
	public static final Block FAKE_REDSTONE_BLOCK = registerFakeBlock(
			"fake_redstone_block",
			FakeEffectBlock.EffectKind.FIRE,
			Blocks.REDSTONE_BLOCK
	);
	public static final Block FAKE_AMETHYST_BLOCK = registerFakeBlock(
			"fake_amethyst_block",
			FakeEffectBlock.EffectKind.MINING_FATIGUE,
			Blocks.AMETHYST_BLOCK
	);
	public static final Block FAKE_PINK_WOOL = registerFakeBlock(
			"fake_pink_wool",
			FakeEffectBlock.EffectKind.SLOWNESS,
			Blocks.PINK_WOOL
	);
	public static final Block FAKE_OBSIDIAN = registerFakeBlock(
			"fake_obsidian",
			FakeEffectBlock.EffectKind.NAUSEA,
			Blocks.OBSIDIAN
	);

	public static final BlockItem FAKE_EMERALD_BLOCK_ITEM = registerBlockItem("fake_emerald_block", FAKE_EMERALD_BLOCK);
	public static final BlockItem FAKE_REDSTONE_BLOCK_ITEM = registerBlockItem("fake_redstone_block", FAKE_REDSTONE_BLOCK);
	public static final BlockItem FAKE_AMETHYST_BLOCK_ITEM = registerBlockItem("fake_amethyst_block", FAKE_AMETHYST_BLOCK);
	public static final BlockItem FAKE_PINK_WOOL_ITEM = registerBlockItem("fake_pink_wool", FAKE_PINK_WOOL);
	public static final BlockItem FAKE_OBSIDIAN_ITEM = registerBlockItem("fake_obsidian", FAKE_OBSIDIAN);

	private ModBlocks() {
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
				.register(entries -> {
					entries.accept(FAKE_EMERALD_BLOCK_ITEM);
					entries.accept(FAKE_REDSTONE_BLOCK_ITEM);
					entries.accept(FAKE_AMETHYST_BLOCK_ITEM);
					entries.accept(FAKE_PINK_WOOL_ITEM);
					entries.accept(FAKE_OBSIDIAN_ITEM);
				});
	}

	private static Block registerFakeBlock(String name, FakeEffectBlock.EffectKind effectKind, Block sourceBlock) {
		return registerBlock(
				name,
				properties -> new FakeEffectBlock(effectKind, properties),
				BlockBehaviour.Properties.ofFullCopy(sourceBlock).noLootTable()
		);
	}

	private static <T extends Block> T registerBlock(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties properties) {
		ResourceKey<Block> blockKey = ResourceKey.create(
				Registries.BLOCK,
				Identifier.fromNamespaceAndPath(BossMod.MOD_ID, name)
		);
		T block = blockFactory.apply(properties.setId(blockKey));

		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
	}

	private static BlockItem registerBlockItem(String name, Block block) {
		ResourceKey<Item> itemKey = ResourceKey.create(
				Registries.ITEM,
				Identifier.fromNamespaceAndPath(BossMod.MOD_ID, name)
		);
		BlockItem item = new BlockItem(block, new Item.Properties().setId(itemKey));

		return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
	}
}
