package com.junior.evandro.ui;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public abstract class BetterLookAtHudManager {
    protected Map<PlayerRef, BetterLookAtCustomHud> customHuds = new HashMap<>();

    public abstract void show(@Nonnull Player player, @Nonnull PlayerRef playerRef, Object object);
    public abstract void hidden(@Nonnull Player player, @Nonnull PlayerRef playerRef);
}
