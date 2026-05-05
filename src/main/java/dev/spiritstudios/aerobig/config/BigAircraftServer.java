package dev.spiritstudios.aerobig.config;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class BigAircraftServer extends ConfigBase {

    private static final String KINETICS = "Parameters and abilities of Aerospace's kinetic mechanisms";

    public final BigAircraftKinetics kinetics = this.nested(0, BigAircraftKinetics::new, KINETICS);

    @Override
    @NotNull
    public String getName() {
        return "server";
    }

}
