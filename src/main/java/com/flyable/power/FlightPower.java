package com.flyable.power;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Abilities;

public final class FlightPower {
	private static final float FAST_FLYING_SPEED = 0.18F;
	private static final Map<UUID, AbilitySnapshot> ACTIVE_PLAYERS = new HashMap<>();

	private FlightPower() {
	}

	public static void initialize() {
		ServerTickEvents.END_SERVER_TICK.register(FlightPower::tick);
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> disable(handler.player, false));
		ServerLifecycleEvents.SERVER_STOPPING.register(FlightPower::disableAll);
	}

	public static void toggle(ServerPlayer player) {
		if (ACTIVE_PLAYERS.containsKey(player.getUUID())) {
			disable(player, true);
		} else {
			enable(player);
		}
	}

	private static void enable(ServerPlayer player) {
		Abilities abilities = player.getAbilities();
		ACTIVE_PLAYERS.put(player.getUUID(), AbilitySnapshot.from(abilities));

		abilities.mayfly = true;
		abilities.flying = true;
		abilities.setFlyingSpeed(FAST_FLYING_SPEED);
		player.onUpdateAbilities();
		player.resetFallDistance();
		player.displayClientMessage(Component.translatable("message.flyable.flight.enabled"), true);
	}

	private static void disable(ServerPlayer player, boolean notify) {
		AbilitySnapshot previous = ACTIVE_PLAYERS.remove(player.getUUID());

		if (previous == null) {
			return;
		}

		previous.applyTo(player.getAbilities());
		player.onUpdateAbilities();
		player.resetFallDistance();

		if (notify) {
			player.displayClientMessage(Component.translatable("message.flyable.flight.disabled"), true);
		}
	}

	private static void tick(MinecraftServer server) {
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			if (!ACTIVE_PLAYERS.containsKey(player.getUUID())) {
				continue;
			}

			Abilities abilities = player.getAbilities();
			boolean changed = !abilities.mayfly || !abilities.flying || abilities.getFlyingSpeed() != FAST_FLYING_SPEED;
			abilities.mayfly = true;
			abilities.flying = true;
			abilities.setFlyingSpeed(FAST_FLYING_SPEED);
			player.resetFallDistance();

			if (changed) {
				player.onUpdateAbilities();
			}
		}
	}

	private static void disableAll(MinecraftServer server) {
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			disable(player, false);
		}

		ACTIVE_PLAYERS.clear();
	}

	private record AbilitySnapshot(boolean mayfly, boolean flying, float flyingSpeed) {
		private static AbilitySnapshot from(Abilities abilities) {
			return new AbilitySnapshot(abilities.mayfly, abilities.flying, abilities.getFlyingSpeed());
		}

		private void applyTo(Abilities abilities) {
			abilities.mayfly = mayfly;
			abilities.flying = flying;
			abilities.setFlyingSpeed(flyingSpeed);
		}
	}
}
