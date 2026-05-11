package dev.spiritstudios.aerobig.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import dev.spiritstudios.aerobig.registry.ModRegistries;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public record ObservedAviationDisplaysComponent(Map<BlockPos, ResourceKey<AviationDisplayType<?>>> observedPositions) implements TooltipProvider {

    public static final ObservedAviationDisplaysComponent DEFAULT = new ObservedAviationDisplaysComponent(Map.of());

    public static final Codec<ObservedAviationDisplaysComponent> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(Codec.dispatchedMap(
                BlockPos.CODEC,
                blockPos -> ResourceKey.codec(ModRegistries.AVIATION_DISPLAY_TYPE)
            )
            .optionalFieldOf("observed_positions", Map.of())
            .forGetter(ObservedAviationDisplaysComponent::observedPositions)
        )
        .apply(instance, ObservedAviationDisplaysComponent::new)
    );
    public static final StreamCodec<ByteBuf, ObservedAviationDisplaysComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {

    }

}
