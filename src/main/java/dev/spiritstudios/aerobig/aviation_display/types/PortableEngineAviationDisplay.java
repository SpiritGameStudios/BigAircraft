package dev.spiritstudios.aerobig.aviation_display.types;

import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.portable_engine.PortableEngineBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CommonColors;

public class PortableEngineAviationDisplay extends AviationDisplayType<PortableEngineBlockEntity> {

    public PortableEngineAviationDisplay() {
        super(SimBlockEntityTypes.PORTABLE_ENGINE);
    }

    @Override
    public void render(PortableEngineBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel subLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        if (blockEntity.isTotalFuelInfinite() || blockEntity.getTotalBurnTime() <= 0)
            return;

        graphics.drawString(
                mc.font,
                "%s/%s".formatted(blockEntity.getCurrentBurnTime(), blockEntity.getTotalBurnTime()),
                0, 0,
                CommonColors.WHITE
        );
    }
}
