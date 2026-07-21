package com.swornhero.wayfinderposts.client.screen;

import com.swornhero.wayfinderposts.blockentity.WayfinderArrow;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WayfinderPostScreen extends Screen {

    private static final int PANEL_WIDTH = 240;
    private static final int TEXT_FIELD_WIDTH = 200;
    private static final int WIDGET_HEIGHT = 20;

    private final String initialLineOne;
    private final String initialLineTwo;
    private WayfinderArrow selectedArrow;

    private EditBox lineOneField;
    private EditBox lineTwoField;

    public WayfinderPostScreen(
            String lineOne,
            String lineTwo,
            WayfinderArrow arrow
    ) {
        super(Component.literal("Edit Wayfinder Post"));

        this.initialLineOne = lineOne;
        this.initialLineTwo = lineTwo;
        this.selectedArrow = arrow;
    }

    @Override
    protected void init() {
        int left = (width - PANEL_WIDTH) / 2;
        int fieldLeft = (width - TEXT_FIELD_WIDTH) / 2;
        int top = height / 2 - 90;

        lineOneField = new EditBox(
                font,
                fieldLeft,
                top + 25,
                TEXT_FIELD_WIDTH,
                WIDGET_HEIGHT,
                Component.literal("First text line")
        );

        lineOneField.setMaxLength(64);
        lineOneField.setValue(initialLineOne);

        addRenderableWidget(lineOneField);

        lineTwoField = new EditBox(
                font,
                fieldLeft,
                top + 55,
                TEXT_FIELD_WIDTH,
                WIDGET_HEIGHT,
                Component.literal("Second text line")
        );

        lineTwoField.setMaxLength(64);
        lineTwoField.setValue(initialLineTwo);

        addRenderableWidget(lineTwoField);

        int arrowButtonY = top + 90;
        int arrowButtonWidth = 48;
        int arrowGap = 3;
        int totalArrowWidth =
                (arrowButtonWidth * 4) + (arrowGap * 3);
        int arrowStartX = (width - totalArrowWidth) / 2;

        addArrowButton(
                WayfinderArrow.NONE,
                "No Arrow",
                arrowStartX,
                arrowButtonY,
                arrowButtonWidth
        );

        addArrowButton(
                WayfinderArrow.LEFT,
                "Left",
                arrowStartX + arrowButtonWidth + arrowGap,
                arrowButtonY,
                arrowButtonWidth
        );

        addArrowButton(
                WayfinderArrow.RIGHT,
                "Right",
                arrowStartX + (arrowButtonWidth + arrowGap) * 2,
                arrowButtonY,
                arrowButtonWidth
        );

        addArrowButton(
                WayfinderArrow.FORWARD,
                "Forward",
                arrowStartX + (arrowButtonWidth + arrowGap) * 3,
                arrowButtonY,
                arrowButtonWidth
        );

        int actionButtonY = top + 125;

        addRenderableWidget(
                Button.builder(
                        Component.literal("Save"),
                        button -> handleSave()
                ).bounds(
                        left + 18,
                        actionButtonY,
                        95,
                        WIDGET_HEIGHT
                ).build()
        );

        addRenderableWidget(
                Button.builder(
                        Component.literal("Cancel"),
                        button -> onClose()
                ).bounds(
                        left + 127,
                        actionButtonY,
                        95,
                        WIDGET_HEIGHT
                ).build()
        );

        setInitialFocus(lineOneField);
    }

    private void addArrowButton(
            WayfinderArrow arrow,
            String label,
            int x,
            int y,
            int width
    ) {
        addRenderableWidget(
                Button.builder(
                        Component.literal(label),
                        button -> selectedArrow = arrow
                ).bounds(
                        x,
                        y,
                        width,
                        WIDGET_HEIGHT
                ).build()
        );
    }

    private void handleSave() {
        /*
         * Networking will be added in the next step.
         *
         * For now, Save only closes the placeholder screen.
         */
        onClose();
    }

    @Override
    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float delta
    ) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        int top = height / 2 - 90;

        graphics.text(
                font,
                title,
                (width - font.width(title)) / 2,
                top,
                0xFFFFFFFF,
                true
        );

        graphics.text(
                font,
                "Printed arrow: "
                        + selectedArrow.getSerializedName(),
                (width - 200) / 2,
                top + 78,
                0xFFD7AE73,
                true
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}