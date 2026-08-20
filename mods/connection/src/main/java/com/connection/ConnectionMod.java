package com.connection;

import com.connection.entity.ModEntities;
import com.connection.event.PoopDropHandler;
import com.connection.item.ModItems;
import com.connection.network.ModNetworking;
import com.connection.recipe.ModRecipeSerializers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConnectionMod implements ModInitializer {
	public static final String MOD_ID = "connection";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
		ModEntities.initialize();
		ModNetworking.initialize();
		ModRecipeSerializers.initialize();
		PoopDropHandler.initialize();
	}
}
