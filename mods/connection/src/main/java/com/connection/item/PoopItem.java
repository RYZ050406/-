package com.connection.item;

import com.connection.entity.ThrownPoopEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public class PoopItem extends Item implements ProjectileItem {
	private static final float PROJECTILE_SHOOT_POWER = 1.5F;
	private static final float DEFAULT_HIT_DAMAGE = 3.0F;
	private static final int DEFAULT_FOOD_RESTORE = 6;
	private static final float DEFAULT_FOOD_SATURATION = 0.6F;
	private static final int EFFECT_DURATION_TICKS = 20 * 60;
	private final float hitDamage;
	private final int foodRestore;
	private final float foodSaturation;
	private final boolean appliesStatusEffects;

	public PoopItem(Properties properties) {
		this(properties, DEFAULT_HIT_DAMAGE, DEFAULT_FOOD_RESTORE, DEFAULT_FOOD_SATURATION, true);
	}

	protected PoopItem(Properties properties, float hitDamage, int foodRestore, float foodSaturation, boolean appliesStatusEffects) {
		super(properties);
		this.hitDamage = hitDamage;
		this.foodRestore = foodRestore;
		this.foodSaturation = foodSaturation;
		this.appliesStatusEffects = appliesStatusEffects;
	}

	public float getHitDamage() {
		return hitDamage;
	}

	@Override
	public InteractionResult use(Level level, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);

		if (user.isShiftKeyDown()) {
			return eatPoop(level, user, stack);
		}

		level.playSound(
				null,
				user.getX(),
				user.getY(),
				user.getZ(),
				SoundEvents.EGG_THROW,
				SoundSource.PLAYERS,
				0.5F,
				0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
		);

		if (level instanceof ServerLevel serverLevel) {
			Projectile.spawnProjectileFromRotation(
					ThrownPoopEntity::new,
					serverLevel,
					stack,
					user,
					0.0F,
					PROJECTILE_SHOOT_POWER,
					1.0F
			);
		}

		user.awardStat(Stats.ITEM_USED.get(this));
		stack.consume(1, user);

		return InteractionResult.SUCCESS;
	}

	private InteractionResult eatPoop(Level level, Player user, ItemStack stack) {
		level.playSound(
				null,
				user.getX(),
				user.getY(),
				user.getZ(),
				SoundEvents.GENERIC_EAT,
				SoundSource.PLAYERS,
				1.0F,
				1.0F
		);

		if (!level.isClientSide()) {
			user.getFoodData().eat(foodRestore, foodSaturation);
			if (appliesStatusEffects) {
				user.addEffect(new MobEffectInstance(MobEffects.NAUSEA, EFFECT_DURATION_TICKS, 0));
				user.addEffect(new MobEffectInstance(MobEffects.POISON, EFFECT_DURATION_TICKS, 0));
				user.addEffect(new MobEffectInstance(MobEffects.SATURATION, EFFECT_DURATION_TICKS, 0));
			}
			stack.consume(1, user);
		}

		user.awardStat(Stats.ITEM_USED.get(this));

		return InteractionResult.SUCCESS;
	}

	@Override
	public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
		return new ThrownPoopEntity(level, position.x(), position.y(), position.z(), stack);
	}
}
