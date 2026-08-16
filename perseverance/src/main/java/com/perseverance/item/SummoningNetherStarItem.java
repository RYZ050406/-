package com.perseverance.item;

import com.perseverance.entity.ModEntities;
import com.perseverance.entity.TakeoffSupermanEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public final class SummoningNetherStarItem extends Item {
	public SummoningNetherStarItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();

		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.PASS;
		}

		BlockPos spawnPos = context.getClickedPos().relative(context.getClickedFace());
		TakeoffSupermanEntity boss = ModEntities.TAKEOFF_SUPERMAN.create(serverLevel, EntitySpawnReason.SPAWN_ITEM_USE);

		if (boss == null) {
			return InteractionResult.FAIL;
		}

		Player player = context.getPlayer();
		float yRot = player == null ? 0.0F : player.getYRot();
		boss.snapTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, yRot, 0.0F);
		boss.setYHeadRot(yRot);
		boss.setYBodyRot(yRot);
		boss.setHealth(boss.getMaxHealth());
		serverLevel.addFreshEntity(boss);

		ItemStack stack = context.getItemInHand();

		if (player == null || !player.getAbilities().instabuild) {
			stack.shrink(1);
		}

		return InteractionResult.SUCCESS_SERVER;
	}
}
