package dev.spiritstudios.aerobig.mixin.flight_hud_augment;

import dev.eriksonn.aeronautics.content.blocks.hot_air.BlockEntityLiftingGasProvider;
import dev.eriksonn.aeronautics.content.blocks.hot_air.hot_air_burner.HotAirBurnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HotAirBurnerBlockEntity.class)
public interface HotAirBurnerBlockEntityAccessor {

    @Accessor("clientBalloonInfo")
    BlockEntityLiftingGasProvider.ClientBalloonInfo getClientBalloonInfo();

}
