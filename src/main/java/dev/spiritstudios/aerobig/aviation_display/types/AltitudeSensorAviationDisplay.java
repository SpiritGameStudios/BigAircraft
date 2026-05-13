package dev.spiritstudios.aerobig.aviation_display.types;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.altitude_sensor.AltitudeSensorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import dev.spiritstudios.aerobig.client.render.AviationHudNumberRenderer;
import dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;

public class AltitudeSensorAviationDisplay extends AviationDisplayType<AltitudeSensorBlockEntity> {

    private static final int ALTITUDE_TEXT_HEIGHT = 118;

    public AltitudeSensorAviationDisplay() {
        super(SimBlockEntityTypes.ALTITUDE_SENSOR);
    }

    @Override
    public void render(AltitudeSensorBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel subLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        AviationHudNumberRenderer numberRenderer = new AviationHudNumberRenderer(graphics, AviationHudNumberRenderer.Font.BOLD);

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
            GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
            GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
        );

        numberRenderer.drawDouble(
            blockEntity.getNormalHeight(),
            graphics.guiWidth() - graphics.guiWidth() / 4,
            graphics.guiHeight() / 2 - ALTITUDE_TEXT_HEIGHT / 2,
            AviationHudNumberRenderer.Alignment.LEFT
        );

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
