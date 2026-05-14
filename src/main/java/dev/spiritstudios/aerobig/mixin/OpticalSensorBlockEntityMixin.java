package dev.spiritstudios.aerobig.mixin;

import dev.simulated_team.simulated.content.blocks.lasers.optical_sensor.OpticalSensorBlockEntity;
import dev.spiritstudios.aerobig.block.BigOpticalSensorBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OpticalSensorBlockEntity.class)
public class OpticalSensorBlockEntityMixin implements BigOpticalSensorBlockEntity {
    @Shadow
    private Block hitBlock;
    @Unique
    private Block bigAircraft$prevHitBlock = Blocks.AIR;

    @Inject(method = "checkFilter", at = @At(value = "FIELD", target = "Ldev/simulated_team/simulated/content/blocks/lasers/optical_sensor/OpticalSensorBlockEntity;hitBlock:Lnet/minecraft/world/level/block/Block;", opcode = Opcodes.PUTFIELD))
    private void meow(BlockHitResult context, CallbackInfoReturnable<Boolean> cir) {
        this.bigAircraft$prevHitBlock = this.hitBlock;
    }

    @Override
    public Block bigAircraft$getPrevHitBlock() {
        return bigAircraft$prevHitBlock;
    }
}
