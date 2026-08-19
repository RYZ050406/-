package com.boss.item;

import com.boss.BossMod;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class BossSwordItem extends Item {
	private static final String READY_TIME_KEY = BossMod.MOD_ID + "_skill_ready_time_ms";
	private static final String LEGACY_READY_TICK_KEY = BossMod.MOD_ID + "_skill_ready_tick";
	private static final long SKILL_COOLDOWN_MILLIS = 10_000L;
	private static final int SPHERE_RADIUS = 3;
	private static final double TARGET_DISTANCE = 10.0D;
	private static final double KNOCKBACK_RADIUS = 6.0D;
	private static final double MAX_KNOCKBACK_STRENGTH = 2.2D;

	private final Block sphereBlock;

	public BossSwordItem(Block sphereBlock, Properties properties) {
		super(properties);
		this.sphereBlock = sphereBlock;
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

		Vec3 look = user.getLookAngle().normalize();
		Vec3 center = user.getEyePosition().add(look.scale(TARGET_DISTANCE));
		BlockPos centerPos = BlockPos.containing(center);

		spawnSphere(serverLevel, centerPos);
		knockBackEntities(serverLevel, center, look);
		serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, center.x(), center.y(), center.z(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
		serverLevel.playSound(null, center.x(), center.y(), center.z(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.0F);

		setReadyTimeMillis(stack, nowMillis + SKILL_COOLDOWN_MILLIS);
		user.awardStat(Stats.ITEM_USED.get(this));
		serverPlayer.displayClientMessage(cooldownMessage(stack, nowMillis), true);

		return InteractionResult.SUCCESS;
	}

	public static boolean isBossSword(ItemStack stack) {
		return stack.getItem() instanceof BossSwordItem;
	}

	public static Component cooldownMessage(ItemStack stack, long nowMillis) {
		long readyTime = getReadyTimeMillis(stack);

		if (readyTime <= nowMillis) {
			return Component.literal("已就绪");
		}

		double seconds = Math.max(0L, readyTime - nowMillis) / 1000.0D;
		return Component.literal(String.format(Locale.ROOT, "技能冷却: %.1fs", seconds));
	}

	private void spawnSphere(ServerLevel level, BlockPos centerPos) {
		BlockState sphereState = sphereBlock.defaultBlockState();
		int radiusSqr = SPHERE_RADIUS * SPHERE_RADIUS;

		for (int x = -SPHERE_RADIUS; x <= SPHERE_RADIUS; x++) {
			for (int y = -SPHERE_RADIUS; y <= SPHERE_RADIUS; y++) {
				for (int z = -SPHERE_RADIUS; z <= SPHERE_RADIUS; z++) {
					if (x * x + y * y + z * z > radiusSqr) {
						continue;
					}

					BlockPos pos = centerPos.offset(x, y, z);
					BlockState currentState = level.getBlockState(pos);

					if (currentState.isAir() || currentState.canBeReplaced()) {
						level.setBlock(pos, sphereState, Block.UPDATE_ALL);
					}
				}
			}
		}
	}

	private void knockBackEntities(ServerLevel level, Vec3 center, Vec3 fallbackDirection) {
		AABB area = new AABB(center, center).inflate(KNOCKBACK_RADIUS);

		for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAlive)) {
			Vec3 offset = entity.position().subtract(center);
			double distance = offset.length();

			if (distance > KNOCKBACK_RADIUS) {
				continue;
			}

			Vec3 direction = distance < 0.1D ? fallbackDirection : offset.normalize();
			double strength = MAX_KNOCKBACK_STRENGTH * (1.0D - Mth.clamp(distance / KNOCKBACK_RADIUS, 0.0D, 0.85D));
			entity.push(direction.x() * strength, 0.35D + strength * 0.25D, direction.z() * strength);
			entity.hurtMarked = true;
		}
	}

	private static long getReadyTimeMillis(ItemStack stack) {
		clearLegacyReadyTick(stack);
		CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
		CompoundTag tag = data.copyTag();

		return tag.getLongOr(READY_TIME_KEY, 0L);
	}

	private static void setReadyTimeMillis(ItemStack stack, long readyTimeMillis) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
			tag.remove(LEGACY_READY_TICK_KEY);
			tag.putLong(READY_TIME_KEY, readyTimeMillis);
		});
	}

	private static void clearLegacyReadyTick(ItemStack stack) {
		CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
		CompoundTag tag = data.copyTag();

		if (tag.contains(LEGACY_READY_TICK_KEY)) {
			CustomData.update(DataComponents.CUSTOM_DATA, stack, updatedTag -> updatedTag.remove(LEGACY_READY_TICK_KEY));
		}
	}
}