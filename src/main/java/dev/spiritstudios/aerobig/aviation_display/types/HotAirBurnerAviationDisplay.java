package dev.spiritstudios.aerobig.aviation_display.types;

import dev.eriksonn.aeronautics.content.blocks.hot_air.hot_air_burner.HotAirBurnerBlockEntity;
import dev.eriksonn.aeronautics.index.AeroBlockEntityTypes;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CommonColors;

public class HotAirBurnerAviationDisplay extends AviationDisplayType<HotAirBurnerBlockEntity> {

    public HotAirBurnerAviationDisplay() {
        super(AeroBlockEntityTypes.HOT_AIR_BURNER);
    }

    @Override
    public void render(HotAirBurnerBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel beSubLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        if (!blockEntity.canOutputGas()) return;

        graphics.drawString(
                mc.font,
                "G%.2f".formatted(blockEntity.getGasOutput()),
                0, 0,
                CommonColors.WHITE
        );
    }

}
