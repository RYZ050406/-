package com.flyable.item;

import com.flyable.power.FlightPower;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public final class TemporaryFlightSlimeBallItem extends Item {
	public TemporaryFlightSlimeBallItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player user, InteractionHand hand) {
		if (level.isClientSide()) {
			return InteractionResult.PASS;
		}

		if (user instanceof ServerPlayer player) {
			FlightPower.toggleTemporary(player);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player user = context.getPlayer();

		if (context.getLevel().isClientSide()) {
			return InteractionResult.PASS;
		}

		if (user instanceof ServerPlayer player) {
			FlightPower.toggleTemporary(player);
		}

		return InteractionResult.SUCCESS;
	}
}
