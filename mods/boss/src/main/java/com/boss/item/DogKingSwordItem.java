package com.boss.item;

import com.boss.BossMod;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class DogKingSwordItem extends Item {
	private static final String READY_TIME_KEY = BossMod.MOD_ID + "_dog_king_laser_ready_time_ms";
	private static final long LASER_COOLDOWN_MILLIS = 30_000L;
	private static final double LASER_RANGE = 40.0D;
	private static final float LASER_DAMAGE = 20.0F;
	private static final double LASER_ENTITY_PADDING = 0.35D;
	private static final double PARTICLE_SPACING = 0.45D;

	public DogKingSwordItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);

		if (!(level instanceof ServerLevel serverLevel) || !(user instanceof ServerPlayer serverPlayer)) {
			return InteractionResult.SUCCESS;
		}

		long nowMillis = System.currentTimeMillis();
		long readyTime = getReadyTimeMillis(stack);

		if (readyTime > nowMillis) {
			serverPlayer.displayClientMessage(cooldownMessage(stack, nowMillis), true);
			return InteractionResult.FAIL;
		}

		fireLaser(serverLevel, serverPlayer);
		setReadyTimeMillis(stack, nowMillis + LASER_COOLDOWN_MILLIS);
		user.awardStat(Stats.ITEM_USED.get(this));
		serverPlayer.displayClientMessage(cooldownMessage(stack, nowMillis), true);

		return InteractionResult.SUCCESS;
	}

	public static boolean isDogKingSword(ItemStack stack) {
		return stack.getItem() instanceof DogKingSwordItem;
	}

	public static Component cooldownMessage(ItemStack stack, long nowMillis) {
		long readyTime = getReadyTimeMillis(stack);

		if (readyTime <= nowMillis) {
			return Component.literal("激光已就绪");
		}

		double seconds = Math.max(0L, readyTime - nowMillis) / 1000.0D;
		return Component.literal(String.format(Locale.ROOT, "激光冷却: %.1fs", seconds));
	}

	private static void fireLaser(ServerLevel level, ServerPlayer user) {
		Vec3 start = user.getEyePosition();
		Vec3 direction = user.getLookAngle().normalize();
		Vec3 maxEnd = start.add(direction.scale(LASER_RANGE));
		BlockHitResult blockHit = level.clip(new ClipContext(start, maxEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, user));
		Vec3 rayEnd = blockHit.getType() == HitResult.Type.MISS ? maxEnd : blockHit.getLocation();
		EntityLaserHit entityHit = findEntityHit(level, user, start, rayEnd);

		if (entityHit != null) {
			rayEnd = entityHit.hitPosition();
		}

		spawnLaserParticles(level, start, rayEnd);
		level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.9F, 1.25F);
		level.playSound(null, rayEnd.x(), rayEnd.y(), rayEnd.z(), SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 0.45F, 1.6F);

		if (entityHit != null) {
			entityHit.target().hurtServer(level, user.damageSources().playerAttack(user), LASER_DAMAGE);
		}
	}

	private static EntityLaserHit findEntityHit(ServerLevel level, ServerPlayer user, Vec3 start, Vec3 end) {
		AABB searchBox = new AABB(start, end).inflate(1.0D);
		LivingEntity nearestTarget = null;
		Vec3 nearestHit = null;
		double nearestDistanceSqr = start.distanceToSqr(end);

		for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, searchBox, target -> canHit(user, target))) {
			Optional<Vec3> hit = target.getBoundingBox().inflate(LASER_ENTITY_PADDING).clip(start, end);

			if (hit.isEmpty()) {
				continue;
			}

			double distanceSqr = start.distanceToSqr(hit.get());

			if (distanceSqr < nearestDistanceSqr) {
				nearestTarget = target;
				nearestHit = hit.get();
				nearestDistanceSqr = distanceSqr;
			}
		}

		return nearestTarget == null ? null : new EntityLaserHit(nearestTarget, nearestHit);
	}

	private static boolean canHit(ServerPlayer user, LivingEntity target) {
		return target != user
				&& target.isAlive()
				&& !target.isSpectator()
				&& target.isPickable()
				&& !target.isPassengerOfSameVehicle(user);
	}

	private static void spawnLaserParticles(ServerLevel level, Vec3 start, Vec3 end) {
		double length = start.distanceTo(end);
		Vec3 direction = end.subtract(start).normalize();
		int steps = Math.max(1, (int) Math.ceil(length / PARTICLE_SPACING));

		for (int i = 0; i <= steps; i++) {
			Vec3 pos = start.add(direction.scale(Math.min(length, i * PARTICLE_SPACING)));
			level.sendParticles(ParticleTypes.END_ROD, pos.x(), pos.y(), pos.z(), 1, 0.015D, 0.015D, 0.015D, 0.0D);
			level.sendParticles(ParticleTypes.CRIT, pos.x(), pos.y(), pos.z(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
		}
	}

	private static long getReadyTimeMillis(ItemStack stack) {
		CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
		CompoundTag tag = data.copyTag();

		return tag.getLongOr(READY_TIME_KEY, 0L);
	}

	private static void setReadyTimeMillis(ItemStack stack, long readyTimeMillis) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putLong(READY_TIME_KEY, readyTimeMillis));
	}

	private record EntityLaserHit(LivingEntity target, Vec3 hitPosition) {
	}
}
