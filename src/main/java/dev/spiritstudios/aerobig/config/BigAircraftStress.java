package dev.spiritstudios.aerobig.config;

import com.simibubi.create.infrastructure.config.CStress;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.spiritstudios.aerobig.BigAircraft;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BigAircraftStress extends CStress {

    private static final String STRESS_UNITS = "[in Stress Units]";
    private static final String IMPACT = "Configure the individual stress impact of mechanical blocks. Note that this cost is doubled for every speed increase it receives.";
    private static final String CAPACITY = "Configure how much stress a source can accommodate for.";

    private static final Object2DoubleMap<ResourceLocation> DEFAULT_IMPACTS = new Object2DoubleOpenHashMap<>();
    private static final Object2DoubleMap<ResourceLocation> DEFAULT_CAPACITIES = new Object2DoubleOpenHashMap<>();

    @Override
    public void registerAll(final ModConfigSpec.Builder builder) {
        builder.comment(new String[]{".", STRESS_UNITS, IMPACT}).push("impact");
        DEFAULT_IMPACTS.forEach((id, value) -> this.impacts.put(id, builder.define(id.getPath(), value)));
        builder.pop();

        builder.comment(new String[]{".", STRESS_UNITS, CAPACITY}).push("capacity");
        DEFAULT_CAPACITIES.forEach((id, value) -> this.capacities.put(id, builder.define(id.getPath(), value)));
        builder.pop();
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setNoImpact() {
        return setImpact(0.0F);
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setImpact(final double value) {
        return (builder) -> {
            assertFromMod(builder);
            DEFAULT_IMPACTS.put(BigAircraft.id(builder.getName()), value);
            return builder;
        };
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setCapacity(final double value) {
        return (builder) -> {
            assertFromMod(builder);
            DEFAULT_CAPACITIES.put(BigAircraft.id(builder.getName()), value);
            return builder;
        };
    }

    private static void assertFromMod(final BlockBuilder<?, ?> builder) {
        if (!builder.getOwner().getModid().equals(BigAircraft.MOD_ID))
            throw new IllegalStateException("Non-%1$s blocks cannot be added to %1$s's config.".formatted(BigAircraft.MOD_NAME));
    }

}
