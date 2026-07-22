package com.swornhero.wayfinderposts.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;

public class WayfinderPostBlockEntity extends BlockEntity {

    private static final String LINE_ONE_KEY = "line_one";
    private static final String LINE_TWO_KEY = "line_two";
    private static final String ARROW_KEY = "arrow";

    private static final int MAX_LINE_LENGTH = 64;

    private String lineOne = "";
    private String lineTwo = "";
    private WayfinderArrow arrow = WayfinderArrow.NONE;

    public WayfinderPostBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WAYFINDER_POST, pos, state);
    }

    public String getLineOne() {
        return lineOne;
    }

    public void setLineOne(String lineOne) {
        this.lineOne = sanitizeLine(lineOne);
        markUpdated();
    }

    public String getLineTwo() {
        return lineTwo;
    }

    public void setLineTwo(String lineTwo) {
        this.lineTwo = sanitizeLine(lineTwo);
        markUpdated();
    }

    public WayfinderArrow getArrow() {
        return arrow;
    }

    public void setArrow(WayfinderArrow arrow) {
        this.arrow = arrow;
        markUpdated();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        output.putString(LINE_ONE_KEY, lineOne);
        output.putString(LINE_TWO_KEY, lineTwo);
        output.putString(ARROW_KEY, arrow.getSerializedName());
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        lineOne = sanitizeLine(
                input.getStringOr(LINE_ONE_KEY, "")
        );

        lineTwo = sanitizeLine(
                input.getStringOr(LINE_TWO_KEY, "")
        );

        arrow = WayfinderArrow.fromSerializedName(
                input.getStringOr(ARROW_KEY, "none")
        );
    }

    @Override
    public CompoundTag getUpdateTag(
            HolderLookup.Provider registryLookup
    ) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void markUpdated() {
        setChanged();

        if (level != null && !level.isClientSide()) {
            BlockState state = getBlockState();

            level.sendBlockUpdated(
                    worldPosition,
                    state,
                    state,
                    Block.UPDATE_CLIENTS
            );
        }
    }

    private static String sanitizeLine(String value) {
        String sanitized = value.strip();

        if (sanitized.length() > MAX_LINE_LENGTH) {
            return sanitized.substring(0, MAX_LINE_LENGTH);
        }

        return sanitized;
    }
}