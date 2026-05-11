package dev.spiritstudios.aerobig.aviation_display.types;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.eriksonn.aeronautics.content.blocks.hot_air.hot_air_burner.HotAirBurnerBlockEntity;
import dev.eriksonn.aeronautics.index.AeroBlockEntityTypes;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;

public class HotAirBurnerAviationDisplay extends AviationDisplayType<HotAirBurnerBlockEntity> {

    public HotAirBurnerAviationDisplay() {
        super(AeroBlockEntityTypes.HOT_AIR_BURNER, true);
    }

    @Override
    public boolean canDisplay(HotAirBurnerBlockEntity blockEntity, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos) {
        return super.canDisplay(blockEntity, level, beSubLevel, pos) && blockEntity.canOutputGas();
    }

    @Override
    public void display(HotAirBurnerBlockEntity blockEntity, Minecraft minecraft, GuiGraphics graphics, PoseStack poseStack, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos, int windowHeight, int windowWidth, float partialTick) {
        this.write(minecraft, graphics, "G%.2f".formatted(blockEntity.getGasOutput()), 0, 0, true);
    }

}
