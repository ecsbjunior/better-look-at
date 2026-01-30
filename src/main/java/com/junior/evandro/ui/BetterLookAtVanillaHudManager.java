package com.junior.evandro.ui;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.junior.evandro.ui.data.BetterLookAtHud;

import javax.annotation.Nonnull;

public class BetterLookAtVanillaHudManager extends BetterLookAtHudManager {
    @Override
    public void show(@Nonnull Player player, @Nonnull PlayerRef playerRef, Object object) {
        var hud = this.customHuds.get(playerRef);

        if (hud == null) {
            hud = new BetterLookAtHud(playerRef);
            this.customHuds.put(playerRef, hud);
            player.getHudManager().setCustomHud(playerRef, hud);
        }

        hud.render(object);
    }

    @Override
    public void hidden(@Nonnull Player player, @Nonnull PlayerRef playerRef) {
        var hud = this.customHuds.get(playerRef);

        if (hud == null) {
            return;
        }

        hud.hidden();
    }
}
