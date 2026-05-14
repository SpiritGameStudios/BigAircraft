package dev.spiritstudios.aerobig.client.render;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.spiritstudios.aerobig.BigAircraft;
import dev.spiritstudios.aerobig.flight_hud.FlightHudAugmentType;
import dev.spiritstudios.aerobig.component.FlightHudAugment;
import dev.spiritstudios.aerobig.component.FlightHudAugmentsComponent;
import dev.spiritstudios.aerobig.registry.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.function.Function;

/**
 * <h1>Plans</h1>
 * - allow players to click on certain components (gimbal sensor, altitude sensor, velocity sensor, hot air burner (?)) to add information to their hud. could be the same overlay as normal goggle tooltips or a cleaner, more minimalistic flight interface<br>
 * - @CallMeEcho maybe design a ui in paint first?
 */
public class FlightHudRenderer {

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
            FlightHudAugmentsComponent component = helmet.get(ModDataComponents.FLIGHT_HUD_AUGMENTS);

            if (component == null)
                return;

            for (FlightHudAugment augment : component.augments()) {
                if (!augment.isIn(subLevel))
                    continue;

                renderHudAugments(
                    augment.type(),
                    graphics,
                    minecraft,
                    player,
                    subLevel,
                    augment.target(),
                    deltaTracker.getGameTimeDeltaPartialTick(false)
                );
            }
        });
    }

    private static <T extends BlockEntity> void renderHudAugments(FlightHudAugmentType<T> augmentType, GuiGraphics graphics, Minecraft minecraft, LocalPlayer player, ClientSubLevel subLevel, GlobalPos globalPos, float partialTick) {
        BlockPos pos = globalPos.pos();
        BlockEntityType<T> blockEntityType = augmentType.blockEntityType;

        player.clientLevel.getBlockEntity(pos, blockEntityType).ifPresent(blockEntity -> augmentType.render(
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
        return player != null && player.getItemBySlot(EquipmentSlot.HEAD).has(ModDataComponents.FLIGHT_HUD_AUGMENTS) && !minecraft.options.hideGui && minecraft.screen == null;
    }

    public static void renderSprite(GuiGraphics graphics, ResourceLocation id, float x, float y, float uWidth, float vHeight, float uPos, float vPos, int textureWidth, int textureHeight, Function<ResourceLocation, RenderType> renderType) {
        renderSprite(
            graphics,
            id,
            x,
            x + uWidth,
            y,
            y + vHeight,
            uPos / textureWidth,
            (uPos + uWidth) / textureWidth,
            vPos / textureHeight,
            (vPos + vHeight) / textureHeight,
            renderType
        );
    }

    public static void renderSprite(GuiGraphics graphics, ResourceLocation id, float minX, float maxX, float minY, float maxY, float minU, float maxU, float minV, float maxV, Function<ResourceLocation, RenderType> renderType) {
        Matrix4f pose = graphics.pose().last().pose();
        graphics.bufferSource().getBuffer(renderType.apply(id))
            .addVertex(pose, minX, minY, 0).setUv(minU, minV)
            .addVertex(pose, minX, maxY, 0).setUv(minU, maxV)
            .addVertex(pose, maxX, maxY, 0).setUv(maxU, maxV)
            .addVertex(pose, maxX, minY, 0).setUv(maxU, minV);
    }

}
