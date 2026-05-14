package dev.spiritstudios.aerobig.flight_hud.augment_types;

import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.lasers.optical_sensor.OpticalSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.block.BigOpticalSensorBlockEntity;
import dev.spiritstudios.aerobig.client.render.*;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.registry.ModSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class OpticalSensorFlightHudAugment extends FlightHudAugmentType<OpticalSensorBlockEntity> {

    private static final ResourceLocation TEXTURE = BigAircraft.id("textures/gui/sprites/aviation_display/optical_sensor.png");
    private static final int ICON_MARGIN = 10;
    private static final int ICON_HEIGHT = 12;
    private static final int TEXTURE_WIDTH = 20;
    private static final int TEXTURE_HEIGHT = 24;

    public OpticalSensorFlightHudAugment() {
        super(SimBlockEntityTypes.OPTICAL_SENSOR);
    }

    @Override
    public void render(OpticalSensorBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel beSubLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        int x = ICON_MARGIN * 2 + DockingConnectorFlightHudAugment.TEXTURE_SIZE;
        int y = graphics.guiHeight() - ICON_MARGIN - ICON_HEIGHT;

        FlightHudRenderer.renderSprite(
            graphics,
            TEXTURE,
            x,
            y,
            TEXTURE_WIDTH,
            ICON_HEIGHT,
            0.0F,
            blockEntity.hasHit() ? 0.0F : ICON_HEIGHT,
            TEXTURE_WIDTH,
            TEXTURE_HEIGHT,
            BigAircraftRenderTypes.GUI_TEXTURED
        );

        if (((BigOpticalSensorBlockEntity) blockEntity).bigAircraft$getPrevHitBlock() != blockEntity.getHitBlock() && blockEntity.hasHit()) {
            mc.getSoundManager().play(SimpleSoundInstance.forUI(ModSoundEvents.TERRAIN_TERRAIN_PULL_UP.get(), 1.0F));
        }

        if (blockEntity.hasHit()) {
            new FlightHudNumberRenderer(graphics, NumericalFont.BIG_BUBBLE, BigAircraftRenderTypes.GUI_TEXTURED).drawDouble(
                blockEntity.getHitBlockDistance() - 0.5,
                x + TEXTURE_WIDTH + 1,
                y,
                Alignment.LEFT
            );
        }
    }
}
