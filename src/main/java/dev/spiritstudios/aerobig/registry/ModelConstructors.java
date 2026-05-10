package dev.spiritstudios.aerobig.registry;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.spiritstudios.aerobig.block.CarbonCompositeWingShaftBlock;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

import static dev.spiritstudios.aerobig.registry.ModBlocks.DEFAULT_WHITE_NAME;

public final class ModelConstructors {
    static NonNullBiConsumer<DataGenContext<Block, AnalogSpeedControllerBlock>, RegistrateBlockstateProvider> analogSpeedController() {
        return (context, provider) -> provider
            .getVariantBuilder(context.get())
            .forAllStates(state -> {
                Direction.Axis axis = state.getValue(BlockStateProperties.HORIZONTAL_AXIS);
                return ConfiguredModel.builder()
                    .modelFile(provider
                        .models()
                        .getExistingFile(
                            provider.modLoc("block/" + context.getName() + (state.getValue(BlockStateProperties.POWERED) ? "_powered" : ""))
                        )
                    )
                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                    .build();
            });
    }

    static NonNullBiConsumer<DataGenContext<Block, CarbonCompositeWingShaftBlock>, RegistrateBlockstateProvider> wingShaft(DyeColor color) {
        return (context, provider) -> provider
            .getVariantBuilder(context.get())
            .forAllStatesExcept(state -> {
                Direction.Axis axis = state.getValue(BlockStateProperties.HORIZONTAL_AXIS);
                ConfiguredModel.Builder<?> builder = ConfiguredModel.builder()
                    .rotationY(axis == Direction.Axis.X ? -90 : 0) // idk why it needs to b negative :cc
                    .uvLock(true);

                boolean positive = state.getValue(CarbonCompositeWingShaftBlock.CONNECT_POSITIVE);
                boolean negative = state.getValue(CarbonCompositeWingShaftBlock.CONNECT_NEGATIVE);

                String suffix;

                if (positive && negative) suffix = "";
                else if (positive) suffix = "_shaft_positive";
                else if (negative) suffix = "_shaft_negative";
                else suffix = "_shaft";

                return builder.modelFile(provider.models()
                    .withExistingParent(context.getName() + suffix, provider.modLoc("block/template_wing" + suffix))
                    .texture("side", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite_wing_side")))
                    .texture("top", provider.modLoc("block/" + DEFAULT_WHITE_NAME.apply(color, "carbon_composite")))
                ).build();
            }, BlockStateProperties.WATERLOGGED);
    }

}
