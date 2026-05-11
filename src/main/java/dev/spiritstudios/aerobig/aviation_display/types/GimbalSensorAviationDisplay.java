package dev.spiritstudios.aerobig.aviation_display.types;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.gimbal_sensor.GimbalSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class GimbalSensorAviationDisplay extends AviationDisplayType<GimbalSensorBlockEntity> {

    private static final int HORIZON_LINE_CENTRE_PADDING = 7;
    private static final int LADDER_STEP_DIST = 5;

    public GimbalSensorAviationDisplay() {
        super(SimBlockEntityTypes.GIMBAL_SENSOR, false);
    }

    @Override
    public void display(GimbalSensorBlockEntity blockEntity, Minecraft minecraft, GuiGraphics graphics, PoseStack poseStack, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos, int windowHeight, int windowWidth, float partialTick) {
        float roll = (float) blockEntity.getZAngle();
        float pitch = (float) blockEntity.getXAngle();

        int windowCentreX = windowWidth / 2;
        int windowCentreY = windowHeight / 2;

        poseStack.pushPose();
        poseStack.translate(windowCentreX, windowCentreY, 0);
        poseStack.mulPose(Axis.ZN.rotationDegrees(Mth.wrapDegrees(roll * Mth.RAD_TO_DEG)));
        poseStack.translate(-windowCentreX, -windowCentreY, 0);

        int padX = windowWidth / 4;
        int padY = windowHeight / 4;

        graphics.enableScissor(padX, padY, windowWidth - padX, windowHeight - padY);

        final float scale = 4;

        for (int i = -360; i < 360; i += LADDER_STEP_DIST) {
            float angle = (i * Mth.DEG_TO_RAD) + pitch;
            float mag = angle / Mth.PI;
            mag *= scale;

            float up = -mag * windowHeight + windowCentreY - 1;

            int len = i % LADDER_STEP_DIST == 0 ? 25 : 10;

            if (i == 0)
                len = 40;

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
            );

            this.hLine(graphics, poseStack, (float) windowCentreX - HORIZON_LINE_CENTRE_PADDING, -len, up);
            this.hLine(graphics, poseStack, (float) windowCentreX + HORIZON_LINE_CENTRE_PADDING - 1, len, up);

            String str = String.format("%d", -i);

            if (i % LADDER_STEP_DIST == 0) {
                int y = (int) up - (minecraft.font.lineHeight / 2);

                this.write(minecraft, graphics, str, windowCentreX - len - 12 - minecraft.font.width(str), y, false);
                this.write(minecraft, graphics, str, windowCentreX + len + 12, y, false);
            }

            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }

        graphics.disableScissor();
        poseStack.popPose();
    }

}
