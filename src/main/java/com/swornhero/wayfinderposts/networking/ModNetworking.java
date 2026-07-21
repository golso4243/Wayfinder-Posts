package com.swornhero.wayfinderposts.networking;

import com.swornhero.wayfinderposts.WayfinderPosts;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class ModNetworking {

    private ModNetworking() {
    }

    public static void initialize() {
        PayloadTypeRegistry.clientboundPlay().register(
                OpenWayfinderEditorPayload.TYPE,
                OpenWayfinderEditorPayload.CODEC
        );

        WayfinderPosts.LOGGER.info(
                "Registered Wayfinder Posts networking"
        );
    }
}