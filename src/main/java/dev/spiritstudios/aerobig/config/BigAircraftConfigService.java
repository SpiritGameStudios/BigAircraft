package dev.spiritstudios.aerobig.config;

import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.infrastructure.config.CStress;
import net.createmod.catnip.config.ConfigBase;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public enum BigAircraftConfigService {

    INSTANCE;

    public static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

    private static BigAircraftServer server;

    public boolean serverLoaded() {
        return server != null && server.specification != null && server.specification.isLoaded();
    }

    public BigAircraftServer server() {
        return server;
    }

    public static ConfigBase byType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }

    private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);

            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();

        CONFIGS.put(side, config);

        return config;
    }

    public static void register(ModLoadingContext context, ModContainer container) {
        server = register(BigAircraftServer::new, ModConfig.Type.SERVER);

        for (final Map.Entry<ModConfig.Type, ConfigBase> typeConfigBaseEntry : CONFIGS.entrySet())
            container.registerConfig(typeConfigBaseEntry.getKey(), typeConfigBaseEntry.getValue().specification);

        CStress stress = INSTANCE.server().kinetics.stressValues;

        BlockStressValues.IMPACTS.registerProvider(stress::getImpact);
        BlockStressValues.CAPACITIES.registerProvider(stress::getCapacity);
    }

}
