package com.swornhero.wayfinderposts.client;

import com.swornhero.wayfinderposts.blockentity.WayfinderArrow;
import com.swornhero.wayfinderposts.client.screen.WayfinderPostScreen;
import com.swornhero.wayfinderposts.networking.OpenWayfinderEditorPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class WayfinderPostsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(
				OpenWayfinderEditorPayload.TYPE,
				(payload, context) -> {
					WayfinderArrow arrow =
							WayfinderArrow.fromSerializedName(
									payload.arrow()
							);

					context.client().setScreenAndShow(
							new WayfinderPostScreen(
									payload.pos(),
									payload.lineOne(),
									payload.lineTwo(),
									arrow
							)
					);
				}
		);
	}
}