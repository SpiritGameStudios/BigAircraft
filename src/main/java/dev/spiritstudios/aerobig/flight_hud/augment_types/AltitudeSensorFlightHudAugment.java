package dev.spiritstudios.aerobig.flight_hud.augment_types;

import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.altitude_sensor.AltitudeSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.client.render.MonoNumberFont;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.client.render.FlightHudNumberRenderer;
import dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes;
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
        int x = graphics.guiWidth() - graphics.guiWidth() / 4,
            y = graphics.guiHeight() / 2 - ALTITUDE_TEXT_HEIGHT / 2;

        FlightHudNumberRenderer numberRenderer = new FlightHudNumberRenderer(graphics, MonoNumberFont.BIG_BLOCK, BigAircraftRenderTypes.GUI_TEXTURED);

        numberRenderer.drawDouble(blockEntity.getAirPressure(), x, y);
        numberRenderer.drawDouble(blockEntity.getWorldHeight(), x, y + MonoNumberFont.BIG_BLOCK.textureHeight());
    }

}
