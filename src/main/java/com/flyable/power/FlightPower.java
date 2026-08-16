package com.flyable.power;

import com.flyable.item.ModItems;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public final class FlightPower {
	private static final float FAST_FLYING_SPEED = 0.18F;
	private static final int TEMPORARY_DURATION_TICKS = 10 * 20;
	private static final int TEMPORARY_COOLDOWN_TICKS = 30 * 20;
	private static final Map<UUID, ActiveFlight> ACTIVE_PLAYERS = new HashMap<>();
	private static final Map<UUID, Integer> TEMPORARY_COOLDOWNS = new HashMap<>();

	private FlightPower() {
	}

	public static void initialize() {
		ServerTickEvents.END_SERVER_TICK.register(FlightPower::tick);
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> disableForDisconnect(handler.player));
		ServerLifecycleEvents.SERVER_STOPPING.register(FlightPower::disableAll);
	}

	public static void togglePermanent(ServerPlayer player) {
		ActiveFlight current = ACTIVE_PLAYERS.get(player.getUUID());

		if (current == null) {
			enable(player, FlightMode.PERMANENT, 0);
			player.displayClientMessage(Component.translatable("message.flyable.flight.enabled"), true);
			return;
		}

		if (current.mode == FlightMode.PERMANENT) {
			disable(player, true, false);
		} else {
			disable(player, false, true);
			enable(player, FlightMode.PERMANENT, 0);
			player.displayClientMessage(Component.translatable("message.flyable.flight.enabled"), true);
		}
	}

	public static void toggleTemporary(ServerPlayer player) {
		ActiveFlight current = ACTIVE_PLAYERS.get(player.getUUID());

		if (current != null) {
			if (current.mode == FlightMode.TEMPORARY) {
				disable(player, true, true);
			} else {
				player.displayClientMessage(Component.translatable("message.flyable.temporary_flight.already_permanent"), true);
			}

			return;
		}

		int cooldownTicks = getTemporaryCooldownTicks(player);

		if (cooldownTicks > 0) {
			syncTemporaryCooldown(player, cooldownTicks);
			player.displayClientMessage(
					Component.translatable("message.flyable.temporary_flight.cooldown", ticksToSeconds(cooldownTicks)),
					true
			);
			return;
		}

		enable(player, FlightMode.TEMPORARY, TEMPORARY_DURATION_TICKS);
		player.displayClientMessage(Component.translatable("message.flyable.temporary_flight.enabled"), true);
	}

	private static void enable(ServerPlayer player, FlightMode mode, int ticksRemaining) {
		Abilities abilities = player.getAbilities();
		ACTIVE_PLAYERS.put(player.getUUID(), new ActiveFlight(mode, AbilitySnapshot.from(player), ticksRemaining));

		abilities.mayfly = true;
		abilities.flying = true;
		abilities.setFlyingSpeed(FAST_FLYING_SPEED);
		player.onUpdateAbilities();
		player.resetFallDistance();
	}

	private static void disable(ServerPlayer player, boolean notify, boolean startTemporaryCooldown) {
		ActiveFlight activeFlight = ACTIVE_PLAYERS.remove(player.getUUID());

		if (activeFlight == null) {
			return;
		}

		activeFlight.previousAbilities.applyTo(player);
		player.onUpdateAbilities();
		player.resetFallDistance();

		if (startTemporaryCooldown) {
			startTemporaryCooldown(player, notify);
		} else if (notify) {
			player.displayClientMessage(Component.translatable("message.flyable.flight.disabled"), true);
		}
	}

	private static void tick(MinecraftServer server) {
		tickTemporaryCooldowns();

		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			ActiveFlight activeFlight = ACTIVE_PLAYERS.get(player.getUUID());

			if (activeFlight == null) {
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

			if (activeFlight.mode == FlightMode.TEMPORARY) {
				activeFlight.ticksRemaining--;

				if (activeFlight.ticksRemaining <= 0) {
					disable(player, true, true);
				}
			}
		}
	}

	private static void disableAll(MinecraftServer server) {
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			disable(player, false, false);
		}

		ACTIVE_PLAYERS.clear();
		TEMPORARY_COOLDOWNS.clear();
	}

	private static void disableForDisconnect(ServerPlayer player) {
		ActiveFlight activeFlight = ACTIVE_PLAYERS.get(player.getUUID());

		if (activeFlight == null) {
			return;
		}

		disable(player, false, activeFlight.mode == FlightMode.TEMPORARY);
	}

	private static void startTemporaryCooldown(ServerPlayer player, boolean notify) {
		TEMPORARY_COOLDOWNS.put(player.getUUID(), TEMPORARY_COOLDOWN_TICKS);
		syncTemporaryCooldown(player, TEMPORARY_COOLDOWN_TICKS);

		if (notify) {
			player.displayClientMessage(Component.translatable("message.flyable.temporary_flight.ended"), true);
		}
	}

	private static void syncTemporaryCooldown(ServerPlayer player, int ticks) {
		player.getCooldowns().addCooldown(new ItemStack(ModItems.TEMPORARY_FLIGHT_SLIME_BALL), ticks);
	}

	private static int getTemporaryCooldownTicks(ServerPlayer player) {
		return TEMPORARY_COOLDOWNS.getOrDefault(player.getUUID(), 0);
	}

	private static void tickTemporaryCooldowns() {
		Iterator<Map.Entry<UUID, Integer>> iterator = TEMPORARY_COOLDOWNS.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<UUID, Integer> entry = iterator.next();
			int ticks = entry.getValue() - 1;

			if (ticks <= 0) {
				iterator.remove();
			} else {
				entry.setValue(ticks);
			}
		}
	}

	private static int ticksToSeconds(int ticks) {
		return (int) Math.ceil(ticks / 20.0D);
	}

	private enum FlightMode {
		PERMANENT,
		TEMPORARY
	}

	private static final class ActiveFlight {
		private final FlightMode mode;
		private final AbilitySnapshot previousAbilities;
		private int ticksRemaining;

		private ActiveFlight(FlightMode mode, AbilitySnapshot previousAbilities, int ticksRemaining) {
			this.mode = mode;
			this.previousAbilities = previousAbilities;
			this.ticksRemaining = ticksRemaining;
		}
	}

	private record AbilitySnapshot(GameType gameMode, boolean mayfly, boolean flying, float flyingSpeed) {
		private static AbilitySnapshot from(ServerPlayer player) {
			Abilities abilities = player.getAbilities();
			return new AbilitySnapshot(player.gameMode(), abilities.mayfly, abilities.flying, abilities.getFlyingSpeed());
		}

		private void applyTo(ServerPlayer player) {
			Abilities abilities = player.getAbilities();
			GameType currentGameMode = player.gameMode();

			if (currentGameMode == gameMode) {
				abilities.mayfly = mayfly;
				abilities.flying = flying;
				abilities.setFlyingSpeed(flyingSpeed);

				if (currentGameMode.isCreative() || currentGameMode == GameType.SPECTATOR) {
					currentGameMode.updatePlayerAbilities(abilities);
				}

				return;
			}

			currentGameMode.updatePlayerAbilities(abilities);
			abilities.setFlyingSpeed(flyingSpeed);
		}
	}
}
