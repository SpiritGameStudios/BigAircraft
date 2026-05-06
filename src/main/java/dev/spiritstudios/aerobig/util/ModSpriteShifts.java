package dev.spiritstudios.aerobig.util;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import dev.spiritstudios.aerobig.BigAircraft;

/**
 * TODO: apply to all instances of the carbon composite sprites
 */
public interface ModSpriteShifts {

    CTSpriteShiftEntry CARBON_COMPOSITE = omni("carbon_composite");

    static CTSpriteShiftEntry omni(String path) {
        return CTSpriteShifter.getCT(AllCTTypes.OMNIDIRECTIONAL, BigAircraft.id("block/" + path), BigAircraft.id("block/" + path + "_connected"));
    }

}
