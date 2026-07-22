package com.swornhero.wayfinderposts.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.swornhero.wayfinderposts.block.WayfinderPostBlock;
import com.swornhero.wayfinderposts.blockentity.WayfinderArrow;
import com.swornhero.wayfinderposts.blockentity.WayfinderPostBlockEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class WayfinderPostBlockEntityRenderer
        implements BlockEntityRenderer<
        WayfinderPostBlockEntity,
        WayfinderPostRenderState
        > {

    private static final float TEXT_SCALE = 1.0F / 96.0F;

    private final Font font;

    public WayfinderPostBlockEntityRenderer(
            BlockEntityRendererProvider.Context context
    ) {
        this.font = context.font();
    }

    @Override
    public WayfinderPostRenderState createRenderState() {
        return new WayfinderPostRenderState();
    }

    @Override
    public void extractRenderState(
            WayfinderPostBlockEntity blockEntity,
            WayfinderPostRenderState state,
            float tickProgress,
            Vec3 cameraPos,
            @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
    ) {
        BlockEntityRenderer.super.extractRenderState(
                blockEntity,
                state,
                tickProgress,
                cameraPos,
                crumblingOverlay
        );

        state.setFirstLine(
                createFirstLine(
                        blockEntity.getArrow(),
                        blockEntity.getLineOne()
                )
        );

        state.setSecondLine(blockEntity.getLineTwo());

        state.setFacing(
                blockEntity.getBlockState()
                        .getValue(WayfinderPostBlock.FACING)
        );
    }

    @Override
    public void submit(
            WayfinderPostRenderState state,
            PoseStack matrices,
            SubmitNodeCollector queue,
            CameraRenderState cameraState
    ) {
        String firstLine = state.getFirstLine();
        String secondLine = state.getSecondLine();

        if (firstLine.isBlank() && secondLine.isBlank()) {
            return;
        }

        matrices.pushPose();

        /*
         * Temporary shared position above the arm.
         * Precise board-face positioning comes next.
         */
        matrices.translate(0.5, 1.08, 0.5);

        matrices.mulPose(
                Axis.YP.rotationDegrees(
                        -state.getFacing().toYRot()
                )
        );

        matrices.scale(
                TEXT_SCALE,
                -TEXT_SCALE,
                TEXT_SCALE
        );

        submitCenteredLine(
                queue,
                matrices,
                firstLine,
                -9.0F,
                state.lightCoords
        );

        submitCenteredLine(
                queue,
                matrices,
                secondLine,
                2.0F,
                state.lightCoords
        );

        matrices.popPose();
    }

    private void submitCenteredLine(
            SubmitNodeCollector queue,
            PoseStack matrices,
            String text,
            float y,
            int light
    ) {
        if (text.isBlank()) {
            return;
        }

        float textWidth = font.width(text);

        queue.submitText(
                matrices,
                -textWidth / 2.0F,
                y,
                Component.literal(text).getVisualOrderText(),
                false,
                Font.DisplayMode.SEE_THROUGH,
                light,
                0xFF2B190F,
                0,
                0
        );
    }

    private static String createFirstLine(
            WayfinderArrow arrow,
            String lineOne
    ) {
        String symbol = switch (arrow) {
            case NONE -> "";
            case LEFT -> "←";
            case RIGHT -> "→";
            case FORWARD -> "↑";
        };

        if (lineOne.isBlank()) {
            return symbol;
        }

        if (symbol.isEmpty()) {
            return lineOne;
        }

        return symbol + " " + lineOne;
    }
}