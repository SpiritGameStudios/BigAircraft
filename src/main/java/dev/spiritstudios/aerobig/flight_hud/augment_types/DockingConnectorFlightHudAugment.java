package dev.spiritstudios.aerobig.flight_hud.augment_types;

import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.docking_connector.DockingConnectorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.client.render.FlightHudRenderer;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class DockingConnectorFlightHudAugment extends FlightHudAugmentType<DockingConnectorBlockEntity> {

    private static final ResourceLocation TEXTURE = BigAircraft.id("textures/gui/sprites/aviation_display/docking_connector.png");
    private static final int ICON_MARGIN = 10;
    private static final int ICON_HEIGHT = 12;
    static final int TEXTURE_SIZE = 24;

    public DockingConnectorFlightHudAugment() {
        super(SimBlockEntityTypes.DOCKING_CONNECTOR);
    }

    @Override
    public void render(DockingConnectorBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel beSubLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        FlightHudRenderer.renderBooleanConditionalSprite(
            graphics,
            TEXTURE,
            ICON_MARGIN,
            graphics.guiHeight() - ICON_MARGIN - ICON_HEIGHT,
            TEXTURE_SIZE,
            TEXTURE_SIZE,
            ICON_HEIGHT,
            blockEntity.isLocked()
        );
    }
}
