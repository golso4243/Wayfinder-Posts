package com.swornhero.wayfinderposts.block;

import com.swornhero.wayfinderposts.blockentity.WayfinderPostBlockEntity;
import com.swornhero.wayfinderposts.blockentity.WayfinderArrow;
import com.swornhero.wayfinderposts.blockentity.WayfinderArrow;
import com.swornhero.wayfinderposts.networking.OpenWayfinderEditorPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class WayfinderPostBlock extends Block
        implements EntityBlock, SimpleWaterloggedBlock {

    public static final EnumProperty<Direction> FACING =
            HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty WATERLOGGED =
            BlockStateProperties.WATERLOGGED;

    private static final VoxelShape POST_SHAPE =
            Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    private static final VoxelShape NORTH_ARM_SHAPE = Shapes.or(
            POST_SHAPE,
            Block.box(6.0, 10.0, 0.0, 10.0, 14.0, 6.0)
    );

    private static final VoxelShape EAST_ARM_SHAPE = Shapes.or(
            POST_SHAPE,
            Block.box(10.0, 10.0, 6.0, 16.0, 14.0, 10.0)
    );

    private static final VoxelShape SOUTH_ARM_SHAPE = Shapes.or(
            POST_SHAPE,
            Block.box(6.0, 10.0, 10.0, 10.0, 14.0, 16.0)
    );

    private static final VoxelShape WEST_ARM_SHAPE = Shapes.or(
            POST_SHAPE,
            Block.box(0.0, 10.0, 6.0, 6.0, 14.0, 10.0)
    );

    public WayfinderPostBlock(Properties properties) {
        super(properties);

        registerDefaultState(
                stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(WATERLOGGED, false)
        );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WayfinderPostBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        if (level.isClientSide() || !(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof WayfinderPostBlockEntity wayfinderPost)) {
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown()) {
            WayfinderArrow nextArrow = wayfinderPost.getArrow().next();

            wayfinderPost.setArrow(nextArrow);

            serverPlayer.sendSystemMessage(
                    Component.literal(
                            "Wayfinder arrow changed to: "
                                    + nextArrow.getSerializedName()
                    )
            );

            return InteractionResult.SUCCESS;
        }

        ServerPlayNetworking.send(
                serverPlayer,
                new OpenWayfinderEditorPayload(
                        pos,
                        wayfinderPost.getLineOne(),
                        wayfinderPost.getLineTwo(),
                        wayfinderPost.getArrow().getSerializedName()
                )
        );

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel()
                .getFluidState(context.getClickedPos());

        return defaultBlockState()
                .setValue(
                        FACING,
                        context.getHorizontalDirection().getOpposite()
                )
                .setValue(
                        WATERLOGGED,
                        fluidState.is(Fluids.WATER)
                );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(
                FACING,
                rotation.rotate(state.getValue(FACING))
        );
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(
                mirror.getRotation(state.getValue(FACING))
        );
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }

        return super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess scheduledTickAccess,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource random
    ) {
        if (state.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(
                    pos,
                    Fluids.WATER,
                    Fluids.WATER.getTickDelay(level)
            );
        }

        return super.updateShape(
                state,
                level,
                scheduledTickAccess,
                pos,
                direction,
                neighborPos,
                neighborState,
                random
        );
    }

    @Override
    protected VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return getShapeForDirection(state.getValue(FACING));
    }

    @Override
    protected VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return getShapeForDirection(state.getValue(FACING));
    }

    private static VoxelShape getShapeForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH_ARM_SHAPE;
            case EAST -> EAST_ARM_SHAPE;
            case SOUTH -> SOUTH_ARM_SHAPE;
            case WEST -> WEST_ARM_SHAPE;
            default -> POST_SHAPE;
        };
    }
}