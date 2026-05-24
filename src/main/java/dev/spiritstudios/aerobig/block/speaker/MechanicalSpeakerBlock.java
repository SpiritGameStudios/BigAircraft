package dev.spiritstudios.aerobig.block.speaker;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import dev.spiritstudios.aerobig.registry.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

public class MechanicalSpeakerBlock extends DirectionalKineticBlock implements IBE<MechanicalSpeakerBlockEntity> {

    public static final Function<BlockState, MapColor> MAP_COLOR_PROVIDER = state -> state.getValue(FACING) == Direction.UP ? MapColor.WOOD : MapColor.TERRACOTTA_YELLOW;

    public MechanicalSpeakerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == getShaftDirection(state);
    }

    public static Direction getShaftDirection(BlockState state) {
        return state.getValue(FACING).getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public Class<MechanicalSpeakerBlockEntity> getBlockEntityClass() {
        return MechanicalSpeakerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalSpeakerBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.MECHANICAL_SPEAKER.get();
    }

}
