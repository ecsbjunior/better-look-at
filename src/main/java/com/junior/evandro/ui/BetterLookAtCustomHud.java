package com.junior.evandro.ui;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public abstract class BetterLookAtCustomHud extends CustomUIHud {
    public BetterLookAtCustomHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    public abstract void render(Object object);
    public abstract void hidden();
}
