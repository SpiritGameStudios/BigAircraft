package dev.spiritstudios.aerobig.aviation_display.types;

import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.docking_connector.DockingConnectorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CommonColors;

public class DockingConnectorAviationDisplay extends AviationDisplayType<DockingConnectorBlockEntity> {
    public DockingConnectorAviationDisplay() {
        super(SimBlockEntityTypes.DOCKING_CONNECTOR);
    }

    @Override
    public void render(DockingConnectorBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel beSubLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        if (!blockEntity.hasOtherConnector()) return;

        graphics.drawString(
                mc.font,
                "Docking.",
                0, 0,
                CommonColors.WHITE
        );
    }
}
