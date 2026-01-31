package com.junior.evandro.ui;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class BetterLookAtHiddenHud extends BetterLookAtCustomHud{
    public BetterLookAtHiddenHud(@NonNullDecl PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    public void render(Object object) {

    }

    @Override
    public void hidden() {

    }

    @Override
    protected void build(@NonNullDecl UICommandBuilder uiCommandBuilder) {

    }
}
