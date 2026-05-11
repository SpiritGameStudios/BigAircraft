package dev.spiritstudios.aerobig.aviation_display.types;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.portable_engine.PortableEngineBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;

public class PortableEngineAviationDisplay extends AviationDisplayType<PortableEngineBlockEntity> {

    public PortableEngineAviationDisplay() {
        super(SimBlockEntityTypes.PORTABLE_ENGINE, false);
    }

    @Override
    public boolean canDisplay(PortableEngineBlockEntity blockEntity, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos) {
        return super.canDisplay(blockEntity, level, beSubLevel, pos) && !blockEntity.isTotalFuelInfinite() && blockEntity.getTotalBurnTime() > 0;
    }

    @Override
    public void display(PortableEngineBlockEntity blockEntity, Minecraft minecraft, GuiGraphics graphics, PoseStack poseStack, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos, int windowHeight, int windowWidth, float partialTick) {
        this.write(minecraft, graphics, "%s/%s".formatted(blockEntity.getCurrentBurnTime(), blockEntity.getTotalBurnTime()), 0, 0, true);
    }

}
