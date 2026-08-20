package com.connection.item;

public final class SuperPoopItem extends PoopItem {
	private static final float HIT_DAMAGE = 7.0F;
	private static final int FOOD_RESTORE = 20;
	private static final float FOOD_SATURATION = 1.0F;

	public SuperPoopItem(Properties properties) {
		super(properties, HIT_DAMAGE, FOOD_RESTORE, FOOD_SATURATION, false);
	}
}
