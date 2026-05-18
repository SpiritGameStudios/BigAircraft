package dev.spiritstudios.aerobig.flight_hud.augment_types;

import dev.eriksonn.aeronautics.content.blocks.hot_air.balloon.ClientBalloon;
import dev.eriksonn.aeronautics.content.blocks.hot_air.steam_vent.SteamVentBlockEntity;
import dev.eriksonn.aeronautics.index.AeroBlockEntityTypes;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.registry.ModFlightHudAugments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.apache.commons.compress.utils.Sets;

import java.util.Set;

public class SteamVentFlightHudAugment extends FlightHudAugmentType<SteamVentBlockEntity> {

    private double lastLift = 0;

    public SteamVentFlightHudAugment() {
        super(AeroBlockEntityTypes.STEAM_VENT);
    }

    @Override
    public Set<FlightHudAugmentType<?>> getExclusives() {
        return Sets.newHashSet(ModFlightHudAugments.HOT_AIR_BURNER.get());
    }

    @Override
    public void render(SteamVentBlockEntity blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel subLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        ClientBalloon balloon = (ClientBalloon) blockEntity.getBalloon();

        if (!HotAirBurnerFlightHudAugment.renderNoBalloon(balloon, graphics)) {
            assert balloon != null;

            double lift = HotAirBurnerFlightHudAugment.getCumulativeLift(balloon, level);
            this.lastLift = Mth.lerp(partialTick, this.lastLift, lift);

            HotAirBurnerFlightHudAugment.renderLift(graphics, this.lastLift, lift);
        }
    }

}
