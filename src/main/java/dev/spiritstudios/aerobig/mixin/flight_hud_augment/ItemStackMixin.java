package dev.spiritstudios.aerobig.mixin.flight_hud_augment;

import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.aerobig.registry.ModDataComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract <T extends TooltipProvider> void addToTooltip(DataComponentType<T> component, Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag);

    @Inject(method = "getTooltipLines", at = @At(value = "FIELD", target = "Lnet/minecraft/core/component/DataComponents;JUKEBOX_PLAYABLE:Lnet/minecraft/core/component/DataComponentType;", opcode = Opcodes.GETSTATIC))
    private void addFlightHudAugmentTooltip(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, @Local Consumer<Component> consumer) {
        this.addToTooltip(ModDataComponents.FLIGHT_HUD_AUGMENTS.get(), tooltipContext, consumer, tooltipFlag);
    }

}
