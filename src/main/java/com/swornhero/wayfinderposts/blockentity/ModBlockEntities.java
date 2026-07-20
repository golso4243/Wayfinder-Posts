package com.swornhero.wayfinderposts.blockentity;

import com.swornhero.wayfinderposts.WayfinderPosts;
import com.swornhero.wayfinderposts.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {

    public static final BlockEntityType<WayfinderPostBlockEntity>
            WAYFINDER_POST = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(
                    WayfinderPosts.MOD_ID,
                    "wayfinder_post"
            ),
            FabricBlockEntityTypeBuilder.create(
                    WayfinderPostBlockEntity::new,
                    ModBlocks.OAK_WAYFINDER_POST
            ).build()
    );

    private ModBlockEntities() {
    }

    public static void initialize() {
        WayfinderPosts.LOGGER.info(
                "Registered Wayfinder Posts block entities"
        );
    }
}