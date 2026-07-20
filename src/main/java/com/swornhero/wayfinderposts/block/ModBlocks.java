package com.swornhero.wayfinderposts.block;

import com.swornhero.wayfinderposts.WayfinderPosts;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public final class ModBlocks {
    public static final Block OAK_WAYFINDER_POST = register(
            "oak_wayfinder_post",
            WayfinderPostBlock::new,
            BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD),
            true
    );

    private ModBlocks() {
    }

    private static Block register(
            String name,
            Function<BlockBehaviour.Properties, Block> blockFactory,
            BlockBehaviour.Properties properties,
            boolean registerBlockItem
    ) {
        ResourceKey<Block> blockKey = ResourceKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(WayfinderPosts.MOD_ID, name)
        );

        Block block = blockFactory.apply(properties.setId(blockKey));

        if (registerBlockItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(
                    Registries.ITEM,
                    Identifier.fromNamespaceAndPath(WayfinderPosts.MOD_ID, name)
            );

            BlockItem blockItem = new BlockItem(
                    block,
                    new Item.Properties()
                            .setId(itemKey)
                            .useBlockDescriptionPrefix()
            );

            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .register(entries -> entries.accept(OAK_WAYFINDER_POST.asItem()));

        WayfinderPosts.LOGGER.info("Registered Wayfinder Posts blocks");
    }
}