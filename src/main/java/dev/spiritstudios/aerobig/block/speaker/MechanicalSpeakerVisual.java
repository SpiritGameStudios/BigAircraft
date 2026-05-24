package dev.spiritstudios.aerobig.block.speaker;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;

public class MechanicalSpeakerVisual extends OrientedRotatingVisual<MechanicalSpeakerBlockEntity> {

    public MechanicalSpeakerVisual(VisualizationContext context, MechanicalSpeakerBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Direction.SOUTH, MechanicalSpeakerBlock.getShaftDirection(blockEntity.getBlockState()), Models.partial(AllPartialModels.SHAFT_HALF));
    }

}
