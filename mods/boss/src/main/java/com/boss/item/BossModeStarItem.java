package com.boss.item;

import com.boss.power.BossModeManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public final class BossModeStarItem extends Item {
	public BossModeStarItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player user, InteractionHand hand) {
		if (!level.isClientSide() && user instanceof ServerPlayer serverPlayer) {
			BossModeManager.enter(serverPlayer);
		}

		user.awardStat(Stats.ITEM_USED.get(this));

		return InteractionResult.SUCCESS;
	}
}
