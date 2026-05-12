package dev.spiritstudios.aerobig.mixin.observable_hud;

import com.simibubi.create.foundation.blockEntity.CachedRenderBBBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dev.spiritstudios.aerobig.component.AviationDisplaysComponent;
import dev.spiritstudios.aerobig.flight_hud.ObservableHud;
import dev.spiritstudios.aerobig.registry.ModDataComponents;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(SmartBlockEntity.class)
public class SmartBlockEntityMixin extends CachedRenderBBBlockEntity implements ObservableHud {

    @Unique private List<UUID> bigAircraft$observers = new ArrayList<>(List.of());

    public SmartBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "read", at = @At("TAIL"))
    private void test(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        this.bigAircraft$observers = NBTHelper.readCompoundList(tag.getList(NBT_KEY, Tag.TAG_INT_ARRAY), compoundTag -> UUIDUtil.uuidFromIntArray(compoundTag.getIntArray("Id")));
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void test2(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        tag.put(NBT_KEY, NBTHelper.writeCompoundList(this.bigAircraft$observers, uuid -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putIntArray("Id", UUIDUtil.uuidToIntArray(uuid));

            return compoundTag;
        }));
    }

    @Override
    public List<UUID> bigAircraft$getObservers() {
        return this.bigAircraft$observers;
    }

    @Override
    public void bigAircraft$addObserver(UUID uuid) {
        this.bigAircraft$observers.add(uuid);

        this.setChanged();
        this.sendData();
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void test(CallbackInfo ci) {
        for (UUID uuid : this.bigAircraft$observers) {
            assert this.level != null;
            Player player = this.level.getPlayerByUUID(uuid);

            if (player != null)
                this.bigAircraft$removeAssociatedInventoryData(this.level, player);
        }

        this.bigAircraft$observers.clear();

        this.setChanged();
        this.sendData();
    }

    @Unique
    private void bigAircraft$removeAssociatedInventoryData(Level level, Player player) {
        for (ItemStack itemStack : player.getInventory().items) {
            AviationDisplaysComponent component = itemStack.get(ModDataComponents.AVIATION_DISPLAYS);

            if (component == null || component.displays().isEmpty())
                continue;

            itemStack.applyComponents(DataComponentMap.builder()
                .set(ModDataComponents.AVIATION_DISPLAYS, component.removePosition(
                    GlobalPos.of(level.dimension(), this.getBlockPos())
                ))
                .build()
            );
        }
    }

}
