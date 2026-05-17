package dev.spiritstudios.aerobig.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.registry.ModDataComponents;
import dev.spiritstudios.aerobig.registry.ModI18N;
import net.minecraft.ChatFormatting;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;

/**
 * TODO: restrict augmentations to only work within a single sublevel. compare new list entry with contents of previous list. maybe change from GlobalPos to the sublevel id (for stability too)?
 */
@ParametersAreNonnullByDefault
public record FlightHudAugmentsComponent(List<FlightHudAugment> augments) implements TooltipProvider {
    public static final FlightHudAugmentsComponent EMPTY = new FlightHudAugmentsComponent(List.of());

    public static final Codec<FlightHudAugmentsComponent> CODEC = RecordCodecBuilder.<FlightHudAugmentsComponent>create(instance -> instance
        .group(FlightHudAugment.CODEC.listOf()
            .optionalFieldOf("augments", List.of())
            .forGetter(FlightHudAugmentsComponent::augments)
        )
        .apply(instance, FlightHudAugmentsComponent::new)
    ).validate(FlightHudAugmentsComponent::validate);

    public static final StreamCodec<RegistryFriendlyByteBuf, FlightHudAugmentsComponent> STREAM_CODEC = FlightHudAugment.STREAM_CODEC
        .apply(ByteBufCodecs.list())
        .map(FlightHudAugmentsComponent::new, FlightHudAugmentsComponent::augments);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        if (this.augments.isEmpty())
            return;

        tooltipAdder.accept(CommonComponents.EMPTY);
        tooltipAdder.accept(ModI18N.AUGMENTS.withStyle(ChatFormatting.GOLD));

        for (FlightHudAugment augment : this.augments) {
            tooltipAdder.accept(Component.literal("- ")
                .append(ModI18N.flightHudAugment(augment.type()))
                .withStyle(ChatFormatting.GRAY)
            );
        }
    }

    public static FlightHudAugmentsComponent getFromItemStack(ItemStack itemStack) {
        return itemStack.getOrDefault(ModDataComponents.FLIGHT_HUD_AUGMENTS, EMPTY);
    }

    private static DataResult<FlightHudAugmentsComponent> validate(FlightHudAugmentsComponent component) {
        Set<GlobalPos> globalPosSet = new HashSet<>();
        Set<FlightHudAugmentType<?>> flightHudAugmentSet = new HashSet<>();

        for (FlightHudAugment augment : component.augments) {
            GlobalPos globalPos = augment.target();
            if (globalPosSet.contains(globalPos))
                return DataResult.error(() -> "Augmentations must not target the same position");

            FlightHudAugmentType<?> type = augment.type();
            if (flightHudAugmentSet.contains(type))
                return DataResult.error(() -> "Augmentations must be distinct");

            globalPosSet.add(globalPos);
            flightHudAugmentSet.add(type);
        }

        return DataResult.success(component);
    }

    public boolean hasAnyMatchingData(GlobalPos globalPos, FlightHudAugmentType<?> augmentType) {
        for (FlightHudAugment augment : this.augments()) {
            if (augment.target().equals(globalPos) || augment.type().equals(augmentType))
                return true;
        }

        return false;
    }

    public static boolean removeIfEmpty(FlightHudAugmentsComponent component, ItemStack stack) {
        if (component.augments().isEmpty()) {
            stack.remove(ModDataComponents.FLIGHT_HUD_AUGMENTS);
            return true;
        }

        return false;
    }

    public FlightHudAugmentsComponent with(FlightHudAugment augment) {
        List<FlightHudAugment> newList = new ArrayList<>(this.augments);
        newList.add(augment);

        return new FlightHudAugmentsComponent(newList);
    }

    public FlightHudAugmentsComponent removePosition(GlobalPos globalPos) {
        List<FlightHudAugment> newList = new ArrayList<>(this.augments);

        for (FlightHudAugment augment : this.augments) {
            if (augment.target().equals(globalPos))
                newList.remove(augment);
        }

        return new FlightHudAugmentsComponent(newList);
    }
}
