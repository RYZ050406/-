package com.boss.event;

import com.boss.block.ModBlocks;
import com.boss.item.BossSwordItem;
import com.boss.item.DogKingSwordItem;
import com.boss.item.ModItems;
import com.boss.power.BossModeManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public final class BossTickHandler {
	private static final int RESISTANCE_REFRESH_TICKS = 40;
	private static final int FAKE_BLOCK_EFFECT_TICKS = 20;

	private BossTickHandler() {
	}

	public static void initialize() {
		ServerTickEvents.END_SERVER_TICK.register(BossTickHandler::onServerTick);
	}

	private static void onServerTick(MinecraftServer server) {
		BossModeManager.tick(server);

		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			applyBootEffects(player);
			applyTouchedFakeBlockEffects(player);
			showHeldSwordCooldown(player);
		}
	}

	private static void applyBootEffects(ServerPlayer player) {
		ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);

		if (feet.is(ModItems.GOD_NETHERITE_BOOTS)) {
			player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, RESISTANCE_REFRESH_TICKS, 1, false, false, true));
		}
	}

	private static void applyTouchedFakeBlockEffects(ServerPlayer player) {
		AABB contactBox = player.getBoundingBox().deflate(0.001D, 0.0D, 0.001D).move(0.0D, -0.05D, 0.0D);

		for (BlockPos pos : BlockPos.betweenClosed(contactBox)) {
			BlockState state = player.level().getBlockState(pos);

			if (state.is(ModBlocks.FAKE_EMERALD_BLOCK) || state.is(ModBlocks.FAKE_PINK_WOOL)) {
				player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, FAKE_BLOCK_EFFECT_TICKS, 0, false, false, true));
			} else if (state.is(ModBlocks.FAKE_REDSTONE_BLOCK)) {
				player.igniteForSeconds(1.0F);
			} else if (state.is(ModBlocks.FAKE_AMETHYST_BLOCK)) {
				player.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, FAKE_BLOCK_EFFECT_TICKS, 0, false, false, true));
			} else if (state.is(ModBlocks.FAKE_OBSIDIAN)) {
				player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, FAKE_BLOCK_EFFECT_TICKS, 0, false, false, true));
			}
		}
	}

	private static void showHeldSwordCooldown(ServerPlayer player) {
		ItemStack stack = player.getMainHandItem();

		if (BossSwordItem.isBossSword(stack)) {
			player.displayClientMessage(BossSwordItem.cooldownMessage(stack, System.currentTimeMillis()), true);
			return;
		}

		if (DogKingSwordItem.isDogKingSword(stack)) {
			player.displayClientMessage(DogKingSwordItem.cooldownMessage(stack, System.currentTimeMillis()), true);
			return;
		}

		stack = player.getOffhandItem();

		if (BossSwordItem.isBossSword(stack)) {
			player.displayClientMessage(BossSwordItem.cooldownMessage(stack, System.currentTimeMillis()), true);
		} else if (DogKingSwordItem.isDogKingSword(stack)) {
			player.displayClientMessage(DogKingSwordItem.cooldownMessage(stack, System.currentTimeMillis()), true);
		}
	}
}