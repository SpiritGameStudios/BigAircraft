package dev.spiritstudios.aerobig.aviation_display.types;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.velocity_sensor.VelocitySensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3dc;

public class VelocitySensorAviationDisplay extends AviationDisplayType<VelocitySensorBlockEntity> {

    private static final double BLOCKS_PER_TICK_TO_KNOTS = 18000.0 / 463.0;

    public VelocitySensorAviationDisplay() {
        super(SimBlockEntityTypes.VELOCITY_SENSOR, false);
    }

    @Override
    public void display(VelocitySensorBlockEntity blockEntity, Minecraft minecraft, GuiGraphics graphics, PoseStack poseStack, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos, int windowHeight, int windowWidth, float partialTick) {
        Vector3dc logicalVec = beSubLevel.logicalPose().position();
        Vector3dc prevVec = beSubLevel.lastPose().position();

        double dx = logicalVec.x() - prevVec.x();
        double dz = logicalVec.z() - prevVec.z();

        double airspeedBPT = Math.hypot(dx, dz);

        this.write(minecraft, graphics, "" + (int) (airspeedBPT * BLOCKS_PER_TICK_TO_KNOTS), 0, windowHeight / 2 + 10, true);
    }

}
