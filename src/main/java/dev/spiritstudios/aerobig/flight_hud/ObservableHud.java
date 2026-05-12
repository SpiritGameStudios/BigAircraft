package dev.spiritstudios.aerobig.flight_hud;

import java.util.List;
import java.util.UUID;

public interface ObservableHud {
    String NBT_KEY = "HudObservers";

    List<UUID> bigAircraft$getObservers();

    void bigAircraft$addObserver(UUID uuid);
}
