package com.boss.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public final class BossClient implements ClientModInitializer {
	private static float lastYawOffset;
	private static float lastPitchOffset;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(BossClient::onClientTick);
	}

	private static void onClientTick(Minecraft client) {
		LocalPlayer player = client.player;

		if (player == null) {
			lastYawOffset = 0.0F;
			lastPitchOffset = 0.0F;
			return;
		}

		MobEffectInstance nausea = player.getEffect(MobEffects.NAUSEA);

		if (client.isPaused() || nausea == null) {
			clearOffset(player);
			return;
		}

		float strength = 1.8F + Math.min(1.2F, nausea.getDuration() / 20.0F * 0.2F) + nausea.getAmplifier() * 0.6F;
		float time = player.tickCount + nausea.getDuration() * 0.37F;
		float yawOffset = Mth.sin(time * 0.72F) * strength + Mth.sin(time * 0.19F) * strength * 0.55F;
		float pitchOffset = Mth.cos(time * 0.58F) * strength * 0.45F;

		applyOffset(player, yawOffset, pitchOffset);
	}

	private static void applyOffset(LocalPlayer player, float yawOffset, float pitchOffset) {
		float baseYaw = player.getYRot() - lastYawOffset;
		float basePitch = player.getXRot() - lastPitchOffset;
		float baseYawO = player.yRotO - lastYawOffset;
		float basePitchO = player.xRotO - lastPitchOffset;
		float nextPitch = Mth.clamp(basePitch + pitchOffset, -90.0F, 90.0F);
		float appliedPitchOffset = nextPitch - basePitch;

		player.setYRot(baseYaw + yawOffset);
		player.setXRot(nextPitch);
		player.yRotO = baseYawO + yawOffset;
		player.xRotO = Mth.clamp(basePitchO + appliedPitchOffset, -90.0F, 90.0F);
		lastYawOffset = yawOffset;
		lastPitchOffset = appliedPitchOffset;
	}

	private static void clearOffset(LocalPlayer player) {
		if (lastYawOffset == 0.0F && lastPitchOffset == 0.0F) {
			return;
		}

		player.setYRot(player.getYRot() - lastYawOffset);
		player.setXRot(Mth.clamp(player.getXRot() - lastPitchOffset, -90.0F, 90.0F));
		player.yRotO -= lastYawOffset;
		player.xRotO = Mth.clamp(player.xRotO - lastPitchOffset, -90.0F, 90.0F);
		lastYawOffset = 0.0F;
		lastPitchOffset = 0.0F;
	}
}
