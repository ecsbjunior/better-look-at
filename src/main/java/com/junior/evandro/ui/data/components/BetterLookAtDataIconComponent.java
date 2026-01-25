package com.junior.evandro.ui.data.components;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;

import javax.annotation.Nonnull;

public class BetterLookAtDataIconComponent extends BetterLookAtDataComponent {
    private static final String DATA_ICON_PATH =
        BetterLookAtDataComponent.BASE_PATH + "BetterLookAtDataIcon.ui";

    private static final String DATA_ICON_ID = "#DataIcon";
    private static final String DATA_ICON_CONTAINER_ID = "#DataIconContainer";

    @Nonnull
    private final UICommandBuilder uiCommandBuilder;

    public BetterLookAtDataIconComponent(@Nonnull UICommandBuilder uiCommandBuilder) {
        this.uiCommandBuilder = uiCommandBuilder;
    }

    public void append(String parent) {
        this.uiCommandBuilder.append(parent, BetterLookAtDataIconComponent.DATA_ICON_PATH);
    }

    public void render(String icon) {
        this.uiCommandBuilder.set(BetterLookAtDataIconComponent.getDataIcon(), icon);
    }

    private static String getDataIcon() {
        return String.format("%s.ItemId", BetterLookAtDataIconComponent.DATA_ICON_ID);
    }
}
