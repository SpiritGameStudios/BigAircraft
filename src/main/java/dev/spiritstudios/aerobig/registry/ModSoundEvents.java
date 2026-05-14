package dev.spiritstudios.aerobig.registry;

import dev.spiritstudios.aerobig.BigAircraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSoundEvents {

    private static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(
            BuiltInRegistries.SOUND_EVENT,
            BigAircraft.MOD_ID
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> TERRAIN_TERRAIN_PULL_UP = REGISTER.register(
            "terrainterrainpullup",
            () -> SoundEvent.createFixedRangeEvent(BigAircraft.id("terrainterrainpullup"), 16)
    );

    public static void init(IEventBus modBus) {
        REGISTER.register(modBus);
    }
}
