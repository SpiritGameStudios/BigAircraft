package dev.spiritstudios.aerobig.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import dev.spiritstudios.aerobig.registry.ModBuiltInRegistries;
import dev.spiritstudios.aerobig.registry.ModRegistries;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record AviationDisplay(AviationDisplayType<?> type, GlobalPos pos) {
    public static final Codec<AviationDisplay> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            ModBuiltInRegistries.AVIATION_DISPLAY_TYPE.byNameCodec().fieldOf("type").forGetter(AviationDisplay::type),
                            GlobalPos.CODEC.fieldOf("pos").forGetter(AviationDisplay::pos)
                    )
                    .apply(instance, AviationDisplay::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AviationDisplay> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(ModRegistries.AVIATION_DISPLAY_TYPE), AviationDisplay::type,
            GlobalPos.STREAM_CODEC, AviationDisplay::pos,
            AviationDisplay::new
    );

    public boolean isIn(ClientSubLevel subLevel) {
        GlobalPos globalPos = this.pos();
        return globalPos.dimension() == subLevel.getLevel().dimension() && subLevel.getPlot().contains(globalPos.pos().getCenter());
    }
}
