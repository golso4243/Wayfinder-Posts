package com.swornhero.wayfinderposts.client.screen;

import com.swornhero.wayfinderposts.blockentity.WayfinderArrow;
import com.swornhero.wayfinderposts.networking.SaveWayfinderPostPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

public class WayfinderPostScreen extends Screen {

    private static final int PANEL_WIDTH = 240;
    private static final int TEXT_FIELD_WIDTH = 200;
    private static final int WIDGET_HEIGHT = 20;

    private final BlockPos blockPos;
    private final String initialLineOne;
    private final String initialLineTwo;

    private WayfinderArrow selectedArrow;

    private EditBox lineOneField;
    private EditBox lineTwoField;

    public WayfinderPostScreen(
            BlockPos blockPos,
            String lineOne,
            String lineTwo,
            WayfinderArrow arrow
    ) {
        super(Component.literal("Edit Wayfinder Post"));

        this.blockPos = blockPos;
        this.initialLineOne = lineOne;
        this.initialLineTwo = lineTwo;
        this.selectedArrow = arrow;
    }

    @Override
    protected void init() {
        int left = (width - PANEL_WIDTH) / 2;
        int fieldLeft = (width - TEXT_FIELD_WIDTH) / 2;
        int top = height / 2 - 110;

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

        int actionButtonY = top + 180;

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

    private static String getArrowSymbol(WayfinderArrow arrow) {
        return switch (arrow) {
            case NONE -> "";
            case LEFT -> "←";
            case RIGHT -> "→";
            case FORWARD -> "↑";
        };
    }

    private static String createPreviewLine(
            WayfinderArrow arrow,
            String text
    ) {
        String arrowSymbol = getArrowSymbol(arrow);
        String displayedText = text.isBlank() ? "Destination" : text;

        if (arrowSymbol.isEmpty()) {
            return displayedText;
        }

        return arrowSymbol + " " + displayedText;
    }

    private void handleSave() {
        if (lineOneField == null || lineTwoField == null) {
            return;
        }

        ClientPlayNetworking.send(
                new SaveWayfinderPostPayload(
                        blockPos,
                        lineOneField.getValue(),
                        lineTwoField.getValue(),
                        selectedArrow.getSerializedName()
                )
        );

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

        int top = height / 2 - 110;

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

        String firstLine = lineOneField == null
                ? initialLineOne
                : lineOneField.getValue();

        String secondLine = lineTwoField == null
                ? initialLineTwo
                : lineTwoField.getValue();

        String previewFirstLine = createPreviewLine(
                selectedArrow,
                firstLine
        );

        String previewSecondLine = secondLine.isBlank()
                ? ""
                : secondLine;

        int previewCenterX = width / 2;
        int previewTop = top + 120;

        graphics.text(
                font,
                "Preview",
                previewCenterX - font.width("Preview") / 2,
                previewTop,
                0xFFAAAAAA,
                true
        );

        int boardLeft = previewCenterX - 100;
        int boardTop = previewTop + 14;
        int boardWidth = 200;
        int boardHeight = 38;

        graphics.fill(
                boardLeft,
                boardTop,
                boardLeft + boardWidth,
                boardTop + boardHeight,
                0xFF6F4528
        );

        graphics.fill(
                boardLeft + 2,
                boardTop + 2,
                boardLeft + boardWidth - 2,
                boardTop + boardHeight - 2,
                0xFF9B673C
        );

        graphics.text(
                font,
                previewFirstLine,
                previewCenterX - font.width(previewFirstLine) / 2,
                boardTop + 7,
                0xFF2B190F,
                false
        );

        if (!previewSecondLine.isEmpty()) {
            graphics.text(
                    font,
                    previewSecondLine,
                    previewCenterX
                            - font.width(previewSecondLine) / 2,
                    boardTop + 21,
                    0xFF2B190F,
                    false
            );
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}