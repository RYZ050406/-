package com.flyable;

import com.flyable.item.ModItems;
import com.flyable.power.FlightPower;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FlyableMod implements ModInitializer {
	public static final String MOD_ID = "flyable";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
		FlightPower.initialize();
	}
}
