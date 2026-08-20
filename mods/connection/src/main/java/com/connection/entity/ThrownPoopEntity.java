package com.connection.entity;

import com.connection.item.ModItems;
import com.connection.item.PoopItem;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public final class ThrownPoopEntity extends ThrowableItemProjectile {
	private static final float DEFAULT_HIT_DAMAGE = 3.0F;

	public ThrownPoopEntity(EntityType<? extends ThrownPoopEntity> entityType, Level level) {
		super(entityType, level);
	}

	public ThrownPoopEntity(Level level, LivingEntity owner, ItemStack stack) {
		super(ModEntities.THROWN_POOP, owner, level, stack);
	}

	public ThrownPoopEntity(Level level, double x, double y, double z, ItemStack stack) {
		super(ModEntities.THROWN_POOP, x, y, z, level, stack);
	}

	@Override
	public void handleEntityEvent(byte status) {
		if (status == 3) {
			for (int i = 0; i < 8; i++) {
				level().addParticle(
						new ItemParticleOption(ParticleTypes.ITEM, getItem()),
						getX(),
						getY(),
						getZ(),
						(random.nextFloat() - 0.5D) * 0.08D,
						(random.nextFloat() - 0.5D) * 0.08D,
						(random.nextFloat() - 0.5D) * 0.08D
				);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
		super.onHitEntity(hitResult);
		Entity owner = getOwner();
		ItemStack stack = getItem();
		float damage = DEFAULT_HIT_DAMAGE;
		if (stack.getItem() instanceof PoopItem poopItem) {
			damage = poopItem.getHitDamage();
		}
		hitResult.getEntity().hurt(damageSources().thrown(this, owner), damage);
	}

	@Override
	protected void onHit(HitResult hitResult) {
		super.onHit(hitResult);

		if (!level().isClientSide()) {
			level().broadcastEntityEvent(this, (byte) 3);
			discard();
		}
	}

	@Override
	protected Item getDefaultItem() {
		return ModItems.POOP;
	}
}
