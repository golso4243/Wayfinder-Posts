package com.swornhero.wayfinderposts.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class WayfinderPostBlockEntity extends BlockEntity {

    private static final String LINE_ONE_KEY = "line_one";
    private static final String LINE_TWO_KEY = "line_two";
    private static final int MAX_LINE_LENGTH = 64;

    private String lineOne = "";
    private String lineTwo = "";

    public WayfinderPostBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WAYFINDER_POST, pos, state);
    }

    public String getLineOne() {
        return lineOne;
    }

    public void setLineOne(String lineOne) {
        this.lineOne = sanitizeLine(lineOne);
        setChanged();
    }

    public String getLineTwo() {
        return lineTwo;
    }

    public void setLineTwo(String lineTwo) {
        this.lineTwo = sanitizeLine(lineTwo);
        setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        output.putString(LINE_ONE_KEY, lineOne);
        output.putString(LINE_TWO_KEY, lineTwo);
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
    }

    private static String sanitizeLine(String value) {
        if (value == null) {
            return "";
        }

        String sanitized = value.strip();

        if (sanitized.length() > MAX_LINE_LENGTH) {
            return sanitized.substring(0, MAX_LINE_LENGTH);
        }

        return sanitized;
    }
}