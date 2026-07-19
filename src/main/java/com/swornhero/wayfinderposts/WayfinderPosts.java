package com.swornhero.wayfinderposts;

import com.swornhero.wayfinderposts.block.ModBlocks;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WayfinderPosts implements ModInitializer {
	public static final String MOD_ID = "wayfinder-posts";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.initialize();

		LOGGER.info("Initializing Wayfinder Posts");
	}
}
