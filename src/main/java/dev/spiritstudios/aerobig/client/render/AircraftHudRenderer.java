package dev.spiritstudios.aerobig.client.render;

import dev.eriksonn.aeronautics.index.AeroItems;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.component.AviationDisplay;
import dev.spiritstudios.aerobig.registry.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Vector3d;

/**
 * <h1>Plans</h1>
 * - allow players to click on certain components (gimbal sensor, altitude sensor, velocity sensor, hot air burner (?)) to add information to their hud. could be the same overlay as normal goggle tooltips or a cleaner, more minimalistic flight interface<br>
 * - @CallMeEcho maybe design a ui in paint first?
 */
public class AircraftHudRenderer {

    private static final Vector3d DOWN_NORMAL = JOMLConversion.atLowerCornerOf(Direction.DOWN.getNormal());
    private static final Pose3dc IDENTITY_POSE = new Pose3d(new Vector3d(), new Quaterniond(), new Vector3d(), new Vector3d(1.0));

    @SubscribeEvent
    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(BigAircraft.id("aviation_hud"), (graphics, deltaTracker) -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level == null) return;

            LocalPlayer player = minecraft.player;
            ClientSubLevel subLevel = (ClientSubLevel) Sable.HELPER.getTrackingOrVehicleSubLevel(player);

            if (shouldRender(minecraft, player) & subLevel != null) {
                var helmet = player.getItemBySlot(EquipmentSlot.HEAD);
                var displays = helmet.get(ModDataComponents.AVIATION_DISPLAYS);
                if (displays != null) {
                    for (AviationDisplay display : displays.displays()) {
                        if (display.pos().dimension() != subLevel.getLevel().dimension()) continue;
                        if (!subLevel.getPlot().contains(display.pos().pos().getCenter())) continue;

                        display.type().render(
                                graphics,
                                minecraft,
                                player.clientLevel,
                                subLevel,
                                display.pos().pos(),
                                player,
                                deltaTracker.getGameTimeDeltaPartialTick(false)
                        );
                    }
                }
            }
        });
    }

    private static boolean shouldRender(Minecraft minecraft, @Nullable Player player) {
        return player != null && player.getItemBySlot(EquipmentSlot.HEAD).is(AeroItems.AVIATORS_GOGGLES) && !minecraft.options.hideGui;
    }
}
