package dev.spiritstudios.aerobig.aviation_display.types;

import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.lasers.optical_sensor.OpticalSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CommonColors;

public class OpticalSensorAviationDisplay extends AviationDisplayType<OpticalSensorBlockEntity> {

    public OpticalSensorAviationDisplay() {
        super(SimBlockEntityTypes.OPTICAL_SENSOR);
    }

    @Override
    public void render(OpticalSensorBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel beSubLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        if (!blockEntity.hasHit()) return;

        graphics.drawString(
                mc.font,
                "%.2f".formatted(blockEntity.getHitBlockDistance()),
                0, 0,
                CommonColors.WHITE
        );
    }
}
