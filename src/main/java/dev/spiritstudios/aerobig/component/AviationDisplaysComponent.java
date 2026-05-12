package dev.spiritstudios.aerobig.component;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import dev.spiritstudios.aerobig.registry.ModBuiltInRegistries;
import dev.spiritstudios.aerobig.registry.ModRegistries;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public record AviationDisplaysComponent(List<AviationDisplay> displays) implements TooltipProvider {
    public static final AviationDisplaysComponent DEFAULT = new AviationDisplaysComponent(List.of());

    public static final Codec<AviationDisplaysComponent> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(AviationDisplay.CODEC.listOf().optionalFieldOf("displays", List.of()).forGetter(AviationDisplaysComponent::displays))
            .apply(instance, AviationDisplaysComponent::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AviationDisplaysComponent> STREAM_CODEC = AviationDisplay.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(AviationDisplaysComponent::new, AviationDisplaysComponent::displays);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {

    }

    public AviationDisplaysComponent with(AviationDisplay display) {
        List<AviationDisplay> newList = new ArrayList<>(displays);
        newList.add(display);

        return new AviationDisplaysComponent(newList);
    }
}
