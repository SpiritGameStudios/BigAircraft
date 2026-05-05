package dev.spiritstudios.aerobig.mixin;

import com.simibubi.create.compat.computercraft.implementation.ComputerBehaviour;
import com.simibubi.create.compat.computercraft.implementation.peripherals.SyncedPeripheral;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerBlockEntity;
import dev.spiritstudios.aerobig.block.analog_speed_controller.AnalogSpeedControllerPeripheral;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ComputerBehaviour.class)
public class ComputerBehaviourMixin {

    @Inject(method = "getPeripheralFor", at = @At("HEAD"), cancellable = true)
    private static void addAnalogSpeedControllerPeripheral(SmartBlockEntity be, CallbackInfoReturnable<Supplier<SyncedPeripheral<?>>> cir) {
        if (be instanceof AnalogSpeedControllerBlockEntity analogSpeedController)
            cir.setReturnValue(() -> new AnalogSpeedControllerPeripheral(analogSpeedController, analogSpeedController.targetSpeed));
    }

}
