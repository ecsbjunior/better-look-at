package com.junior.evandro.ui;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.ui.data.BetterLookAtHud;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public abstract class BetterLookAtHudManager {
    protected Map<PlayerRef, BetterLookAtCustomHud> customHuds = new HashMap<>();

    public void show(@Nonnull Player player, @Nonnull PlayerRef playerRef, Object object) {
        try {
            var hud = this.customHuds.get(playerRef);

            if (hud == null) {
                hud = new BetterLookAtHud(playerRef);
                this.customHuds.put(playerRef, hud);
                this.showCustomHud(player, playerRef, hud);
            }

            hud.render(object);
        } catch (Exception exception) {
            this.hide(player, playerRef);
            BetterLookAt.LOGGER.atWarning().log("An exception occur when call BetterLookAtHudManager.show method: %s", exception.getMessage());
        }
    }

    public void hide(@Nonnull Player player, @Nonnull PlayerRef playerRef) {
        try {
            var hud = this.customHuds.get(playerRef);

            if (hud == null) {
                return;
            }

            hud.hidden();
            this.customHuds.remove(playerRef);
            this.hideCustomHud(player, playerRef);
        } catch (Exception exception) {
            BetterLookAt.LOGGER.atWarning().log("An exception occur when call BetterLookAtHudManager.hide method: %s", exception.getMessage());
        }
    }

    public abstract void showCustomHud(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull BetterLookAtCustomHud hud);
    public abstract void hideCustomHud(@Nonnull Player player, @Nonnull PlayerRef playerRef);

    public static BetterLookAtHudManager getInstance() {
        BetterLookAtHudManager hudManager = new BetterLookAtVanillaHudManager();

        if (BetterLookAtMultipleHudManager.MultipleHud.isAvailable()) {
            hudManager = new BetterLookAtMultipleHudManager();
        }

        BetterLookAt.LOGGER.atInfo().log("The HUD MANAGER has been configured with %s", hudManager.getClass().toString());

        return hudManager;
    }
}
