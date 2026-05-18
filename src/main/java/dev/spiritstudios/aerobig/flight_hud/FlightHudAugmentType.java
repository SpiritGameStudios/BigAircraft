package dev.spiritstudios.aerobig.flight_hud;

import com.simibubi.create.api.registry.SimpleRegistry;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.compress.utils.Sets;

import java.util.Set;

public abstract class FlightHudAugmentType<T extends BlockEntity> {

    public static final SimpleRegistry<BlockEntityType<?>, FlightHudAugmentType<?>> BY_BLOCK_ENTITY = SimpleRegistry.create();
    public final BlockEntityType<T> blockEntityType;

    public FlightHudAugmentType(BlockEntityEntry<T> blockEntityType) {
        this.blockEntityType = blockEntityType.get();
        BY_BLOCK_ENTITY.register(this.blockEntityType, this);
    }

    public Set<FlightHudAugmentType<?>> getExclusives() {
        return Sets.newHashSet();
    }

    public abstract void render(
        T blockEntity,
        GuiGraphics graphics,
        Minecraft mc,
        ClientLevel level,
        ClientSubLevel subLevel,
        BlockPos blockPos,
        LocalPlayer player,
        float partialTick
    );

}
