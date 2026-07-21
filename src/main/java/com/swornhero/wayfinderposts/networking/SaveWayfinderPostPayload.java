package com.swornhero.wayfinderposts.networking;

import com.swornhero.wayfinderposts.WayfinderPosts;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SaveWayfinderPostPayload(
        BlockPos pos,
        String lineOne,
        String lineTwo,
        String arrow
) implements CustomPacketPayload {

    public static final Identifier ID =
            Identifier.fromNamespaceAndPath(
                    WayfinderPosts.MOD_ID,
                    "save_wayfinder_post"
            );

    public static final Type<SaveWayfinderPostPayload> TYPE =
            new Type<>(ID);

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            SaveWayfinderPostPayload
            > CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SaveWayfinderPostPayload::pos,
            ByteBufCodecs.STRING_UTF8,
            SaveWayfinderPostPayload::lineOne,
            ByteBufCodecs.STRING_UTF8,
            SaveWayfinderPostPayload::lineTwo,
            ByteBufCodecs.STRING_UTF8,
            SaveWayfinderPostPayload::arrow,
            SaveWayfinderPostPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}