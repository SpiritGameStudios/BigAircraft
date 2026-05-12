package dev.spiritstudios.aerobig.aviation_display.types;

import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.altitude_sensor.AltitudeSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CommonColors;

public class AltitudeSensorAviationDisplay extends AviationDisplayType {

    private static final int ALTITUDE_TEXT_HEIGHT = 118;

    public AltitudeSensorAviationDisplay() {
        super(SimBlockEntityTypes.ALTITUDE_SENSOR);
    }

    @Override
    public void render(GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel subLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        if (!subLevel.getPlot().contains(player.position())) return;
        if (!(level.getBlockEntity(blockPos) instanceof AltitudeSensorBlockEntity sensor)) return;

        graphics.drawString(
                mc.font,
                "G%.2f".formatted(sensor.getNormalHeight()),
                graphics.guiWidth() - graphics.guiWidth() / 4, graphics.guiHeight() / 2 - ALTITUDE_TEXT_HEIGHT / 2,
                CommonColors.WHITE
        );
    }
}
