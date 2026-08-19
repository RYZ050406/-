package com.boss.power;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;

public final class BossModeManager {
	private static final Component BOSS_BAR_NAME = Component.literal("古希腊掌管起飞的神")
			.withStyle(style -> style.withColor(0x8B5A2B).withBold(true).withItalic(true));
	private static final Map<UUID, ServerBossEvent> ACTIVE_BOSS_BARS = new ConcurrentHashMap<>();

	private BossModeManager() {
	}

	public static void initialize() {
		ServerPlayerEvents.JOIN.register(BossModeManager::onJoin);
		ServerPlayerEvents.LEAVE.register(BossModeManager::onLeave);
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> exit(newPlayer));
	}

	public static void enter(ServerPlayer player) {
		ServerBossEvent bossEvent = ACTIVE_BOSS_BARS.computeIfAbsent(player.getUUID(), uuid -> new ServerBossEvent(
				BOSS_BAR_NAME,
				BossEvent.BossBarColor.YELLOW,
				BossEvent.BossBarOverlay.PROGRESS
		));
		MinecraftServer server = player.level().getServer();

		if (server != null) {
			syncViewers(server, bossEvent);
		}

		updateProgress(player, bossEvent);
		player.displayClientMessage(Component.literal("已进入BOSS模式"), true);
	}

	public static void tick(MinecraftServer server) {
		Iterator<Map.Entry<UUID, ServerBossEvent>> iterator = ACTIVE_BOSS_BARS.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<UUID, ServerBossEvent> entry = iterator.next();
			ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
			ServerBossEvent bossEvent = entry.getValue();

			if (player == null || !player.isAlive() || player.isRemoved()) {
				bossEvent.removeAllPlayers();
				iterator.remove();
				continue;
			}

			syncViewers(server, bossEvent);
			updateProgress(player, bossEvent);
		}
	}

	private static void exit(ServerPlayer player) {
		ServerBossEvent bossEvent = ACTIVE_BOSS_BARS.remove(player.getUUID());

		if (bossEvent != null) {
			bossEvent.removeAllPlayers();
		}
	}

	private static void onJoin(ServerPlayer player) {
		for (ServerBossEvent bossEvent : ACTIVE_BOSS_BARS.values()) {
			bossEvent.addPlayer(player);
		}
	}

	private static void onLeave(ServerPlayer player) {
		for (ServerBossEvent bossEvent : ACTIVE_BOSS_BARS.values()) {
			bossEvent.removePlayer(player);
		}

		exit(player);
	}

	private static void syncViewers(MinecraftServer server, ServerBossEvent bossEvent) {
		for (ServerPlayer onlinePlayer : server.getPlayerList().getPlayers()) {
			bossEvent.addPlayer(onlinePlayer);
		}
	}

	private static void updateProgress(ServerPlayer player, ServerBossEvent bossEvent) {
		float progress = player.getMaxHealth() <= 0.0F ? 0.0F : player.getHealth() / player.getMaxHealth();
		bossEvent.setProgress(Mth.clamp(progress, 0.0F, 1.0F));
	}
}