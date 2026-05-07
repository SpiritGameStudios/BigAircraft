package dev.spiritstudios.aerobig.util;

import com.google.common.collect.Maps;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.connected.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.EnumMap;
import java.util.function.BiPredicate;

/**
 * TODO: apply to all instances of the carbon composite sprites
 */
public interface ModSpriteShifts {

    BiPredicate<BlockState, Direction> AXES_MATCH_PREDICATE = (state, direction) -> direction.getAxis() == state.getValue(BlockStateProperties.AXIS);

    EnumMap<DyeColor, CTSpriteShiftEntry> CARBON_COMPOSITES = Util.make(Maps.newEnumMap(DyeColor.class), map -> {
        for (DyeColor color : DyeColor.values())
            map.put(color, register(AllCTTypes.OMNIDIRECTIONAL, ModBlocks.DEFAULT_WHITE_NAME.apply(color, "carbon_composite")));
    });

    static NonNullConsumer<? super Block> registerSimpleCT(DyeColor color) {
        return CreateRegistrate.connectedTextures(() -> new SimpleCTBehaviour(CARBON_COMPOSITES.get(color)));
    }

    static NonNullConsumer<? super Block> registerCasingCT(DyeColor color, BiPredicate<BlockState, Direction> predicate) {
        return block -> {
            CTSpriteShiftEntry spriteShiftEntry = CARBON_COMPOSITES.get(color);

            CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(spriteShiftEntry)).accept(block);
            CreateRegistrate.casingConnectivity((b, connectivity) -> connectivity.make(b, spriteShiftEntry, predicate)).accept(block);
        };
    }

    static CTSpriteShiftEntry register(CTType type, String path) {
        return CTSpriteShifter.getCT(type, BigAircraft.id("block/" + path), BigAircraft.id("block/" + path + "_connected"));
    }

}
