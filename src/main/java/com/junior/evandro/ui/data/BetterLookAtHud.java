package com.junior.evandro.ui.data;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.ecs.data.components.*;
import com.junior.evandro.ui.BetterLookAtCustomHud;
import com.junior.evandro.utils.BetterLookAtMessage;
import com.junior.evandro.ui.data.records.BetterLookAtDataRecord;
import com.junior.evandro.utils.BetterLookAtAnchor;

import javax.annotation.Nonnull;

public class BetterLookAtHud extends BetterLookAtCustomHud {
    private final String PATH = "Hud/%s/BetterLookAtHud.ui".formatted(BetterLookAt.NAME);

    private final String ROOT_CONTAINER = "#BetterLookAtHud";

    private final String DATA_TITLE_SELECTOR = "#BetterLookAtDataTitle";
    private final String DATA_BLOCK_ICON_SELECTOR = "#BetterLookAtDataBlockIcon";
    private final String DATA_ENTITY_ICON_SELECTOR = "#BetterLookAtDataEntityIcon";

    private final String DATA_HEALTH_CONTAINER_SELECTOR = "#BetterLookAtDataHealthContainer";
    private final String DATA_HEALTH_PROGRESS_BAR_SELECTOR = "#BetterLookAtDataHealthProgressBar";
    private final String DATA_HEALTH_PROGRESS_BAR_TEXTURE_SELECTOR = "#BetterLookAtDataHealthProgressBarTexture";

    private final String DATA_PROCESSING_BENCH_STATE_CONTAINER_SELECTOR =
        "#BetterLookAtDataProcessingBenchStateContainer";
    private final String DATA_PROCESSING_BENCH_STATE_PROGRESS_BAR_SELECTOR =
        "#BetterLookAtDataProcessingBenchStateProgressBar";
    private final String DATA_PROCESSING_BENCH_STATE_PROGRESS_BAR_TEXTURE_SELECTOR =
        "#BetterLookAtDataProcessingBenchStateProgressBarTexture";

    private final String DATA_PLUGIN_SELECTOR = "#BetterLookAtDataPlugin";
    private final String DATA_PLUGIN_CONTAINER_SELECTOR = "#BetterLookAtDataPluginContainer";

    private final String DATA_RECOMMENDED_TOOLS_SELECTOR = "#BetterLookAtDataRecommendedTools";
    private final String DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR = "#BetterLookAtDataRecommendedToolsContainer";

    private final String DATA_CHEST_SELECTOR = "#BetterLookAtDataChest";
    private final String DATA_CHEST_CONTAINER_SELECTOR = "#BetterLookAtDataChestContainer";

    private final String DATA_FUEL_SELECTOR = "#BetterLookAtDataFuel";
    private final String DATA_CONSUMABLE_SELECTOR = "#BetterLookAtDataConsumable";
    private final String DATA_INVULNERABLE_SELECTOR = "#BetterLookAtDataInvulnerable";
    private final String DATA_METADATA_CONTAINER_SELECTOR = "#BetterLookAtDataMetadataContainer";

    private boolean shouldSpacing;
    private UICommandBuilder uiCommandBuilder;

    public BetterLookAtHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    protected void build(@Nonnull UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append(this.PATH);
    }

    @Override
    public void render(Object object) {
        if (object instanceof BetterLookAtDataRecord record) {
            this.shouldSpacing = false;
            this.uiCommandBuilder = new UICommandBuilder();

            this.showRoot();
            this.hiddenMetadata();

            record.titleComponent().ifPresentOrElse(this::showTitle, this::hiddenTitle);
            record.blockIconComponent().ifPresentOrElse(this::showBlockIcon, this::hiddenBlockIcon);
            record.entityIconComponent().ifPresentOrElse(this::showEntityIcon, this::hiddenEntityIcon);
            record.healthComponent().ifPresentOrElse(this::showHealth, this::hiddenHealth);
            record.processingBenchStateComponent().ifPresentOrElse(
                this::showProcessingBenchState, this::hiddenProcessingBenchState);
            record.fuelComponent().ifPresentOrElse(this::showFuel, this::hiddenFuel);
            record.consumableComponent().ifPresentOrElse(this::showConsumable, this::hiddenConsumable);
            record.invulnerableComponent().ifPresentOrElse(this::showInvulnerable, this::hiddenInvulnerable);
            record.recommendedToolsComponent().ifPresentOrElse(this::showRecommendedTools, this::hiddenRecommendedTools);
            record.pluginComponent().ifPresentOrElse(this::showPlugin, this::hiddenPlugin);
            record.chestComponent().ifPresentOrElse(this::showChest, this::hiddenChest);

            this.update(false, uiCommandBuilder);
        }
    }

