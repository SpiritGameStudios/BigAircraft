package dev.spiritstudios.aerobig.mixin;

import dev.simulated_team.simulated.registrate.simulated_tab.CreativeTabItemTransforms;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;

@Mixin(CreativeTabItemTransforms.class)
public interface CreativeTabItemTransformsAccessor {

    @Accessor("ITEM_VISIBILITY")
    static HashMap<Item, CreativeTabItemTransforms.VisibilityType> getItemVisibilities() {
        throw new AssertionError();
    }

}
