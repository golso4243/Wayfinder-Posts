package com.swornhero.wayfinderposts.blockentity;

import java.util.Locale;
import org.jspecify.annotations.Nullable;

public enum WayfinderArrow {
    NONE,
    LEFT,
    RIGHT,
    FORWARD;

    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public WayfinderArrow next() {
        WayfinderArrow[] arrows = values();
        int nextIndex = (ordinal() + 1) % arrows.length;

        return arrows[nextIndex];
    }

    public static WayfinderArrow fromSerializedName(@Nullable String value) {
        if (value == null) {
            return NONE;
        }

        for (WayfinderArrow arrow : values()) {
            if (arrow.getSerializedName().equalsIgnoreCase(value)) {
                return arrow;
            }
        }

        return NONE;
    }
}