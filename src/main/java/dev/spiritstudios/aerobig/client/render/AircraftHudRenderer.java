package dev.spiritstudios.aerobig.client.render;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import dev.spiritstudios.aerobig.component.AviationDisplay;
import dev.spiritstudios.aerobig.component.AviationDisplaysComponent;
import dev.spiritstudios.aerobig.registry.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
            LocalPlayer player = minecraft.player;

            ClientSubLevel subLevel = (ClientSubLevel) Sable.HELPER.getTrackingOrVehicleSubLevel(player);

            if (!shouldRender(minecraft, player) || subLevel == null)
                return;

            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            AviationDisplaysComponent component = helmet.get(ModDataComponents.AVIATION_DISPLAYS);

            if (component == null)
                return;

            for (AviationDisplay display : component.displays()) {
                if (!display.isIn(subLevel))
                    continue;

                renderDisplayType(
                    display.type(),
                    graphics,
                    minecraft,
                    player,
                    subLevel,
                    display.pos(),
                    deltaTracker.getGameTimeDeltaPartialTick(false)
                );
            }
        });
    }

    private static <T extends BlockEntity> void renderDisplayType(AviationDisplayType<T> displayType, GuiGraphics graphics, Minecraft minecraft, LocalPlayer player, ClientSubLevel subLevel, GlobalPos globalPos, float partialTick) {
        BlockPos pos = globalPos.pos();
        BlockEntityType<T> blockEntityType = displayType.blockEntityType;

        player.clientLevel.getBlockEntity(pos, blockEntityType).ifPresent(blockEntity -> displayType.render(
            blockEntity,
            graphics,
            minecraft,
            player.clientLevel,
            subLevel,
            pos,
            player,
            partialTick
        ));
    }

    private static boolean shouldRender(Minecraft minecraft, @Nullable Player player) {
        return player != null && player.getItemBySlot(EquipmentSlot.HEAD).has(ModDataComponents.AVIATION_DISPLAYS) && !minecraft.options.hideGui;
    }
}
