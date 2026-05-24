package dev.spiritstudios.aerobig.block.speaker;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.client.render.BigAircraftIcons;
import dev.spiritstudios.aerobig.registry.ModI18N;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class MechanicalSpeakerBlockEntity extends KineticBlockEntity {

    public ScrollOptionBehaviour<SpeakingMode> speakingMode;

    public MechanicalSpeakerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        this.speakingMode = new ScrollOptionBehaviour<>(SpeakingMode.class, ModI18N.SPEAKING_MODE, this, new SpeakerScrollOptionSlot());
        this.speakingMode.value = SpeakingMode.BOTH.ordinal();

        behaviours.add(this.speakingMode);
    }

    public static class SpeakerScrollOptionSlot extends CenteredSideValueBoxTransform {

        public SpeakerScrollOptionSlot() {
            super((state, direction) -> direction.getAxis() != MechanicalSpeakerBlock.getShaftDirection(state).getAxis());
        }

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Vec3i normal = state.getValue(FACING).getNormal();
            return super.getLocalOffset(level, pos, state).add(Vec3.atLowerCornerOf(normal).scale(2.0 / 16.0));
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack stack) {
            if (this.getSide().getAxis().isVertical())
                TransformStack.of(stack).rotateYDegrees(AngleHelper.horizontalAngle(state.getValue(FACING)) + 180);

            super.rotate(level, pos, state, stack);
        }

    }

    public enum SpeakingMode implements INamedIconOptions {

        TEXT_ONLY(BigAircraftIcons.TEXT_ONLY, "text_only", "Text Only"),
        BOTH(BigAircraftIcons.BOTH, "both", "Text and Speech"),
        SPEAK_ONLY(BigAircraftIcons.SPEAK_ONLY, "speak_only", "Speech Only");

        private final BigAircraftIcons icon;
        private final String translationKey;
        private final String name;

        SpeakingMode(BigAircraftIcons icon, String path, String name) {
            this.icon = icon;
            this.translationKey = ModI18N.ofId("speaking_mode" + path);
            this.name = name;
        }

        @Override
        public AllIcons getIcon() {
            return this.icon;
        }

        @Override
        public String getTranslationKey() {
            return this.translationKey;
        }

        public static void registerLang() {
            for (SpeakingMode mode : values())
                BigAircraft.registrate().addRawLang(mode.translationKey, mode.name);
        }

    }

}
