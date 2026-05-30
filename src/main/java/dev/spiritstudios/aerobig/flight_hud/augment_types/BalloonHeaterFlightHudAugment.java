package dev.spiritstudios.aerobig.flight_hud.augment_types;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.eriksonn.aeronautics.content.blocks.hot_air.BlockEntityLiftingGasProvider;
import dev.eriksonn.aeronautics.content.blocks.hot_air.balloon.ClientBalloon;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.client.render.Alignment;
import dev.spiritstudios.aerobig.client.render.BigAircraftRenderTypes;
import dev.spiritstudios.aerobig.client.render.FlightHudNumberRenderer;
import dev.spiritstudios.aerobig.client.render.MonoNumberFont;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.mixin.flight_hud_augment.HotAirBurnerBlockEntityAccessor;
import dev.spiritstudios.aerobig.mixin.flight_hud_augment.SteamVentBlockEntityAccessor;
import dev.spiritstudios.aerobig.registry.ModFlightHudAugments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.compress.utils.Sets;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.Set;

public class BalloonHeaterFlightHudAugment<T extends BlockEntity & BlockEntityLiftingGasProvider> extends FlightHudAugmentType<T> {

    private static final ResourceLocation TEXTURE = BigAircraft.id("aviation_display/balloon");
    private static final ResourceLocation FLOOP_TEXTURE = BigAircraft.id("aviation_display/balloon_floop");
    private static final ResourceLocation RISING_TEXTURE = BigAircraft.id("aviation_display/balloon_rising");
    private static final ResourceLocation FALLING_TEXTURE = BigAircraft.id("aviation_display/balloon_falling");

    private static final int MARGIN = 1;
    private static final int TEXTURE_SIZE = 18;

    private double lastLift = 0;

    public BalloonHeaterFlightHudAugment(BlockEntityEntry<T> blockEntityType) {
        super(blockEntityType);
    }

    @Override
    public Set<FlightHudAugmentType<?>> getExclusives() {
        return Sets.newHashSet(ModFlightHudAugments.HOT_AIR_BURNER.get(), ModFlightHudAugments.STEAM_VENT.get());
    }

    @Override
    public void render(T blockEntity, GuiGraphics graphics, Minecraft mc, ClientLevel level, ClientSubLevel beSubLevel, BlockPos blockPos, LocalPlayer player, float partialTick) {
        ClientBalloon balloon = (ClientBalloon) blockEntity.getBalloon();

        if (!renderNoBalloon(graphics, balloon)) {
            double lift = getCumulativeLift(level, balloon);
            this.lastLift = Mth.lerp(partialTick, this.lastLift, lift); // this seems cursed

            this.renderLift(graphics, lift);
        }
    }

    private static Vector2i position(GuiGraphics graphics) {
        return new Vector2i(graphics.guiWidth() - TEXTURE_SIZE - MARGIN, graphics.guiHeight() / 2 - TEXTURE_SIZE / 2);
    }

    private static boolean renderNoBalloon(GuiGraphics graphics, ClientBalloon balloon) {
        if (balloon == null || !balloon.isValid()) {
            Vector2i pos = position(graphics);
            graphics.blitSprite(FLOOP_TEXTURE, pos.x, pos.y, TEXTURE_SIZE, TEXTURE_SIZE);

            return true;
        }

        return false;
    }

    private void renderLift(GuiGraphics graphics, double lift) {
        ResourceLocation sprite = TEXTURE;

        if (lift - this.lastLift < -Mth.EPSILON)
            sprite = FALLING_TEXTURE;
        else if (lift - this.lastLift > Mth.EPSILON)
            sprite = RISING_TEXTURE;

        Vector2i pos = position(graphics);
        graphics.blitSprite(sprite, pos.x, pos.y, TEXTURE_SIZE, TEXTURE_SIZE);

        FlightHudNumberRenderer numberRenderer = new FlightHudNumberRenderer(graphics, MonoNumberFont.BIG_BLOCK, BigAircraftRenderTypes.GUI_TEXTURED);

        numberRenderer.alignTo(Alignment.RIGHT);
        numberRenderer.drawDouble(lift, pos.x - 1, pos.y + MonoNumberFont.BIG_BLOCK.textureHeight() / 2 + MonoNumberFont.BIG_BLOCK.spacing() * 2);
    }

    private static double getCumulativeLift(ClientLevel level, ClientBalloon balloon) {
        double lift = 0.0;

        for (BlockEntityLiftingGasProvider heater : balloon.getHeaters()) {
            BlockEntityLiftingGasProvider.ClientBalloonInfo info = getClientBalloonInfo(heater);

            if (info == null)
                continue;

            double d = info.clientBalloonLift() * heater.getAirPressure(info, level);

            if (info.clientBalloonFilled() > 0.01)
                d *= heater.getClientPredictedVolume() / info.clientBalloonFilled();

            lift += d;
        }

        return lift;
    }

    @Nullable
    private static BlockEntityLiftingGasProvider.ClientBalloonInfo getClientBalloonInfo(BlockEntityLiftingGasProvider heater) {
        if (heater instanceof HotAirBurnerBlockEntityAccessor accessor)
            return accessor.getClientBalloonInfo();

        if (heater instanceof SteamVentBlockEntityAccessor accessor)
            return accessor.getClientBalloonInfo();

        return null;
    }

}
