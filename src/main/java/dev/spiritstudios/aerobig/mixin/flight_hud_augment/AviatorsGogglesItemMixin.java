package dev.spiritstudios.aerobig.mixin.flight_hud_augment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.eriksonn.aeronautics.content.items.AviatorsGogglesItem;
import dev.ryanhcode.sable.Sable;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.component.FlightHudAugment;
import dev.spiritstudios.aerobig.component.FlightHudAugmentsComponent;
import dev.spiritstudios.aerobig.registry.ModDataComponents;
import dev.spiritstudios.aerobig.registry.ModI18N;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mixin(AviatorsGogglesItem.class)
public class AviatorsGogglesItemMixin extends ItemImplMixin {

    @Override
    public void inventoryTickImpl(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected, Operation<Void> original) {
        original.call(stack, level, entity, slotId, isSelected);

        if (level.getGameTime() % SharedConstants.TICKS_PER_SECOND == 1 && stack.has(ModDataComponents.FLIGHT_HUD_AUGMENTS))
            bigAircraft$removeInvalidEntries(level, FlightHudAugmentsComponent.getFromItemStack(stack), stack, entity);
    }

    @Override
    public boolean isFoilImpl(ItemStack stack, Operation<Boolean> original) {
        return super.isFoilImpl(stack, original) || !FlightHudAugmentsComponent.getFromItemStack(stack).augments().isEmpty();
    }

    @Override
    @NotNull
    public InteractionResult useOnImpl(UseOnContext context, Operation<InteractionResult> original) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockEntity blockEntity = level.getBlockEntity(clickedPos);

        if (player == null || blockEntity == null)
            return original.call(context);

        ItemStack itemInHand = context.getItemInHand();
        FlightHudAugmentsComponent component = FlightHudAugmentsComponent.getFromItemStack(itemInHand);

        GlobalPos target = GlobalPos.of(level.dimension(), clickedPos);
        FlightHudAugmentType<?> augmentType = FlightHudAugmentType.BY_BLOCK_ENTITY.get(blockEntity.getType());

        if (!bigAircraft$validate(augmentType, player, blockEntity, component, target))
            return InteractionResult.PASS;

        itemInHand.applyComponents(DataComponentMap.builder()
            .set(ModDataComponents.FLIGHT_HUD_AUGMENTS, component.with(new FlightHudAugment(augmentType, target)))
            .build()
        );

        return InteractionResult.SUCCESS;
    }

    @Unique
    private static boolean bigAircraft$validate(@Nullable FlightHudAugmentType<?> augmentType, Player player, BlockEntity blockEntity, FlightHudAugmentsComponent component, GlobalPos target) {
        if (augmentType == null) {
            player.displayClientMessage(ModI18N.FlightHudAugmentError.UNOBSERVABLE_INSTRUMENT.getText(), true);
            return false;
        }

        if (!Sable.HELPER.isInPlotGrid(blockEntity)) {
            player.displayClientMessage(ModI18N.FlightHudAugmentError.NOT_IN_SIMULATED_CONTRAPTION.getText(), true);
            return false;
        }

        if (component.hasAnyMatchingData(target, augmentType)) {
            player.displayClientMessage(ModI18N.FlightHudAugmentError.ALREADY_OBSERVING.getText(), true);
            return false;
        }

        return true;
    }

    @Unique
    private static void bigAircraft$removeInvalidEntries(Level level, FlightHudAugmentsComponent component, ItemStack stack, Entity entity) {
        if (FlightHudAugmentsComponent.removeIfEmpty(component, stack))
            return;

        for (FlightHudAugment augment : component.augments()) {
            GlobalPos target = augment.target();

            if (level.dimension() != target.dimension() || level.getBlockEntity(target.pos()) != null)
                continue;

            FlightHudAugmentsComponent newComponent = component.removePosition(target);

            if (entity instanceof Player player)
                player.displayClientMessage(ModI18N.FlightHudAugmentError.LOST_CONNECTION.getText(ModI18N.flightHudAugment(augment.type())), true);

            if (FlightHudAugmentsComponent.removeIfEmpty(newComponent, stack))
                return;

            stack.applyComponents(DataComponentMap.builder()
                .set(ModDataComponents.FLIGHT_HUD_AUGMENTS, newComponent)
                .build()
            );
        }
    }

}
