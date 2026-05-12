package dev.spiritstudios.aerobig.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.aerobig.registry.ModBuiltInRegistries;
import dev.spiritstudios.aerobig.registry.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

    /**
     * TODO: fix this just like outright not working at all
     */
    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        if (this.displays.isEmpty())
            return;

        tooltipAdder.accept(Component.literal("Observing:").withStyle(ChatFormatting.GRAY));

        for (AviationDisplay display : this.displays) {
            ResourceLocation key = ModBuiltInRegistries.AVIATION_DISPLAY_TYPE.getKey(display.type());

            if (key == null)
                continue;

            tooltipAdder.accept(CommonComponents.SPACE.copy().append(Component.translatable(key.toLanguageKey()).withStyle(ChatFormatting.GOLD)));
        }
    }

    public static AviationDisplaysComponent getFromItemStack(ItemStack itemStack) {
        return itemStack.getOrDefault(ModDataComponents.AVIATION_DISPLAYS, DEFAULT);
    }

    public AviationDisplaysComponent with(AviationDisplay display) {
        List<AviationDisplay> newList = new ArrayList<>(this.displays);
        newList.add(display);

        return new AviationDisplaysComponent(newList);
    }

    public AviationDisplaysComponent removePosition(GlobalPos globalPos) {
        List<AviationDisplay> newList = new ArrayList<>(this.displays);

        for (AviationDisplay display : this.displays) {
            if (display.pos().equals(globalPos))
                newList.remove(display);
        }

        return new AviationDisplaysComponent(newList);
    }
}
