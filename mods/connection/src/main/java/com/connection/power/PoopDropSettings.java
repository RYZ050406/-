package com.connection.power;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class PoopDropSettings {
	private static final Set<UUID> ENABLED_PLAYERS = ConcurrentHashMap.newKeySet();

	private PoopDropSettings() {
	}

	public static boolean isEnabled(ServerPlayer player) {
		return ENABLED_PLAYERS.contains(player.getUUID());
	}

	public static void toggle(ServerPlayer player) {
		boolean enabled = toggleAndGetEnabled(player.getUUID());
		player.displayClientMessage(
				Component.literal(enabled ? "拉屎：开启" : "拉屎：关闭")
						.withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED),
				true
		);
	}

	private static boolean toggleAndGetEnabled(UUID uuid) {
		if (ENABLED_PLAYERS.remove(uuid)) {
			return false;
		}

		ENABLED_PLAYERS.add(uuid);
		return true;
	}
}
