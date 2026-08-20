package com.connection.event;

import com.connection.item.ModItems;
import com.connection.power.PoopDropSettings;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class PoopDropHandler {
	private static final int CHECK_INTERVAL_TICKS = 20;

	private PoopDropHandler() {
	}

	public static void initialize() {
		ServerTickEvents.END_SERVER_TICK.register(PoopDropHandler::onServerTick);
	}

	private static void onServerTick(MinecraftServer server) {
		if (server.getTickCount() % CHECK_INTERVAL_TICKS != 0) {
			return;
		}

		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			if (player.isSpectator() || !player.isAlive() || !player.isShiftKeyDown() || !PoopDropSettings.isEnabled(player)) {
				continue;
			}

			player.drop(new ItemStack(ModItems.POOP), false, false);
		}
	}
}
