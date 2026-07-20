package com.swornhero.wayfinderposts.blockentity;

import java.util.Locale;

public enum WayfinderArrow {
    NONE,
    LEFT,
    RIGHT,
    FORWARD;

    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static WayfinderArrow fromSerializedName(String value) {
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