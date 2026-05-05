package dev.spiritstudios.aerobig.item;

import com.simibubi.create.content.kinetics.gearbox.VerticalGearboxItem;
import dev.spiritstudios.aerobig.registry.ModBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VerticalCarbonCompositeGearboxItem extends VerticalGearboxItem {

    private final DyeColor color;

    public VerticalCarbonCompositeGearboxItem(Properties builder, DyeColor color) {
        super(builder);
        this.color = color;
    }

    @Override
    public Block getBlock() {
        return ModBlocks.CARBON_COMPOSITE_GEARBOXES.get(this.color).get();
    }

    @Override
    public String getDescriptionId() {
        return Util.makeDescriptionId("item", BuiltInRegistries.BLOCK.getKey(this.getBlock()).withPath(s -> "vertical_" + s));
    }

}
