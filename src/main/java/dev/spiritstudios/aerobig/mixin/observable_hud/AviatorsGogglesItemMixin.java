package dev.spiritstudios.aerobig.mixin.observable_hud;

import com.simibubi.create.content.equipment.armor.BaseArmorItem;
import dev.eriksonn.aeronautics.content.items.AviatorsGogglesItem;
import dev.spiritstudios.aerobig.aviation_display.AviationDisplayType;
import dev.spiritstudios.aerobig.component.AviationDisplay;
import dev.spiritstudios.aerobig.component.AviationDisplaysComponent;
import dev.spiritstudios.aerobig.flight_hud.ObservableHud;
import dev.spiritstudios.aerobig.registry.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AviatorsGogglesItem.class)
public class AviatorsGogglesItemMixin extends BaseArmorItem {

    public AviatorsGogglesItemMixin(Holder<ArmorMaterial> armorMaterial, Type type, Properties properties, ResourceLocation textureLoc) {
        super(armorMaterial, type, properties, textureLoc);
    }

    @Override
    @NotNull
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockEntity blockEntity = level.getBlockEntity(clickedPos);

        if (player != null && blockEntity instanceof ObservableHud observableHud) {
            AviationDisplayType aviationDisplayType = AviationDisplayType.BY_BLOCK_ENTITY.get(blockEntity.getType());

            if (aviationDisplayType == null)
                return InteractionResult.PASS;

            ItemStack itemInHand = context.getItemInHand();

            AviationDisplaysComponent component = itemInHand.getOrDefault(
                    ModDataComponents.AVIATION_DISPLAYS,
                    AviationDisplaysComponent.DEFAULT
            );

            itemInHand.applyComponents(DataComponentMap.builder()
                    .set(ModDataComponents.AVIATION_DISPLAYS, component.with(new AviationDisplay(
                            aviationDisplayType,
                            new GlobalPos(
                                    level.dimension(),
                                    clickedPos
                            )
                    )))
                    .build()
            );

            observableHud.bigAircraft$addObserver(player.getUUID());

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

}