    @Override
    public void hidden() {
        this.uiCommandBuilder = new UICommandBuilder();
        this.hiddenRoot();
        this.update(false, uiCommandBuilder);
    }

    private void showRoot() {
        this.uiCommandBuilder.set("%s.Visible".formatted(this.ROOT_CONTAINER), true);
    }

    private void hiddenRoot() {
        this.uiCommandBuilder.set("%s.Visible".formatted(this.ROOT_CONTAINER), false);
    }

    private void showTitle(BetterLookAtTitleComponent title) {
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_TITLE_SELECTOR), true);
        this.uiCommandBuilder.set("%s.TextSpans".formatted(DATA_TITLE_SELECTOR), title.value());
    }

    private void hiddenTitle() {
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_TITLE_SELECTOR), false);
    }

    private void showBlockIcon(BetterLookAtBlockIconComponent icon) {
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_BLOCK_ICON_SELECTOR), true);
        this.uiCommandBuilder.set("%s.ItemId".formatted(DATA_BLOCK_ICON_SELECTOR), icon.value());
    }

    private void hiddenBlockIcon() {
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_BLOCK_ICON_SELECTOR), false);
    }

    private void showEntityIcon(BetterLookAtEntityIconComponent icon) {
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_ENTITY_ICON_SELECTOR), true);
        this.uiCommandBuilder.set("%s.AssetPath".formatted(DATA_ENTITY_ICON_SELECTOR),
            "UI/Custom/Pages/Memories/npcs/%s.png".formatted(icon.value()));
    }

    private void hiddenEntityIcon() {
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_ENTITY_ICON_SELECTOR), false);
    }

    private void showHealth(BetterLookAtHealthComponent health) {
        var normalizedHealth = health.value() / health.maxValue();
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_HEALTH_CONTAINER_SELECTOR), true);
        this.uiCommandBuilder.set("%s.Value".formatted(DATA_HEALTH_PROGRESS_BAR_SELECTOR), normalizedHealth);
        this.uiCommandBuilder.set("%s.Value".formatted(DATA_HEALTH_PROGRESS_BAR_TEXTURE_SELECTOR), normalizedHealth);
        this.shouldSpacing = true;
    }

    private void hiddenHealth() {
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_HEALTH_CONTAINER_SELECTOR), false);
    }

    private void showProcessingBenchState(BetterLookAtProcessingBenchStateComponent processingBenchState) {
        var anchorBuilder = new BetterLookAtAnchor.Builder().withWidth(274).withHeight(16);
        if (this.shouldSpacing) anchorBuilder.withTop(4);
        var normalizedProgress = processingBenchState.value() / processingBenchState.maxValue();
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_PROCESSING_BENCH_STATE_CONTAINER_SELECTOR), true);
        this.uiCommandBuilder.set("%s.Value".formatted(DATA_PROCESSING_BENCH_STATE_PROGRESS_BAR_SELECTOR), normalizedProgress);
        this.uiCommandBuilder.set("%s.Value".formatted(DATA_PROCESSING_BENCH_STATE_PROGRESS_BAR_TEXTURE_SELECTOR), normalizedProgress);
        this.uiCommandBuilder.setObject("%s.Anchor".formatted(DATA_PROCESSING_BENCH_STATE_CONTAINER_SELECTOR), anchorBuilder.build());
        this.shouldSpacing = true;
    }

    private void hiddenProcessingBenchState() {
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_PROCESSING_BENCH_STATE_CONTAINER_SELECTOR), false);
    }

    private void showPlugin(BetterLookAtPluginComponent plugin) {
        var anchorBuilder = new BetterLookAtAnchor.Builder().withTop(-4).withBottom(-4);
        if (this.shouldSpacing) anchorBuilder.withTop(0);
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_PLUGIN_CONTAINER_SELECTOR), true);
        this.uiCommandBuilder.set("%s.TextSpans".formatted(DATA_PLUGIN_SELECTOR), Message.raw(plugin.value()));
        this.uiCommandBuilder.setObject("%s.Anchor".formatted(DATA_PLUGIN_SELECTOR), anchorBuilder.build());
        this.shouldSpacing = true;
    }

    private void hiddenPlugin() {
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_PLUGIN_CONTAINER_SELECTOR), false);
    }

    private void showRecommendedTools(BetterLookAtRecommendedToolsComponent recommendedTools) {
        var anchorBuilder = new BetterLookAtAnchor.Builder();
        if (this.shouldSpacing) anchorBuilder.withTop(4);
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR), true);
        this.uiCommandBuilder.set("%s.ItemStacks".formatted(DATA_RECOMMENDED_TOOLS_SELECTOR), recommendedTools.tools());
        this.uiCommandBuilder.setObject("%s.Anchor".formatted(DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR), anchorBuilder.build());
        this.shouldSpacing = true;
    }

    private void hiddenRecommendedTools() {
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR), false);
    }

    private void showChest(BetterLookAtChestComponent chest) {
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_CHEST_CONTAINER_SELECTOR), true);
        this.uiCommandBuilder.set("%s.ItemStacks".formatted(DATA_CHEST_SELECTOR), chest.items());
    }

    private void hiddenChest() {
        this.uiCommandBuilder.set("%s.Visible".formatted(DATA_CHEST_CONTAINER_SELECTOR), false);
    }

    private void showFuel(BetterLookAtFuelComponent fuel) {
        var isFuel = fuel.value() > 1;
        var fuelMessage = Message.raw("Fuel: ");
        var isFuelMessage = BetterLookAtMessage.toMessage(isFuel);
        var fuelQualityMessage = BetterLookAtMessage.showIf(
            BetterLookAtMessage.wrapParentheses(BetterLookAtMessage.toMessage(fuel.value())), isFuel);
        this.showMetadata();
        this.uiCommandBuilder.set("%s.Visible".formatted(this.DATA_FUEL_SELECTOR), true);
        this.uiCommandBuilder.set("%s.TextSpans".formatted(this.DATA_FUEL_SELECTOR),
            Message.join(fuelMessage, isFuelMessage, fuelQualityMessage));
    }

    private void hiddenFuel() {
        this.uiCommandBuilder.set("%s.Visible".formatted(this.DATA_FUEL_SELECTOR), false);
    }

    private void showConsumable(BetterLookAtConsumableComponent consumable) {
        var consumableMessage = Message.raw("Consumable: ");
        var isConsumableMessage = BetterLookAtMessage.toMessage(consumable.value());
        this.showMetadata();
        this.uiCommandBuilder.set("%s.Visible".formatted(this.DATA_CONSUMABLE_SELECTOR), true);
        this.uiCommandBuilder.set("%s.TextSpans".formatted(this.DATA_CONSUMABLE_SELECTOR),
            Message.join(consumableMessage, isConsumableMessage));
    }

    private void hiddenConsumable() {
        this.uiCommandBuilder.set("%s.Visible".formatted(this.DATA_CONSUMABLE_SELECTOR), false);
    }

    private void showInvulnerable(BetterLookAtInvulnerableComponent invulnerable) {
        var invulnerableMessage = Message.raw("Invulnerable: ");
        var isInvulnerableMessage = BetterLookAtMessage.toMessage(invulnerable.value());
        this.showMetadata();
        this.uiCommandBuilder.set("%s.Visible".formatted(this.DATA_INVULNERABLE_SELECTOR), true);
        this.uiCommandBuilder.set("%s.TextSpans".formatted(this.DATA_INVULNERABLE_SELECTOR),
            Message.join(invulnerableMessage, isInvulnerableMessage));
    }

    private void hiddenInvulnerable() {
        this.uiCommandBuilder.set("%s.Visible".formatted(this.DATA_INVULNERABLE_SELECTOR), false);
    }

    private void showMetadata() {
        this.uiCommandBuilder.set("%s.Visible".formatted(this.DATA_METADATA_CONTAINER_SELECTOR), true);
    }

    private void hiddenMetadata() {
        this.uiCommandBuilder.set("%s.Visible".formatted(this.DATA_METADATA_CONTAINER_SELECTOR), false);
    }
}
