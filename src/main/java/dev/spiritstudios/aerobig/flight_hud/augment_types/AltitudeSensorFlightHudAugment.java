package dev.spiritstudios.aerobig.flight_hud.augment_types;

import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.altitude_sensor.AltitudeSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.client.render.FlightHudNumberRenderer;
import dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes;
import dev.spiritstudios.aerobig.client.render.NumericalFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;

public class AltitudeSensorFlightHudAugment extends FlightHudAugmentType<AltitudeSensorBlockEntity> {

    private static final int ALTITUDE_TEXT_HEIGHT = 118;

    public AltitudeSensorFlightHudAugment() {
        super(SimBlockEntityTypes.ALTITUDE_SENSOR);
    }

    @Override
    public void render(AltitudeSensorBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel subLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        new FlightHudNumberRenderer(graphics, NumericalFont.BOLD, BigAircraftRenderTypes.NUMBER_INVERT).drawDouble(
            blockEntity.getNormalHeight(),
            graphics.guiWidth() - graphics.guiWidth() / 4,
            graphics.guiHeight() / 2 - ALTITUDE_TEXT_HEIGHT / 2,
            FlightHudNumberRenderer.Alignment.LEFT
        );
    }
}
