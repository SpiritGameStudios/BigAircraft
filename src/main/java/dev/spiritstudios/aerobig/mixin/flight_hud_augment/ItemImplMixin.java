package dev.spiritstudios.aerobig.mixin.flight_hud_augment;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ItemImplMixin {
    @WrapMethod(method = "inventoryTick")
    public void inventoryTickImpl(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected, Operation<Void> original) {
        original.call(stack, level, entity, slotId, isSelected);
    }

    @WrapMethod(method = "useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;")
    public InteractionResult useOnImpl(UseOnContext context, Operation<InteractionResult> original) {
        return original.call(context);
    }

    @WrapMethod(method = "isFoil")
    public boolean isFoilImpl(ItemStack stack, Operation<Boolean> original) {
        return original.call(stack);
    }

}
