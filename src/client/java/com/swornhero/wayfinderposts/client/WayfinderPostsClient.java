package com.swornhero.wayfinderposts.client;

import com.swornhero.wayfinderposts.blockentity.WayfinderArrow;
import com.swornhero.wayfinderposts.client.screen.WayfinderPostScreen;
import com.swornhero.wayfinderposts.networking.OpenWayfinderEditorPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.swornhero.wayfinderposts.blockentity.ModBlockEntities;
import com.swornhero.wayfinderposts.client.render.WayfinderPostBlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class WayfinderPostsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		BlockEntityRenderers.register(
				ModBlockEntities.WAYFINDER_POST,
				WayfinderPostBlockEntityRenderer::new
		);

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