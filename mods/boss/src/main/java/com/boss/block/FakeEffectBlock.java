package com.boss.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class FakeEffectBlock extends Block {
	private static final int EFFECT_TICKS = 20;
	private final EffectKind effectKind;

	public FakeEffectBlock(EffectKind effectKind, Properties properties) {
		super(properties);
		this.effectKind = effectKind;
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		super.stepOn(level, pos, state, entity);
		applyEffect(level, entity);
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean appliesEffects) {
		super.entityInside(state, level, pos, entity, effectApplier, appliesEffects);
		applyEffect(level, entity);
	}

	private void applyEffect(Level level, Entity entity) {
		if (level.isClientSide() || !(entity instanceof Player player)) {
			return;
		}

		switch (effectKind) {
			case SLOWNESS -> player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, EFFECT_TICKS, 0, false, false, true));
			case FIRE -> player.igniteForSeconds(1.0F);
			case MINING_FATIGUE -> player.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, EFFECT_TICKS, 0, false, false, true));
			case NAUSEA -> player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, EFFECT_TICKS, 0, false, false, true));
		}
	}

	public enum EffectKind {
		SLOWNESS,
		FIRE,
		MINING_FATIGUE,
		NAUSEA
	}
}
