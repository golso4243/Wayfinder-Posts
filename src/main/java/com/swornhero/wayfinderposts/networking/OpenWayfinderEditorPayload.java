package com.swornhero.wayfinderposts.networking;

import com.swornhero.wayfinderposts.WayfinderPosts;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record OpenWayfinderEditorPayload(
        BlockPos pos,
        String lineOne,
        String lineTwo,
        String arrow
) implements CustomPacketPayload {

    public static final Identifier ID =
            Identifier.fromNamespaceAndPath(
                    WayfinderPosts.MOD_ID,
                    "open_wayfinder_editor"
            );

    public static final Type<OpenWayfinderEditorPayload> TYPE =
            new Type<>(ID);

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            OpenWayfinderEditorPayload
            > CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenWayfinderEditorPayload::pos,
            ByteBufCodecs.STRING_UTF8,
            OpenWayfinderEditorPayload::lineOne,
            ByteBufCodecs.STRING_UTF8,
            OpenWayfinderEditorPayload::lineTwo,
            ByteBufCodecs.STRING_UTF8,
            OpenWayfinderEditorPayload::arrow,
            OpenWayfinderEditorPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}