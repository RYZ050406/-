package com.perseverance.client;

import com.perseverance.client.renderer.TakeoffSupermanRenderer;
import com.perseverance.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public final class PerseveranceClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntities.TAKEOFF_SUPERMAN, TakeoffSupermanRenderer::new);
	}
}
