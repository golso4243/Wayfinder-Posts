package com.swornhero.wayfinderposts.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WayfinderPostBlockEntity extends BlockEntity {

    public WayfinderPostBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WAYFINDER_POST, pos, state);
    }
}