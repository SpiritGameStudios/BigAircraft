package dev.spiritstudios.aerobig.mixin.flight_hud_augment;

import dev.eriksonn.aeronautics.content.blocks.hot_air.BlockEntityLiftingGasProvider;
import dev.eriksonn.aeronautics.content.blocks.hot_air.steam_vent.SteamVentBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SteamVentBlockEntity.class)
public interface SteamVentBlockEntityAccessor {

    @Accessor("clientBalloonInfo")
    BlockEntityLiftingGasProvider.ClientBalloonInfo getClientBalloonInfo();

}
