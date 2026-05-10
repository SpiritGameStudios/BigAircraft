package dev.spiritstudios.aerobig.client.render;

import com.mojang.math.Axis;
import dev.eriksonn.aeronautics.index.AeroItems;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import static java.lang.Math.atan2;

public class AircraftHudRenderer {
    private static final Pose3dc IDENTITY_POSE = new Pose3d(new Vector3d(), new Quaterniond(), new Vector3d(), new Vector3d(1.0));

    private static final double BPT_TO_KT = 18000.0 / 463.0;

    @SubscribeEvent
    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(BigAircraft.id("aircraft_hud"), (graphics, deltaTracker) -> {
            var mc = Minecraft.getInstance();
            var level = mc.level;
            var player = mc.player;
            var aeroGraphics = new AeroGraphics(graphics);
            SubLevel subLevel = Sable.HELPER.getTrackingOrVehicleSubLevel(player);

            assert level != null;
            assert player != null;

            if (!player.getInventory().getArmor(3).is(AeroItems.AVIATORS_GOGGLES)) return;

            int windowW = graphics.guiWidth();
            int windowH = graphics.guiHeight();
            int centreX = windowW / 2;
            int centreY = windowH / 2;

            final int elementHeight = 118;
            final int rightOffset = windowW - windowW / 4;
            final int topOffset = windowH / 2 - elementHeight / 2;

            var altitude = Sable.HELPER.projectOutOfSubLevel(level, player.position()).y;

            graphics.drawString(mc.font, "G" + (int) (altitude * 100), rightOffset, topOffset, CommonColors.WHITE);

            Pose3dc pose = subLevel != null ? subLevel.logicalPose() : IDENTITY_POSE;
            Pose3dc prevPose = subLevel != null ? subLevel.lastPose() : IDENTITY_POSE;

            Vector3d down = JOMLConversion.toJOML(Vec3.atLowerCornerOf(Direction.DOWN.getNormal()));
            pose.orientation().transformInverse(down);

            var roll = down.y() < 0 || down.z() * down.z() > Mth.EPSILON ? atan2(down.z(), -down.y()) : 0;
            var pitch = down.y() < 0 || down.x() * down.x() > Mth.EPSILON ? atan2(down.x(), -down.y()) : 0;

            graphics.drawString(mc.font, "R" + (int) Math.toDegrees(roll), centreX, centreY + 10, CommonColors.WHITE);

            graphics.pose().pushPose();
            graphics.pose().translate(centreX, centreY, 0);
            graphics.pose().mulPose(Axis.ZN.rotation((float) roll));
            graphics.pose().translate(-centreX, -centreY, 0);

            final int attStep = 16;
            float up = (float)-pitch / Mth.PI;
//            up -= Mth.HALF_PI;
            up *= windowH;

            up += centreY;

            aeroGraphics.fill(
                    20, up,
                    windowW - 20, up + 1
            );

            graphics.pose().popPose();

            double airSpeedBPT = pose.position().distance(prevPose.position());
            double airSpeedKnots = airSpeedBPT * BPT_TO_KT;

            graphics.drawString(mc.font, "" + (int) airSpeedKnots, 0, centreY + 10, CommonColors.WHITE);
        });
    }
}
