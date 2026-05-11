package dev.spiritstudios.aerobig.aviation_display.types;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.simulated_team.simulated.content.blocks.docking_connector.DockingConnectorBlockEntity;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;

public class DockingConnectorAviationDisplay extends AviationDisplayType<DockingConnectorBlockEntity> {

    public DockingConnectorAviationDisplay() {
        super(SimBlockEntityTypes.DOCKING_CONNECTOR, true);
    }

    @Override
    public boolean canDisplay(DockingConnectorBlockEntity blockEntity, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos) {
        return super.canDisplay(blockEntity, level, beSubLevel, pos) && blockEntity.hasOtherConnector();
    }

    @Override
    public void display(DockingConnectorBlockEntity blockEntity, Minecraft minecraft, GuiGraphics graphics, PoseStack poseStack, ClientLevel level, ClientSubLevel beSubLevel, Vec3 pos, int windowHeight, int windowWidth, float partialTick) {
        this.write(minecraft, graphics, "Docking.", 0, 0, true);
    }

}
