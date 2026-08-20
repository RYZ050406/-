package com.connection.network;

import com.connection.power.PoopDropSettings;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class ModNetworking {
	private ModNetworking() {
	}

	public static void initialize() {
		PayloadTypeRegistry.playC2S().register(TogglePoopDropPayload.TYPE, TogglePoopDropPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(TogglePoopDropPayload.TYPE, (payload, context) -> {
			ServerPlayer player = context.player();

			context.server().execute(() -> PoopDropSettings.toggle(player));
		});
	}
}
