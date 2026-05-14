package dev.spiritstudios.aerobig.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.registry.ModBuiltInRegistries;
import dev.spiritstudios.aerobig.registry.ModRegistries;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

public record FlightHudAugment(FlightHudAugmentType<?> type, GlobalPos target) {
    public static final Codec<FlightHudAugment> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ModBuiltInRegistries.FLIGHT_HUD_AUGMENTS.byNameCodec().fieldOf("type").forGetter(FlightHudAugment::type),
            GlobalPos.CODEC.fieldOf("target").forGetter(FlightHudAugment::target)
        )
        .apply(instance, FlightHudAugment::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FlightHudAugment> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.registry(ModRegistries.FLIGHT_HUD_AUGMENT), FlightHudAugment::type,
        GlobalPos.STREAM_CODEC, FlightHudAugment::target,
        FlightHudAugment::new
    );

    public boolean isIn(ClientSubLevel subLevel) {
        GlobalPos globalPos = this.target();
        return globalPos.dimension() == subLevel.getLevel().dimension() && subLevel.getPlot().contains(globalPos.pos().getCenter());
    }
}
