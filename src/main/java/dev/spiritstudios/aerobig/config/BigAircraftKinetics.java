package dev.spiritstudios.aerobig.config;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class BigAircraftKinetics extends ConfigBase {

    private static final String STRESS = "Fine tune the kinetic stats of individual components";

    public final BigAircraftStress stressValues = this.nested(1, BigAircraftStress::new, STRESS);

    @Override
    @NotNull
    public String getName() {
        return "kinetics";
    }

}
