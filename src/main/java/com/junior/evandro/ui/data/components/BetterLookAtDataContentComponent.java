package com.junior.evandro.ui.data.components;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;

import javax.annotation.Nonnull;
import java.util.List;

public class BetterLookAtDataContentComponent extends BetterLookAtDataComponent {
    private static final String DATA_CONTENT_PATH =
        BetterLookAtDataComponent.BASE_PATH + "BetterLookAtDataContent.ui";

    private static final String DATA_CONTENT_ID = "#DataContent";
    private static final String DATA_CONTENT_CONTAINER_ID = "#DataContentContainer";

    @Nonnull
    private final UICommandBuilder uiCommandBuilder;
    private final BetterLookAtDataPluginComponent pluginComponent;
    private final BetterLookAtDataHealthComponent healthComponent;
    private final BetterLookAtDataMetadataComponent metadataComponent;
    private final BetterLookAtDataRecommendedToolComponent recommendedToolComponent;
    private final BetterLookAtDataProcessingBenchStateComponent processingBenchStateComponent;

    public BetterLookAtDataContentComponent(@Nonnull UICommandBuilder uiCommandBuilder) {
        this.uiCommandBuilder = uiCommandBuilder;

        this.pluginComponent = new BetterLookAtDataPluginComponent(this.uiCommandBuilder);
        this.healthComponent = new BetterLookAtDataHealthComponent(this.uiCommandBuilder);
        this.metadataComponent = new BetterLookAtDataMetadataComponent(this.uiCommandBuilder);
        this.recommendedToolComponent = new BetterLookAtDataRecommendedToolComponent(this.uiCommandBuilder);
        this.processingBenchStateComponent = new BetterLookAtDataProcessingBenchStateComponent(this.uiCommandBuilder);
    }

    public void append(String parent) {
        this.uiCommandBuilder.append(parent, BetterLookAtDataContentComponent.DATA_CONTENT_PATH);
    }

    public void render() {

    }

    public void appendPlugin() {
        this.pluginComponent.append(BetterLookAtDataContentComponent.DATA_CONTENT_ID);
    }

    public void appendHealth() {
        this.healthComponent.append(BetterLookAtDataContentComponent.DATA_CONTENT_ID);
    }

    public void appendMetadata() {
        this.metadataComponent.append(BetterLookAtDataContentComponent.DATA_CONTENT_ID);
    }

    public void appendRecommendedTool() {
        this.recommendedToolComponent.append(BetterLookAtDataContentComponent.DATA_CONTENT_ID);
    }

    public void appendProcessingBenchState() {
        this.processingBenchStateComponent.append(BetterLookAtDataContentComponent.DATA_CONTENT_ID);
    }

    public void updatePlugin(Message plugin) {
        this.pluginComponent.render(plugin);
    }

    public void updateHealth(float health, int maxHealth) {
        this.healthComponent.render(health, maxHealth);
    }

    public void updateMetadata(Message metadata) {
        this.metadataComponent.render(metadata);
    }

    public void updateRecommendedTools(List<String> recommendedTools) {
        for (var recommendedTool : recommendedTools) {
            this.recommendedToolComponent.render(recommendedTool);
        }
    }

    public void updateProcessingBenchState(float progress, float maxProgress) {
        this.processingBenchStateComponent.render(progress, maxProgress);
    }
}
