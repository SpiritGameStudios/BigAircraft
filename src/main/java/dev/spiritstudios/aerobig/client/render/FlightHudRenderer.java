package dev.spiritstudios.aerobig.client.render;

import dev.ryanhcode.sable.Sable;
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
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.function.Consumer;
import java.util.function.Function;

public class FlightHudRenderer {

    @SubscribeEvent
    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, BigAircraft.id("aviation_hud"), (graphics, deltaTracker) -> {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;

            if (!shouldRender(minecraft, player))
                return;

            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            FlightHudAugmentsComponent component = helmet.get(ModDataComponents.FLIGHT_HUD_AUGMENTS);

            if (component == null)
                return;

            for (FlightHudAugment augment : component.augments()) {
                GlobalPos target = augment.target();
                ClientSubLevel subLevel = Sable.HELPER.getContainingClient(target.pos());

                if (subLevel == null || !augment.isIn(subLevel))
                    continue;

                renderHudAugments(
                    augment.type(),
                    graphics,
                    minecraft,
                    player,
                    subLevel,
                    target,
                    deltaTracker.getGameTimeDeltaPartialTick(false)
                );
            }
        });
    }

    private static <T extends BlockEntity> void renderHudAugments(FlightHudAugmentType<T> augmentType, GuiGraphics graphics, Minecraft minecraft, LocalPlayer player, ClientSubLevel subLevel, GlobalPos globalPos, float partialTick) {
        BlockPos pos = globalPos.pos();
        BlockEntityType<T> blockEntityType = augmentType.blockEntityType;

        player.clientLevel.getBlockEntity(pos, blockEntityType).ifPresent(blockEntity -> {
            graphics.pose().pushPose();
            augmentType.render(
                blockEntity,
                graphics,
                minecraft,
                player.clientLevel,
                subLevel,
                pos,
                player,
                partialTick
            );
            graphics.pose().popPose();
        });
    }

    private static boolean shouldRender(Minecraft minecraft, @Nullable LocalPlayer player) {
        if (player == null || !player.getItemBySlot(EquipmentSlot.HEAD).has(ModDataComponents.FLIGHT_HUD_AUGMENTS))
            return false;

        return minecraft.gameMode != null && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR;
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

    public static void scissor(GuiGraphics graphics, int marginX, int marginY, Consumer<GuiGraphics> renderAction) {
        graphics.enableScissor(marginX, marginY, graphics.guiWidth() - marginX, graphics.guiHeight() - marginY);
        renderAction.accept(graphics);
        graphics.disableScissor();
    }

}
