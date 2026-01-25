package com.junior.evandro.ui.data;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.junior.evandro.ui.BetterLookAtHiddenHud;
import com.junior.evandro.ui.data.components.BetterLookAtDataContentComponent;
import com.junior.evandro.ui.data.components.BetterLookAtDataIconComponent;

import javax.annotation.Nonnull;
import java.util.List;

public class BetterLookAtDataHud extends CustomUIHud {
    public static final String DATA_HUD_PATH = "Hud/BetterLookAt/Data/BetterLookAtDataHud.ui";

    private static final String DATA_HUD_TITLE_ID = "#DataHudTitle";
    private static final String DATA_HUD_CONTAINER_ID = "#DataHudContainer";

    private final UICommandBuilder uiCommandBuilder;
    private final BetterLookAtDataIconComponent iconComponent;
    private final BetterLookAtDataContentComponent contentComponent;

    public BetterLookAtDataHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);

        this.uiCommandBuilder = new UICommandBuilder();
        this.iconComponent = new BetterLookAtDataIconComponent(this.uiCommandBuilder);
        this.contentComponent = new BetterLookAtDataContentComponent(this.uiCommandBuilder);

        this.uiCommandBuilder.clear(BetterLookAtDataHud.DATA_HUD_CONTAINER_ID);
    }

    @Override
    protected void build(@Nonnull UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append(BetterLookAtDataHud.DATA_HUD_PATH);
    }

    public void show(@Nonnull Player player) {
        player.getHudManager().setCustomHud(this.getPlayerRef(), this);
    }

    public void hidden(@Nonnull Player player) {
        player.getHudManager().setCustomHud(this.getPlayerRef(), new BetterLookAtHiddenHud(this.getPlayerRef()));
    }

    public void render() {
        this.update(false, this.uiCommandBuilder);
    }

    public void appendIcon() {
        this.iconComponent.append(BetterLookAtDataHud.DATA_HUD_CONTAINER_ID);
    }

    public void appendContent() {
        this.contentComponent.append(BetterLookAtDataHud.DATA_HUD_CONTAINER_ID);
    }

    public void updateIcon(String icon) {
        this.iconComponent.render(icon);
    }

    public void updateTitle(Message title) {
        this.uiCommandBuilder.set(String.format("%s.TextSpans", BetterLookAtDataHud.DATA_HUD_TITLE_ID), title);
    }

    public void updateHealth(float health, int maxHealth) {
        this.contentComponent.appendHealth();
        this.contentComponent.updateHealth(health, maxHealth);
    }

    public void updatePlugin(Message plugin) {
        this.contentComponent.appendPlugin();
        this.contentComponent.updatePlugin(plugin);
    }

    public void updateMetadata(Message metadata) {
        this.contentComponent.appendMetadata();
        this.contentComponent.updateMetadata(metadata);
    }

    public void updateRecommendedTools(List<String> recommendedTools) {
        this.contentComponent.appendRecommendedTool();
        this.contentComponent.updateRecommendedTools(recommendedTools);
    }

    public void updateProcessingBenchState(float progress, float maxProgress) {
        this.contentComponent.appendProcessingBenchState();
        this.contentComponent.updateProcessingBenchState(progress, maxProgress);
    }
}
