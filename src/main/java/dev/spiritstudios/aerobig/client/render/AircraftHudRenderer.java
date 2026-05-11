package dev.spiritstudios.aerobig.client.render;

import dev.eriksonn.aeronautics.index.AeroItems;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.spiritstudios.aerobig.BigAircraft;
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
 *   - @CallMeEcho maybe design a ui in paint first?
 */
public class AircraftHudRenderer {

    private static final Vector3d DOWN_NORMAL = JOMLConversion.atLowerCornerOf(Direction.DOWN.getNormal());
    private static final Pose3dc IDENTITY_POSE = new Pose3d(new Vector3d(), new Quaterniond(), new Vector3d(), new Vector3d(1.0));

    @SubscribeEvent
    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(BigAircraft.id("aircraft_hud"), (graphics, deltaTracker) -> {
            Minecraft minecraft = Minecraft.getInstance();

            LocalPlayer player = minecraft.player;
            SubLevel subLevel = Sable.HELPER.getTrackingOrVehicleSubLevel(player);

            if (shouldRender(minecraft, player) && subLevel != null) {
                Pose3dc pose = subLevel.logicalPose();
                Pose3dc prevPose = subLevel.lastPose();

                render(graphics, minecraft, player, pose, prevPose);
            }
        });
    }

    private static void render(GuiGraphics graphics, Minecraft minecraft, LocalPlayer player, Pose3dc pose, Pose3dc prevPose) {
        Vector3d downNormal = JOMLConversion.atLowerCornerOf(Direction.DOWN.getNormal());
        pose.orientation().transformInverse(downNormal);

        int windowWidth = graphics.guiWidth();
        int windowHeight = graphics.guiHeight();

        float roll = getRadians(downNormal.y(), downNormal.z());
        float pitch = getRadians(downNormal.y(), downNormal.x());

        int centreX = windowWidth / 2;
        int centreY = windowHeight / 2;

        AeroGraphics aeroGraphics = new AeroGraphics(graphics);

        aeroGraphics.writeAirspeed(minecraft, centreY, pose, prevPose);
        aeroGraphics.writeAltitude(minecraft, windowWidth, windowHeight, player.position());

        aeroGraphics.drawAttitude(pitch, roll, windowHeight, centreX, centreY);
    }

    private static float getRadians(double y, double a) {
        return a * a > Mth.EPSILON ? (float) Mth.atan2(a, -y) : 0.0F;
    }

    private static boolean shouldRender(Minecraft minecraft, @Nullable Player player) {
        return player != null && player.getItemBySlot(EquipmentSlot.HEAD).is(AeroItems.AVIATORS_GOGGLES) && !minecraft.options.hideGui;
    }

}
