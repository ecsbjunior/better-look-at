package com.junior.evandro.ui;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class BetterLookAtVanillaHudManager extends BetterLookAtHudManager {
    @Override
    public void showCustomHud(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull BetterLookAtCustomHud hud) {
        player.getHudManager().setCustomHud(playerRef, hud);
    }

    @Override
    public void hideCustomHud(@NonNullDecl Player player, @NonNullDecl PlayerRef playerRef) {
        this.showCustomHud(player, playerRef, new BetterLookAtHiddenHud(playerRef));
    }
}
