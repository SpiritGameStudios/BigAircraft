package dev.spiritstudios.aerobig.aviation_display.types;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.eriksonn.aeronautics.content.blocks.hot_air.steam_vent.SteamVentBlockEntity;
import dev.eriksonn.aeronautics.index.AeroBlockEntityTypes;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;

public class SteamVentAviationDisplay extends AviationDisplayType<SteamVentBlockEntity> {

    private static final int ALTITUDE_TEXT_HEIGHT = 118;

    public SteamVentAviationDisplay() {
        super(AeroBlockEntityTypes.STEAM_VENT, true);
    }

    @Override
    public boolean canDisplay(SteamVentBlockEntity blockEntity, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos) {
        return super.canDisplay(blockEntity, level, beSubLevel, pos) && blockEntity.canOutputGas();
    }

    @Override
    public void display(SteamVentBlockEntity blockEntity, Minecraft minecraft, GuiGraphics graphics, PoseStack poseStack, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos, int windowHeight, int windowWidth, float partialTick) {
        this.write(minecraft, graphics, "G%.2f".formatted(blockEntity.getGasOutput()), windowWidth - windowWidth / 4, windowHeight / 2 - ALTITUDE_TEXT_HEIGHT / 2, true);
    }

}
