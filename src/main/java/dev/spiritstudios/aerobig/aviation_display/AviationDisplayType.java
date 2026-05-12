package dev.spiritstudios.aerobig.aviation_display;

import com.simibubi.create.api.registry.SimpleRegistry;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class AviationDisplayType {
    public static final SimpleRegistry<BlockEntityType<?>, AviationDisplayType> BY_BLOCK_ENTITY = SimpleRegistry.create();

    public AviationDisplayType(BlockEntityEntry<?> blockEntityType) {
        BY_BLOCK_ENTITY.register(blockEntityType.get(), this);
    }

    public abstract void render(
            GuiGraphics graphics,
            Minecraft mc,
            ClientLevel level,
            ClientSubLevel subLevel,
            BlockPos blockPos,
            LocalPlayer player,
            float partialTick
    );
}
