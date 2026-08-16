package com.perseverance.entity;

import com.perseverance.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public final class TakeoffSupermanEntity extends Monster {
	private static final Component DISPLAY_NAME = Component.literal("起飞超人");
	private static final float MAX_HEALTH = 1000.0F;
	private static final float ATTACK_DAMAGE = 5.0F;
	private static final double ATTACK_REACH_SQR = 4.0D * 4.0D;
	private final ServerBossEvent bossEvent = new ServerBossEvent(
			DISPLAY_NAME,
			BossEvent.BossBarColor.RED,
			BossEvent.BossBarOverlay.PROGRESS
	);

	public TakeoffSupermanEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
		setCustomName(DISPLAY_NAME);
		setCustomNameVisible(true);
		setPersistenceRequired();
		equipDisplayItems();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, MAX_HEALTH)
				.add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
				.add(Attributes.ATTACK_SPEED, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(2, new FixedMeleeAttackGoal(this, 1.0D, true));
		goalSelector.addGoal(7, new RandomStrollGoal(this, 0.8D));
		goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	protected void customServerAiStep(ServerLevel level) {
		super.customServerAiStep(level);
		bossEvent.setProgress(Math.max(0.0F, getHealth() / getMaxHealth()));
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		bossEvent.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		bossEvent.removePlayer(player);
	}

	@Override
	protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean causedByPlayer) {
		super.dropCustomDeathLoot(level, damageSource, causedByPlayer);
		spawnAtLocation(level, new ItemStack(ModItems.PERSEVERANCE_PROOF));
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity target) {
		if (!(target instanceof LivingEntity livingTarget)) {
			return false;
		}

		boolean damaged = livingTarget.hurtServer(level, damageSources().mobAttack(this), ATTACK_DAMAGE);

		if (damaged) {
			onAttack();
		}

		return damaged;
	}

	@Override
	public int getArmorValue() {
		return 0;
	}

	private void equipDisplayItems() {
		setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
		setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
		setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
		setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			setDropChance(slot, 0.0F);
		}
	}

	private static final class FixedMeleeAttackGoal extends MeleeAttackGoal {
		private FixedMeleeAttackGoal(TakeoffSupermanEntity mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
			super(mob, speedModifier, followingTargetEvenIfNotSeen);
		}

		@Override
		protected int getAttackInterval() {
			return 20;
		}

		@Override
		protected boolean canPerformAttack(LivingEntity target) {
			return isTimeToAttack() && mob.distanceToSqr(target) <= ATTACK_REACH_SQR && mob.getSensing().hasLineOfSight(target);
		}
	}
}
