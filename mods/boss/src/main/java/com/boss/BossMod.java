package com.boss;

import com.boss.block.ModBlocks;
import com.boss.event.BossTickHandler;
import com.boss.item.ModItems;
import com.boss.power.BossModeManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BossMod implements ModInitializer {
	public static final String MOD_ID = "boss";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModItems.initialize();
		BossTickHandler.initialize();
		BossModeManager.initialize();
	}
}
