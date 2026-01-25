package com.junior.evandro.ui.data.components;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;

import javax.annotation.Nonnull;

public class BetterLookAtDataHealthComponent extends BetterLookAtDataComponent {
    private static final String DATA_HEALTH_PATH =
        BetterLookAtDataComponent.BASE_PATH + "BetterLookAtDataHealth.ui";

    private static final String DATA_HEALTH_ID = "#DataHealthContainer";
    private static final String DATA_HEALTH_PROGRESS_BAR_ID = "#DataHealthProgressBar";
    private static final String DATA_HEALTH_PROGRESS_BAR_TEXTURE_ID = "#DataHealthProgressBarTexture";

    @Nonnull
    private final UICommandBuilder uiCommandBuilder;

    public BetterLookAtDataHealthComponent(@Nonnull UICommandBuilder uiCommandBuilder) {
        this.uiCommandBuilder = uiCommandBuilder;
    }

    public void append(String parent) {
        this.uiCommandBuilder.append(parent, BetterLookAtDataHealthComponent.DATA_HEALTH_PATH);
    }

    public void render(float health, int maxHealth) {
        this.setHealthProgressBar(health, maxHealth);
        this.setHealthProgressBarTexture(health, maxHealth);
    }

    private void setHealthProgressBar(float health, int maxHealth) {
        this.uiCommandBuilder.set(this.getDataHealthProgressBar(), health / maxHealth);
    }

    private void setHealthProgressBarTexture(float health, int maxHealth) {
        this.uiCommandBuilder.set(this.getDataHealthProgressBarTexture(), health / maxHealth);
    }

    private String getDataHealthProgressBar() {
        return String.format("%s.Value", BetterLookAtDataHealthComponent.DATA_HEALTH_PROGRESS_BAR_ID);
    }

    private String getDataHealthProgressBarTexture() {
        return String.format("%s.Value", BetterLookAtDataHealthComponent.DATA_HEALTH_PROGRESS_BAR_TEXTURE_ID);
    }
}
