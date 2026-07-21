package com.swornhero.wayfinderposts.networking;

import com.swornhero.wayfinderposts.WayfinderPosts;
import com.swornhero.wayfinderposts.blockentity.WayfinderArrow;
import com.swornhero.wayfinderposts.blockentity.WayfinderPostBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class ModNetworking {

    private static final double MAX_EDIT_DISTANCE_SQUARED = 64.0;

    private ModNetworking() {
    }

    public static void initialize() {
        PayloadTypeRegistry.clientboundPlay().register(
                OpenWayfinderEditorPayload.TYPE,
                OpenWayfinderEditorPayload.CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(
                SaveWayfinderPostPayload.TYPE,
                SaveWayfinderPostPayload.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(
                SaveWayfinderPostPayload.TYPE,
                (payload, context) -> handleSave(
                        payload,
                        context.player()
                )
        );

        WayfinderPosts.LOGGER.info(
                "Registered Wayfinder Posts networking"
        );
    }

    private static void handleSave(
            SaveWayfinderPostPayload payload,
            ServerPlayer player
    ) {
        BlockPos pos = payload.pos();

        if (!player.level().hasChunkAt(pos)) {
            return;
        }

        if (player.distanceToSqr(
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5
        ) > MAX_EDIT_DISTANCE_SQUARED) {
            return;
        }

        BlockEntity blockEntity = player.level().getBlockEntity(pos);

        if (!(blockEntity instanceof WayfinderPostBlockEntity wayfinderPost)) {
            return;
        }

        WayfinderArrow arrow =
                WayfinderArrow.fromSerializedName(payload.arrow());

        wayfinderPost.setLineOne(payload.lineOne());
        wayfinderPost.setLineTwo(payload.lineTwo());
        wayfinderPost.setArrow(arrow);
    }
}