package dev.spiritstudios.aerobig.mixin.flight_hud_augment;

import com.simibubi.create.content.equipment.armor.BaseArmorItem;
import dev.eriksonn.aeronautics.content.items.AviatorsGogglesItem;
import dev.ryanhcode.sable.Sable;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.component.FlightHudAugment;
import dev.spiritstudios.aerobig.component.FlightHudAugmentsComponent;
import dev.spiritstudios.aerobig.registry.ModDataComponents;
import dev.spiritstudios.aerobig.registry.ModI18N;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mixin(AviatorsGogglesItem.class)
public class AviatorsGogglesItemMixin extends BaseArmorItem {

    public AviatorsGogglesItemMixin(Holder<ArmorMaterial> armorMaterial, Type type, Properties properties, ResourceLocation textureLoc) {
        super(armorMaterial, type, properties, textureLoc);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide() || level.getGameTime() % SharedConstants.TICKS_PER_SECOND != 1)
            return;

        FlightHudAugmentsComponent component = FlightHudAugmentsComponent.getFromItemStack(stack);

        for (FlightHudAugment augment : component.augments()) {
            GlobalPos globalPos = augment.target();

            if (level.dimension() != globalPos.dimension())
                continue;

            BlockEntity blockEntity = level.getBlockEntity(globalPos.pos());

            if (blockEntity != null)
                continue;

            FlightHudAugmentsComponent newComponent = component.removePosition(globalPos);

            if (newComponent.augments().isEmpty()) {
                stack.remove(ModDataComponents.FLIGHT_HUD_AUGMENTS);
                break;
            }
            else stack.applyComponents(DataComponentMap.builder()
                .set(ModDataComponents.FLIGHT_HUD_AUGMENTS, newComponent)
                .build()
            );

        }
    }

    @Override
    @NotNull
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockEntity blockEntity = level.getBlockEntity(clickedPos);

        if (player == null || blockEntity == null)
            return InteractionResult.PASS;

        FlightHudAugmentType<?> flightHudAugment = FlightHudAugmentType.BY_BLOCK_ENTITY.get(blockEntity.getType());

        if (flightHudAugment == null) {
            player.displayClientMessage(ModI18N.FlightHudAugment.ERROR_UNOBSERVABLE_INSTRUMENT.withStyle(ChatFormatting.RED), true);
            return InteractionResult.PASS;
        }

        if (!Sable.HELPER.isInPlotGrid(blockEntity)) {
            player.displayClientMessage(ModI18N.FlightHudAugment.ERROR_NOT_IN_SIMULATED_CONTRAPTION.withStyle(ChatFormatting.RED), true);
            return InteractionResult.PASS;
        }

        ItemStack itemInHand = context.getItemInHand();
        FlightHudAugmentsComponent fromItemStack = FlightHudAugmentsComponent.getFromItemStack(itemInHand);

        GlobalPos globalPos = GlobalPos.of(level.dimension(), clickedPos);

        if (fromItemStack.hasAnyMatchingData(globalPos, flightHudAugment)) {
            player.displayClientMessage(ModI18N.FlightHudAugment.ERROR_ALREADY_OBSERVING.withStyle(ChatFormatting.RED), true);
            return InteractionResult.PASS;
        }

        itemInHand.applyComponents(DataComponentMap.builder()
            .set(ModDataComponents.FLIGHT_HUD_AUGMENTS, fromItemStack.with(new FlightHudAugment(flightHudAugment, globalPos)))
            .build()
        );

        return InteractionResult.SUCCESS;
    }

}
