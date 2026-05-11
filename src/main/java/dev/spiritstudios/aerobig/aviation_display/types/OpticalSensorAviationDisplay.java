package dev.spiritstudios.aerobig.aviation_display.types;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.lasers.optical_sensor.OpticalSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;

public class OpticalSensorAviationDisplay extends AviationDisplayType<OpticalSensorBlockEntity> {

    public OpticalSensorAviationDisplay() {
        super(SimBlockEntityTypes.OPTICAL_SENSOR, true);
    }

    @Override
    public boolean canDisplay(OpticalSensorBlockEntity blockEntity, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos) {
        return super.canDisplay(blockEntity, level, beSubLevel, pos) && blockEntity.hasHit();
    }

    @Override
    public void display(OpticalSensorBlockEntity blockEntity, Minecraft minecraft, GuiGraphics graphics, PoseStack poseStack, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos, int windowHeight, int windowWidth, float partialTick) {
        this.write(minecraft, graphics, String.valueOf(blockEntity.getHitBlockDistance()), 0, 0, true);
    }

}
