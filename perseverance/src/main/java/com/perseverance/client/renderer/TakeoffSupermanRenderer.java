package com.perseverance.client.renderer;

import com.perseverance.entity.TakeoffSupermanEntity;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;

public final class TakeoffSupermanRenderer extends HumanoidMobRenderer<TakeoffSupermanEntity, AvatarRenderState, PlayerModel> {
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
			"perseverance",
			"textures/entity/thegou.png"
	);

	public TakeoffSupermanRenderer(EntityRendererProvider.Context context) {
		super(
				context,
				new PlayerModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true),
				new PlayerModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true),
				0.5F
		);
		addLayer(new HumanoidArmorLayer<>(
				this,
				ArmorModelSet.bake(ModelLayers.PLAYER_SLIM_ARMOR, context.getModelSet(), layer -> new PlayerModel(layer, true)),
				ArmorModelSet.bake(ModelLayers.PLAYER_SLIM_ARMOR, context.getModelSet(), layer -> new PlayerModel(layer, true)),
				context.getEquipmentRenderer()
		));
	}

	@Override
	public Identifier getTextureLocation(AvatarRenderState renderState) {
		return TEXTURE;
	}

	@Override
	public AvatarRenderState createRenderState() {
		return new AvatarRenderState();
	}
}
