package com.perseverance;

import com.perseverance.entity.ModEntities;
import com.perseverance.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PerseveranceMod implements ModInitializer {
	public static final String MOD_ID = "perseverance";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
		ModEntities.initialize();
	}
}
