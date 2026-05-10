package dev.spiritstudios.aerobig.util;

import com.google.common.collect.Maps;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.connected.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.block.ICarbonCompositeWing;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import net.minecraft.Util;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.function.BiPredicate;
import java.util.function.Function;

public final class ModSpriteShifts {
    public static final BiPredicate<BlockState, Direction> AXES_MATCH_PREDICATE = (state, direction) -> direction.getAxis() == state.getValue(BlockStateProperties.AXIS);

    public static final EnumMap<DyeColor, CTSpriteShiftEntry> CARBON_COMPOSITES = Util.make(Maps.newEnumMap(DyeColor.class), map -> {
        for (DyeColor color : DyeColor.values())
            map.put(color, register(AllCTTypes.OMNIDIRECTIONAL, ModBlocks.DEFAULT_WHITE_NAME.apply(color, "carbon_composite")));
    });

    public static NonNullConsumer<? super Block> registerCT(DyeColor color, Function<CTSpriteShiftEntry, ? extends ConnectedTextureBehaviour> ctBehaviour) {
        return CreateRegistrate.connectedTextures(() -> ctBehaviour.apply(CARBON_COMPOSITES.get(color)));
    }

    public static NonNullConsumer<? super Block> registerCasingCT(DyeColor color, BiPredicate<BlockState, Direction> predicate) {
        return block -> {
            CTSpriteShiftEntry spriteShiftEntry = CARBON_COMPOSITES.get(color);

            CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(spriteShiftEntry)).accept(block);
            CreateRegistrate.casingConnectivity((b, connectivity) -> connectivity.make(b, spriteShiftEntry, predicate)).accept(block);
        };
    }

    public static CTSpriteShiftEntry register(CTType type, String path) {
        return CTSpriteShifter.getCT(type, BigAircraft.id("block/" + path), BigAircraft.id("block/" + path + "_connected"));
    }

    public static class WingCTBehaviour extends ConnectedTextureBehaviour.Base {
        protected CTSpriteShiftEntry shift;

        public WingCTBehaviour(CTSpriteShiftEntry shift) {
            this.shift = shift;
        }

        @Override
        public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
            if (this.isBeingBlocked(state, reader, pos, otherPos, face))
                return false;

            if (state.getBlock() instanceof ICarbonCompositeWing wing && other.getBlock() instanceof ICarbonCompositeWing otherWing)
                return wing.getDyeColor() == otherWing.getDyeColor();

            return false;
        }

        @Nullable
        @Override
        public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
            return this.shift;
        }

    }
}
