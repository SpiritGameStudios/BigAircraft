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

public class HotAirBurnerAviationDisplay extends AviationDisplayType {

    public HotAirBurnerAviationDisplay() {
        super(AeroBlockEntityTypes.HOT_AIR_BURNER);
    }

    @Override
    public void render(GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel beSubLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        if (!(level.getBlockEntity(blockPos) instanceof HotAirBurnerBlockEntity burner)) return;
        if (!burner.canOutputGas()) return;

        graphics.drawString(
                mc.font,
                "G%.2f".formatted(burner.getGasOutput()),
                0, 0,
                CommonColors.WHITE
        );
    }

}
