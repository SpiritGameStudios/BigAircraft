package dev.spiritstudios.aerobig.aviation_display.types;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.altitude_sensor.AltitudeSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;

public class AltitudeSensorAviationDisplay extends AviationDisplayType<AltitudeSensorBlockEntity> {

    private static final int ALTITUDE_TEXT_HEIGHT = 118;

    public AltitudeSensorAviationDisplay() {
        super(SimBlockEntityTypes.ALTITUDE_SENSOR, false);
    }

    @Override
    public void display(AltitudeSensorBlockEntity blockEntity, Minecraft minecraft, GuiGraphics graphics, PoseStack poseStack, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos, int windowHeight, int windowWidth, float partialTick) {
        this.write(minecraft, graphics, "G%.2f".formatted(blockEntity.getNormalHeight()), windowWidth - windowWidth / 4, windowHeight / 2 - ALTITUDE_TEXT_HEIGHT / 2, true);
    }

}
