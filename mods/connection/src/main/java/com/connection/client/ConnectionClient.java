package com.connection.client;

import com.connection.ConnectionMod;
import com.connection.entity.ModEntities;
import com.connection.network.TogglePoopDropPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class ConnectionClient implements ClientModInitializer {
	private static final KeyMapping.Category CONNECTION_CATEGORY = KeyMapping.Category.register(
			Identifier.fromNamespaceAndPath(ConnectionMod.MOD_ID, "connection")
	);

	private static KeyMapping togglePoopDropKey;

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.THROWN_POOP, ThrownItemRenderer::new);
		togglePoopDropKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.connection.toggle_poop_drop",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_K,
				CONNECTION_CATEGORY
		));
		ClientTickEvents.END_CLIENT_TICK.register(ConnectionClient::onClientTick);
	}

	private static void onClientTick(Minecraft client) {
		while (togglePoopDropKey.consumeClick()) {
			if (client.player != null && ClientPlayNetworking.canSend(TogglePoopDropPayload.TYPE)) {
				ClientPlayNetworking.send(new TogglePoopDropPayload());
			}
		}
	}
}
