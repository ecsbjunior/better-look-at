package com.junior.evandro.ui.data;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.config.BetterLookAtPosition;
import com.junior.evandro.config.BetterLookAtScale;
import com.junior.evandro.ecs.data.BetterLookAtFarmingGrowthStatus;
import com.junior.evandro.ecs.data.components.*;
import com.junior.evandro.messages.BetterLookAtI18n;
import com.junior.evandro.messages.BetterLookAtMessage;
import com.junior.evandro.ui.BetterLookAtCustomHud;
import com.junior.evandro.ui.data.records.BetterLookAtDataRecord;
import com.junior.evandro.ui.core.BetterLookAtAnchor;
import com.junior.evandro.utils.BetterLookAtBooleanUtils;
import com.junior.evandro.utils.BetterLookAtColor;

import javax.annotation.Nonnull;

public class BetterLookAtHud extends BetterLookAtCustomHud {
    private final String PATH = "Hud/%s/BetterLookAtHud.ui".formatted(BetterLookAt.NAME);

    public BetterLookAtHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    protected void build(@Nonnull UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append(this.PATH);
    }

    @Override
    public void render(Object object) {
        if (object instanceof BetterLookAtDataRecord(
            var fuelComponent, var chestComponent, var titleComponent, var pluginComponent, var healthComponent,
            var benchTierComponent, var blockIconComponent, var benchFuelComponent,
            var consumableComponent, var entityIconComponent, var benchInputsComponent, var benchOutputsComponent,
            var invulnerableComponent, var farmingStagesComponent, var recommendedToolsComponent,
            var farmingLightStatusComponent, var farmingWaterStatusComponent, var processingBenchStateComponent,
            var farmingRemainingTimeComponent, var farmingCurrentStageInfoComponent,
            var farmingFertilizerStatusComponent
        )) {
            var context = new Context();

            context.root.show();

            titleComponent.ifPresentOrElse(context.title::show, context.title::hide);
            blockIconComponent.ifPresentOrElse(context.icon::show, context.icon::hideBlock);
            entityIconComponent.ifPresentOrElse(context.icon::show, context.icon::hideEntity);
            healthComponent.ifPresentOrElse(context.progressBar::show, context.progressBar::hideHealth);
            processingBenchStateComponent.ifPresentOrElse(context.progressBar::show, context.progressBar::hideProcessingBenchState);

            benchTierComponent.ifPresentOrElse(context.benchTier::show, context.benchTier::hide);
            fuelComponent.ifPresentOrElse(context.fuel::show, context.fuel::hide);
            consumableComponent.ifPresentOrElse(context.consumable::show, context.consumable::hide);
            invulnerableComponent.ifPresentOrElse(context.invulnerable::show, context.invulnerable::hide);
            BetterLookAtBooleanUtils.ifTrue(context.showMetadata, context.metadata::show, context.metadata::hide);

            recommendedToolsComponent.ifPresentOrElse(context.recommendedTools::show, context.recommendedTools::hide);
            pluginComponent.ifPresentOrElse(context.plugin::show, context.plugin::hide);

            chestComponent.ifPresentOrElse(context.chest::show, context.chest::hide);

            benchFuelComponent.ifPresentOrElse(context.benchFuels::show, context.benchFuels::hide);
            benchInputsComponent.ifPresentOrElse(context.benchInputs::show, context.benchInputs::hide);
            benchOutputsComponent.ifPresentOrElse(context.benchOutputs::show, context.benchOutputs::hide);
            BetterLookAtBooleanUtils.ifTrue(context.showBench, context.bench::show, context.bench::hide);

            farmingStagesComponent.ifPresentOrElse(context.farmingStages::show, context.farmingStages::hide);
            farmingCurrentStageInfoComponent.ifPresentOrElse(context.farmingCurrentStageInfo::show, context.farmingCurrentStageInfo::hide);
            farmingRemainingTimeComponent.ifPresentOrElse(context.farmingRemainingTime::show, context.farmingRemainingTime::hide);
            farmingLightStatusComponent.ifPresentOrElse(context.farmingLightStatus::show, context.farmingLightStatus::hide);
            farmingWaterStatusComponent.ifPresentOrElse(context.farmingWaterStatus::show, context.farmingWaterStatus::hide);
            farmingFertilizerStatusComponent.ifPresentOrElse(context.farmingFertilizerStatus::show, context.farmingFertilizerStatus::hide);
            BetterLookAtBooleanUtils.ifTrue(context.showFarming, context.farming::show, context.farming::hide);

            this.update(false, context.uiCommandBuilder);
        }
    }

    @Override
    public void hidden() {
        var context = new Context();

        context.root.hide();

        this.update(false, context.uiCommandBuilder);
    }

    private static final class Context {
        private boolean showBench;
        private boolean showFarming;
        private boolean showMetadata;
        private boolean shouldSpacing;
        private boolean showingBenchFuels;
        private boolean showingBenchInputs;
        private boolean showingBenchOutputs;
        private boolean showingFarmingStages;
        private boolean showingFarmingCondition;
        private final UICommandBuilder uiCommandBuilder;

        public final Root root;
        public final Icon icon;
        public final Chest chest;
        public final Title title;
        public final Plugin plugin;
        public final ProgressBar progressBar;
        public final RecommendedTools recommendedTools;

        public final Metadata metadata;
        public final Metadata.Fuel fuel;
        public final Metadata.BenchTier benchTier;
        public final Metadata.Consumable consumable;
        public final Metadata.Invulnerable invulnerable;

        public final Bench bench;
        public final Bench.Fuels benchFuels;
        public final Bench.Inputs benchInputs;
        public final Bench.Outputs benchOutputs;

        public final Farming farming;
        public final Farming.Stages farmingStages;
        public final Farming.LightStatus farmingLightStatus;
        public final Farming.WaterStatus farmingWaterStatus;
        public final Farming.RemainingTime farmingRemainingTime;
        public final Farming.CurrentStageInfo farmingCurrentStageInfo;
        public final Farming.FertilizerStatus farmingFertilizerStatus;

        public Context() {
            this.showBench = false;
            this.showFarming = false;
            this.showMetadata = false;
            this.shouldSpacing = false;
            this.showingBenchFuels = false;
            this.showingBenchInputs = false;
            this.showingBenchOutputs = false;
            this.showingFarmingStages = false;
            this.showingFarmingCondition = false;
            this.uiCommandBuilder = new UICommandBuilder();

            this.root = new Root(this);
            this.icon = new Icon(this);
            this.chest = new Chest(this);
            this.title = new Title(this);
            this.plugin = new Plugin(this);
            this.progressBar = new ProgressBar(this);
            this.recommendedTools = new RecommendedTools(this);

            this.metadata = new Metadata(this);
            this.fuel = new Metadata.Fuel(this);
            this.benchTier = new Metadata.BenchTier(this);
            this.consumable = new Metadata.Consumable(this);
            this.invulnerable = new Metadata.Invulnerable(this);

            this.bench = new Bench(this);
            this.benchFuels = new Bench.Fuels(this);
            this.benchInputs = new Bench.Inputs(this);
            this.benchOutputs = new Bench.Outputs(this);

            this.farming = new Farming(this);
            this.farmingStages = new Farming.Stages(this);
            this.farmingLightStatus = new Farming.LightStatus(this);
            this.farmingWaterStatus = new Farming.WaterStatus(this);
            this.farmingRemainingTime = new Farming.RemainingTime(this);
            this.farmingCurrentStageInfo = new Farming.CurrentStageInfo(this);
            this.farmingFertilizerStatus = new Farming.FertilizerStatus(this);
        }
    }

    private static final class Root {
        private static final String ROOT_CONTAINER = "#BetterLookAtHud";

        private final Context context;

        public Root(Context context) {
            this.context = context;
        }

        private void show() {
            var position = BetterLookAt.CONFIG.get().getPosition();
            var anchorBuilder = new BetterLookAtAnchor.Builder().withWidth(360);
            if (position.equals(BetterLookAtPosition.TopLeft)) anchorBuilder.withTop(12).withLeft(30);
            else if (position.equals(BetterLookAtPosition.TopRight)) anchorBuilder.withTop(12).withRight(-10);
            else if (position.equals(BetterLookAtPosition.TopCenter)) anchorBuilder.withTop(40).withLeft(0).withRight(-40);
            else if (position.equals(BetterLookAtPosition.BottomLeft)) anchorBuilder.withBottom(12).withLeft(30);
            else if (position.equals(BetterLookAtPosition.BottomRight)) anchorBuilder.withBottom(12).withRight(-10);
            this.context.uiCommandBuilder.set("%s.Visible".formatted(ROOT_CONTAINER), true);
            this.context.uiCommandBuilder.setObject("%s.Anchor".formatted(ROOT_CONTAINER), anchorBuilder.build());
        }

        private void hide() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(ROOT_CONTAINER), false);
        }
    }

    private static final class Title {
        private static final String DATA_TITLE_SELECTOR = "#BetterLookAtDataTitle";

        private final Context context;

        public Title(Context context) {
            this.context = context;
        }

        private void show(BetterLookAtTitleComponent title) {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_TITLE_SELECTOR), true);
            this.context.uiCommandBuilder.set("%s.TextSpans".formatted(DATA_TITLE_SELECTOR), title.value());
        }

        private void hide() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_TITLE_SELECTOR), false);
        }
    }

    private static final class Icon {
        private static final String DATA_BLOCK_ICON_SELECTOR = "#BetterLookAtDataBlockIcon";
        private static final String DATA_ENTITY_ICON_SELECTOR = "#BetterLookAtDataEntityIcon";

        private final Context context;

        public Icon(Context context) {
            this.context = context;
        }

        private void show(BetterLookAtBlockIconComponent icon) {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BLOCK_ICON_SELECTOR), true);
            this.context.uiCommandBuilder.set("%s.ItemId".formatted(DATA_BLOCK_ICON_SELECTOR), icon.value());
        }

        private void hideBlock() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BLOCK_ICON_SELECTOR), false);
        }

        private void show(BetterLookAtEntityIconComponent icon) {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_ENTITY_ICON_SELECTOR), true);
            this.context.uiCommandBuilder.set("%s.AssetPath".formatted(DATA_ENTITY_ICON_SELECTOR),
                "UI/Custom/Pages/Memories/npcs/%s.png".formatted(icon.value()));
        }

        private void hideEntity() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_ENTITY_ICON_SELECTOR), false);
        }
    }

    private static final class ProgressBar {
        private static final String DATA_HEALTH_CONTAINER_SELECTOR = "#BetterLookAtDataHealthContainer";
        private static final String DATA_HEALTH_PROGRESS_BAR_SELECTOR = "#BetterLookAtDataHealthProgressBar";
        private static final String DATA_HEALTH_PROGRESS_BAR_TEXTURE_SELECTOR = "#BetterLookAtDataHealthProgressBarTexture";

        private static final String DATA_PROCESSING_BENCH_STATE_CONTAINER_SELECTOR = "#BetterLookAtDataProcessingBenchStateContainer";
        private static final String DATA_PROCESSING_BENCH_STATE_PROGRESS_BAR_SELECTOR = "#BetterLookAtDataProcessingBenchStateProgressBar";
        private static final String DATA_PROCESSING_BENCH_STATE_PROGRESS_BAR_TEXTURE_SELECTOR = "#BetterLookAtDataProcessingBenchStateProgressBarTexture";

        private final Context context;

        public ProgressBar(Context context) {
            this.context = context;
        }

        private void show(BetterLookAtHealthComponent health) {
            var anchorBuilder = new BetterLookAtAnchor.Builder().withWidth(274).withHeight(16);
            if (this.context.shouldSpacing) anchorBuilder.withTop(4);
            var normalizedHealth = health.value() / health.maxValue();
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_HEALTH_CONTAINER_SELECTOR), true);
            this.context.uiCommandBuilder.set("%s.Value".formatted(DATA_HEALTH_PROGRESS_BAR_SELECTOR), normalizedHealth);
            this.context.uiCommandBuilder.set("%s.Value".formatted(DATA_HEALTH_PROGRESS_BAR_TEXTURE_SELECTOR), normalizedHealth);
            this.context.uiCommandBuilder.setObject("%s.Anchor".formatted(DATA_HEALTH_CONTAINER_SELECTOR), anchorBuilder.build());
            this.context.shouldSpacing = true;
        }

        private void hideHealth() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_HEALTH_CONTAINER_SELECTOR), false);
        }

        private void show(BetterLookAtProcessingBenchStateComponent processingBenchState) {
            var anchorBuilder = new BetterLookAtAnchor.Builder().withWidth(274).withHeight(16);
            if (this.context.shouldSpacing) anchorBuilder.withTop(4);
            var normalizedProgress = processingBenchState.value() / processingBenchState.maxValue();
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_PROCESSING_BENCH_STATE_CONTAINER_SELECTOR), true);
            this.context.uiCommandBuilder.set("%s.Value".formatted(DATA_PROCESSING_BENCH_STATE_PROGRESS_BAR_SELECTOR), normalizedProgress);
            this.context.uiCommandBuilder.set("%s.Value".formatted(DATA_PROCESSING_BENCH_STATE_PROGRESS_BAR_TEXTURE_SELECTOR), normalizedProgress);
            this.context.uiCommandBuilder.setObject("%s.Anchor".formatted(DATA_PROCESSING_BENCH_STATE_CONTAINER_SELECTOR), anchorBuilder.build());
            this.context.shouldSpacing = true;
        }

        private void hideProcessingBenchState() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_PROCESSING_BENCH_STATE_CONTAINER_SELECTOR), false);
        }
    }

    private static final class Plugin {
        private static final String DATA_PLUGIN_SELECTOR = "#BetterLookAtDataPlugin";
        private static final String DATA_PLUGIN_CONTAINER_SELECTOR = "#BetterLookAtDataPluginContainer";

        private final Context context;

        public Plugin(Context context) {
            this.context = context;
        }

        private void show(BetterLookAtPluginComponent plugin) {
            var anchorBuilder = new BetterLookAtAnchor.Builder().withTop(-4).withBottom(-4);
            if (this.context.shouldSpacing) anchorBuilder.withTop(0);
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_PLUGIN_CONTAINER_SELECTOR), true);
            this.context.uiCommandBuilder.set("%s.TextSpans".formatted(DATA_PLUGIN_SELECTOR), Message.raw(plugin.value()));
            this.context.uiCommandBuilder.setObject("%s.Anchor".formatted(DATA_PLUGIN_SELECTOR), anchorBuilder.build());
            this.context.shouldSpacing = true;
        }

        private void hide() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_PLUGIN_CONTAINER_SELECTOR), false);
        }
    }

    private static final class RecommendedTools {
        private static final String DATA_RECOMMENDED_TOOLS_SELECTOR = "#BetterLookAtRecommendedTools";
        private static final String DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR = "#BetterLookAtRecommendedToolsContainer";

        private final Context context;

        public RecommendedTools(Context context) {
            this.context = context;
        }

        private void show(BetterLookAtRecommendedToolsComponent recommendedTools) {
            var anchorBuilder = new BetterLookAtAnchor.Builder();
            if (this.context.shouldSpacing) anchorBuilder.withTop(4);
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR), true);
            this.context.uiCommandBuilder.clear("%s".formatted(DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR));
            this.context.uiCommandBuilder.append(
                "%s".formatted(DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR), RecommendedTools.getRecommendedToolPath());
            this.context.uiCommandBuilder.set("%s.ItemStacks".formatted(DATA_RECOMMENDED_TOOLS_SELECTOR), recommendedTools.tools());
            this.context.uiCommandBuilder.setObject("%s.Anchor".formatted(DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR), anchorBuilder.build());
            this.context.shouldSpacing = true;
        }

        private void hide() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR), false);
        }

        private static String getRecommendedToolPath() {
            return switch (BetterLookAt.CONFIG.get().getRecommendedToolScale()) {
                case BetterLookAtScale.ExtraSmall -> "Hud/BetterLookAt/RecommendedTool/BetterLookAtRecommendedToolExtraSmall.ui";
                case BetterLookAtScale.Small -> "Hud/BetterLookAt/RecommendedTool/BetterLookAtRecommendedToolSmall.ui";
                case BetterLookAtScale.Medium -> "Hud/BetterLookAt/RecommendedTool/BetterLookAtRecommendedToolMedium.ui";
                case BetterLookAtScale.Large -> "Hud/BetterLookAt/RecommendedTool/BetterLookAtRecommendedToolLarge.ui";
                case BetterLookAtScale.ExtraLarge -> "Hud/BetterLookAt/RecommendedTool/BetterLookAtRecommendedToolExtraLarge.ui";
            };
        }
    }

    private static final class Chest {
        private static final String DATA_CHEST_SELECTOR = "#BetterLookAtDataChest";
        private static final String DATA_CHEST_WRAPPER_SELECTOR = "#BetterLookAtDataChestWrapper";
        private static final String DATA_CHEST_CONTAINER_SELECTOR = "#BetterLookAtDataChestContainer";

        private final Context context;

        public Chest(Context context) {
            this.context = context;
        }

        private void show(BetterLookAtChestComponent chest) {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_CHEST_CONTAINER_SELECTOR), true);

            this.context.uiCommandBuilder.clear("%s".formatted(DATA_CHEST_WRAPPER_SELECTOR));

            var chestPath = switch (BetterLookAt.CONFIG.get().getChestScale()) {
                case BetterLookAtScale.ExtraSmall -> "Hud/BetterLookAt/Chest/BetterLookAtChestExtraSmall.ui";
                case BetterLookAtScale.Small -> "Hud/BetterLookAt/Chest/BetterLookAtChestSmall.ui";
                case BetterLookAtScale.Medium -> "Hud/BetterLookAt/Chest/BetterLookAtChestMedium.ui";
                case BetterLookAtScale.Large -> "Hud/BetterLookAt/Chest/BetterLookAtChestLarge.ui";
                case BetterLookAtScale.ExtraLarge -> "Hud/BetterLookAt/Chest/BetterLookAtChestExtraLarge.ui";
            };

            this.context.uiCommandBuilder.append("%s".formatted(DATA_CHEST_WRAPPER_SELECTOR), chestPath);

            this.context.uiCommandBuilder.set("%s.ItemStacks".formatted(DATA_CHEST_SELECTOR), chest.items());
        }

        private void hide() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_CHEST_CONTAINER_SELECTOR), false);
        }
    }

    private static final class Metadata {
        private static final String DATA_METADATA_CONTAINER_SELECTOR = "#BetterLookAtDataMetadataContainer";

        private final Context context;

        public Metadata(Context context) {
            this.context = context;
        }

        private void show() {
            var anchorBuilder = new BetterLookAtAnchor.Builder().withTop(-2);
            if (this.context.shouldSpacing) anchorBuilder.withTop(5);
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_METADATA_CONTAINER_SELECTOR), true);
            this.context.uiCommandBuilder.setObject("%s.Anchor".formatted(DATA_METADATA_CONTAINER_SELECTOR), anchorBuilder.build());
            this.context.shouldSpacing = true;
        }

        private void hide() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_METADATA_CONTAINER_SELECTOR), false);
        }

        public static final class Fuel {
            private static final String DATA_FUEL_SELECTOR = "#BetterLookAtDataFuel";

            private final Context context;

            public Fuel(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtFuelComponent fuel) {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_FUEL_SELECTOR), true);
                this.context.uiCommandBuilder.set("%s.TextSpans".formatted(DATA_FUEL_SELECTOR), BetterLookAtI18n.fuel(fuel.value()));
                this.context.showMetadata = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_FUEL_SELECTOR), false);
            }
        }

        public static final class Consumable {
            private static final String DATA_CONSUMABLE_SELECTOR = "#BetterLookAtDataConsumable";

            private final Context context;

            public Consumable(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtConsumableComponent consumable) {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_CONSUMABLE_SELECTOR), true);
                this.context.uiCommandBuilder.set(
                        "%s.TextSpans".formatted(DATA_CONSUMABLE_SELECTOR), BetterLookAtI18n.consumable(consumable.value()));
                this.context.showMetadata = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_CONSUMABLE_SELECTOR), false);
            }
        }

        public static final class Invulnerable {
            private static final String DATA_INVULNERABLE_SELECTOR = "#BetterLookAtDataInvulnerable";

            private final Context context;

            public Invulnerable(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtInvulnerableComponent invulnerable) {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_INVULNERABLE_SELECTOR), true);
                this.context.uiCommandBuilder.set(
                        "%s.TextSpans".formatted(DATA_INVULNERABLE_SELECTOR), BetterLookAtI18n.invulnerable(invulnerable.value()));
                this.context.showMetadata = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_INVULNERABLE_SELECTOR), false);
            }
        }

        public static final class BenchTier {
            private static final String DATA_BENCH_TIER_SELECTOR = "#BetterLookAtDataBenchTier";

            private final Context context;

            public BenchTier(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtBenchTierComponent benchTier) {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_TIER_SELECTOR), true);
                this.context.uiCommandBuilder.set("%s.TextSpans".formatted(DATA_BENCH_TIER_SELECTOR), BetterLookAtI18n.benchTier(benchTier.value()));
                this.context.showMetadata = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_TIER_SELECTOR), false);
            }
        }
    }

    private static final class Bench {
        private static final String DATA_BENCH_CONTAINER_SELECTOR = "#BetterLookAtDataBenchContainer";
        private static final String DATA_BENCH_FUELS_INPUTS_SEPARATOR_SELECTOR =
            "#BetterLookAtDataBenchFuelsInputsSeparator";
        private static final String DATA_BENCH_INPUTS_OUTPUTS_SEPARATOR_SELECTOR =
            "#BetterLookAtDataBenchInputsOutputsSeparator";

        private final Context context;

        public Bench(Context context) {
            this.context = context;
        }

        private void show() {
            var showFuelsInputsSeparator = this.context.showingBenchFuels && this.context.showingBenchInputs;
            var showInputsOutputsSeparator = this.context.showingBenchInputs && this.context.showingBenchOutputs;
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_CONTAINER_SELECTOR), true);
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_FUELS_INPUTS_SEPARATOR_SELECTOR), showFuelsInputsSeparator);
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_INPUTS_OUTPUTS_SEPARATOR_SELECTOR), showInputsOutputsSeparator);
        }

        private void hide() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_CONTAINER_SELECTOR), false);
        }

        private static String getBenchItemsPath() {
            return switch (BetterLookAt.CONFIG.get().getBenchScale()) {
                case BetterLookAtScale.ExtraSmall -> "Hud/BetterLookAt/Bench/BetterLookAtBenchItemsExtraSmall.ui";
                case BetterLookAtScale.Small -> "Hud/BetterLookAt/Bench/BetterLookAtBenchItemsSmall.ui";
                case BetterLookAtScale.Medium -> "Hud/BetterLookAt/Bench/BetterLookAtBenchItemsMedium.ui";
                case BetterLookAtScale.Large -> "Hud/BetterLookAt/Bench/BetterLookAtBenchItemsLarge.ui";
                case BetterLookAtScale.ExtraLarge -> "Hud/BetterLookAt/Bench/BetterLookAtBenchItemsExtraLarge.ui";
            };
        }

        public static final class Fuels {
            private static final String DATA_BENCH_FUELS_LABEL_SELECTOR = "#BetterLookAtDataBenchFuelsLabel";
            private static final String DATA_BENCH_FUELS_ITEMS_SELECTOR = "#BetterLookAtDataBenchItems";
            private static final String DATA_BENCH_FUELS_WRAPPER_SELECTOR = "#BetterLookAtDataBenchFuelsWrapper";
            private static final String DATA_BENCH_FUELS_CONTAINER_SELECTOR = "#BetterLookAtDataBenchFuelsContainer";

            private final Context context;

            public Fuels(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtBenchFuelsComponent fuels) {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_FUELS_CONTAINER_SELECTOR), true);

                this.context.uiCommandBuilder.clear("%s".formatted(DATA_BENCH_FUELS_WRAPPER_SELECTOR));
                this.context.uiCommandBuilder.append("%s".formatted(DATA_BENCH_FUELS_WRAPPER_SELECTOR), Bench.getBenchItemsPath());

                this.context.uiCommandBuilder.set("%s.TextSpans".formatted(DATA_BENCH_FUELS_LABEL_SELECTOR), BetterLookAtI18n.benchFuels());

                this.context.uiCommandBuilder.set("%s %s.ItemStacks".formatted(DATA_BENCH_FUELS_WRAPPER_SELECTOR, DATA_BENCH_FUELS_ITEMS_SELECTOR), fuels.items());

                this.context.showBench = true;
                this.context.showingBenchFuels = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_FUELS_CONTAINER_SELECTOR), false);
            }
        }

        public static final class Inputs {
            private static final String DATA_BENCH_INPUTS_LABEL_SELECTOR = "#BetterLookAtDataBenchInputsLabel";
            private static final String DATA_BENCH_INPUTS_ITEMS_SELECTOR = "#BetterLookAtDataBenchItems";
            private static final String DATA_BENCH_INPUTS_WRAPPER_SELECTOR = "#BetterLookAtDataBenchInputsWrapper";
            private static final String DATA_BENCH_INPUTS_CONTAINER_SELECTOR = "#BetterLookAtDataBenchInputsContainer";

            private final Context context;

            public Inputs(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtBenchInputsComponent inputs) {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_INPUTS_CONTAINER_SELECTOR), true);

                this.context.uiCommandBuilder.clear("%s".formatted(DATA_BENCH_INPUTS_WRAPPER_SELECTOR));
                this.context.uiCommandBuilder.append("%s".formatted(DATA_BENCH_INPUTS_WRAPPER_SELECTOR), Bench.getBenchItemsPath());

                this.context.uiCommandBuilder.set("%s.TextSpans".formatted(DATA_BENCH_INPUTS_LABEL_SELECTOR), BetterLookAtI18n.benchInputs());

                this.context.uiCommandBuilder.set("%s %s.ItemStacks".formatted(DATA_BENCH_INPUTS_WRAPPER_SELECTOR, DATA_BENCH_INPUTS_ITEMS_SELECTOR), inputs.items());

                this.context.showBench = true;
                this.context.showingBenchInputs = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_INPUTS_CONTAINER_SELECTOR), false);
            }
        }

        public static final class Outputs {
            private static final String DATA_BENCH_OUTPUTS_LABEL_SELECTOR = "#BetterLookAtDataBenchOutputsLabel";
            private static final String DATA_BENCH_OUTPUTS_ITEMS_SELECTOR = "#BetterLookAtDataBenchItems";
            private static final String DATA_BENCH_OUTPUTS_WRAPPER_SELECTOR = "#BetterLookAtDataBenchOutputsWrapper";
            private static final String DATA_BENCH_OUTPUTS_CONTAINER_SELECTOR = "#BetterLookAtDataBenchOutputsContainer";

            private final Context context;

            public Outputs(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtBenchOutputsComponent outputs) {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_OUTPUTS_CONTAINER_SELECTOR), true);

                this.context.uiCommandBuilder.clear("%s".formatted(DATA_BENCH_OUTPUTS_WRAPPER_SELECTOR));
                this.context.uiCommandBuilder.append("%s".formatted(DATA_BENCH_OUTPUTS_WRAPPER_SELECTOR), Bench.getBenchItemsPath());

                this.context.uiCommandBuilder.set("%s.TextSpans".formatted(DATA_BENCH_OUTPUTS_LABEL_SELECTOR), BetterLookAtI18n.benchOutputs());

                this.context.uiCommandBuilder.set("%s %s.ItemStacks".formatted(DATA_BENCH_OUTPUTS_WRAPPER_SELECTOR, DATA_BENCH_OUTPUTS_ITEMS_SELECTOR), outputs.items());

                this.context.showBench = true;
                this.context.showingBenchOutputs = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_OUTPUTS_CONTAINER_SELECTOR), false);
            }
        }
    }

    private static final class Farming {
        private static final String FARMING_CONTAINER_SELECTOR = "#BetterLookAtFarmingContainer";
        private static final String FARMING_CONDITION_TIPS_SEPARATOR_SELECTOR = "#BetterLookAtFarmingConditionTipsSeparator";
        private static final String FARMING_TIME_CONDITION_SEPARATOR_SELECTOR = "#BetterLookAtFarmingTimeConditionSeparator";

        private final Context context;

        public Farming(Context context) {
            this.context = context;
        }

        private void show() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_CONTAINER_SELECTOR), true);
            this.context.uiCommandBuilder.set(
                "%s.Visible".formatted(FARMING_TIME_CONDITION_SEPARATOR_SELECTOR), this.context.showingFarmingCondition);
        }

        private void hide() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_CONTAINER_SELECTOR), false);
        }

        public static final class Stages {
            private static final String FARMING_STAGES_PATH = "Hud/BetterLookAt/Farming/BetterLookAtFarmingStages.ui";

            private static final String FARMING_STAGE_SELECTOR = "#BetterLookAtFarmingStages";
            private static final String FARMING_STAGES_CONTAINER_SELECTOR = "#BetterLookAtFarmingStagesContainer";
            private static final String FARMING_STAGES_PROGRESS_BAR_SELECTOR = "#BetterLookAtFarmingStagesProgressBar";
            private static final String FARMING_STAGES_PROGRESS_BAR_TEXTURE_SELECTOR = "#BetterLookAtFarmingStagesProgressBarTexture";

            private final Context context;

            public Stages(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtFarmingStagesComponent farmingStages) {
                var index = 0;

                this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_STAGES_CONTAINER_SELECTOR), true);

                this.context.uiCommandBuilder.clear("%s".formatted(FARMING_STAGES_CONTAINER_SELECTOR));

                for (var farmingStage : farmingStages.stages()) {
                    var normalizedStage = ((float) farmingStage.value()) / farmingStage.maxValue();

                    if (index > 0) {
                        this.context.uiCommandBuilder.appendInline(FARMING_STAGES_CONTAINER_SELECTOR, "Group { Anchor: (Width: 4); }");
                        index += 1;
                    }

                    var BASE_FARMING_SELECTOR = "%s[%d]".formatted(FARMING_STAGES_CONTAINER_SELECTOR, index++);

                    this.context.uiCommandBuilder.append(FARMING_STAGES_CONTAINER_SELECTOR, FARMING_STAGES_PATH);
                    this.context.uiCommandBuilder.set("%s %s.Value".formatted(BASE_FARMING_SELECTOR, FARMING_STAGES_PROGRESS_BAR_SELECTOR), normalizedStage);
                    this.context.uiCommandBuilder.set("%s %s.Value".formatted(BASE_FARMING_SELECTOR, FARMING_STAGES_PROGRESS_BAR_TEXTURE_SELECTOR), normalizedStage);
                }

                this.context.showFarming = true;
                this.context.showingFarmingStages = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_STAGES_CONTAINER_SELECTOR), false);
            }
        }

        public static final class CurrentStageInfo {
            private static final String FARMING_CURRENT_STAGE_INFO_CONTAINER_SELECTOR = "#BetterLookAtFarmingCurrentStageInfoContainer";
            private static final String FARMING_CURRENT_STAGE_INFO_LEFT_LABEL_SELECTOR = "#BetterLookAtFarmingCurrentStageInfoLeftLabel";
            private static final String FARMING_CURRENT_STAGE_INFO_RIGHT_LABEL_SELECTOR = "#BetterLookAtFarmingCurrentStageInfoRightLabel";

            private final Context context;

            public CurrentStageInfo(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtFarmingCurrentStageInfoComponent currentStageInfo) {
                var anchor = new BetterLookAtAnchor.Builder();
                var percentage = ((float) currentStageInfo.elapsedTime()) / currentStageInfo.stageDurationTime() * 100.0;
                var stageMessage = BetterLookAtI18n.Farming.CurrentStageInfo
                    .stage(currentStageInfo.stage(), currentStageInfo.maxStage());
                var percentageMessage = Message.raw("%.02f%%".formatted(percentage)).bold(true);
                if (percentage >= 25.0) percentageMessage.color(BetterLookAtColor.YELLOW);
                if (percentage >= 75.0) percentageMessage.color(BetterLookAtColor.SUCCESS);
                if (this.context.showingFarmingStages) anchor.withTop(6);
                this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_CURRENT_STAGE_INFO_CONTAINER_SELECTOR), true);
                this.context.uiCommandBuilder.setObject("%s.Anchor".formatted(FARMING_CURRENT_STAGE_INFO_CONTAINER_SELECTOR), anchor.build());
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(FARMING_CURRENT_STAGE_INFO_LEFT_LABEL_SELECTOR), stageMessage);
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(FARMING_CURRENT_STAGE_INFO_RIGHT_LABEL_SELECTOR), percentageMessage);
                this.context.showFarming = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_CURRENT_STAGE_INFO_CONTAINER_SELECTOR), false);
            }
        }

        // TODO(evandro): should rename to REMAINING TIME
        public static final class RemainingTime {
            private static final String FARMING_TIME_CONTAINER_SELECTOR = "#BetterLookAtFarmingTimeContainer";
            private static final String FARMING_TIME_LEFT_LABEL_SELECTOR = "#BetterLookAtFarmingTimeLeftLabel";
            private static final String FARMING_TIME_RIGHT_LABEL_SELECTOR = "#BetterLookAtFarmingTimeRightLabel";

            private final Context context;

            public RemainingTime(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtFarmingRemainingTimeComponent farmingRemainingTime) {
                var anchor = new BetterLookAtAnchor.Builder();
                if (this.context.showingFarmingStages) anchor.withTop(4);
                var remainingTimeMessage = BetterLookAtI18n.Farming.RemainingTime.remainingTime();
                var remainingTimeInSeconds = farmingRemainingTime.value() / 33 + 1;
                this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_TIME_CONTAINER_SELECTOR), true);
                this.context.uiCommandBuilder.setObject("%s.Anchor".formatted(FARMING_TIME_CONTAINER_SELECTOR), anchor.build());
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(FARMING_TIME_LEFT_LABEL_SELECTOR), remainingTimeMessage);
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(FARMING_TIME_RIGHT_LABEL_SELECTOR),
                    Message.raw("%ds".formatted(remainingTimeInSeconds)));
                this.context.showFarming = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_TIME_CONTAINER_SELECTOR), false);
            }
        }

        public static final class LightStatus {
            private static final String FARMING_LIGHT_STATUS_CONTAINER_SELECTOR = "#BetterLookAtFarmingLightStatusContainer";
            private static final String FARMING_LIGHT_STATUS_LEFT_LABEL_SELECTOR = "#BetterLookAtFarmingLightStatusLeftLabel";
            private static final String FARMING_LIGHT_STATUS_RIGHT_LABEL_SELECTOR = "#BetterLookAtFarmingLightStatusRightLabel";

            private final Context context;

            public LightStatus(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtFarmingLightStatusComponent farmingLightStatus) {
                var lightStatusMessage = BetterLookAtI18n.Farming.LightStatus.light();
                var lightGrowthStatusMessage = BetterLookAtI18n.Farming.GrowthStatus.status(farmingLightStatus.status());
                var multiplierMessage = Message.raw(" (%.1fx)".formatted(farmingLightStatus.multiplier())).color(BetterLookAtColor.PRIMARY);
                this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_LIGHT_STATUS_CONTAINER_SELECTOR), true);
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(FARMING_LIGHT_STATUS_LEFT_LABEL_SELECTOR), lightStatusMessage);
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(FARMING_LIGHT_STATUS_RIGHT_LABEL_SELECTOR), Message.join(lightGrowthStatusMessage, multiplierMessage));
                this.context.showFarming = true;
                this.context.showingFarmingCondition = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_LIGHT_STATUS_CONTAINER_SELECTOR), false);
            }
        }

        public static final class WaterStatus {
            private static final String FARMING_WATER_STATUS_CONTAINER_SELECTOR = "#BetterLookAtFarmingWaterStatusContainer";
            private static final String FARMING_WATER_STATUS_LEFT_LABEL_SELECTOR = "#BetterLookAtFarmingWaterStatusLeftLabel";
            private static final String FARMING_WATER_STATUS_RIGHT_LABEL_SELECTOR = "#BetterLookAtFarmingWaterStatusRightLabel";

            private final Context context;

            public WaterStatus(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtFarmingWaterStatusComponent farmingWaterStatus) {
                var waterStatusMessage = BetterLookAtI18n.Farming.WaterStatus.water();
                var waterGrowthStatusMessage = BetterLookAtI18n.Farming.GrowthStatus.status(farmingWaterStatus.status());
                var multiplierMessage = Message.raw(" (%.1fx)".formatted(farmingWaterStatus.multiplier())).color(BetterLookAtColor.PRIMARY);
                this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_WATER_STATUS_CONTAINER_SELECTOR), true);
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(FARMING_WATER_STATUS_LEFT_LABEL_SELECTOR), waterStatusMessage);
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(FARMING_WATER_STATUS_RIGHT_LABEL_SELECTOR), Message.join(waterGrowthStatusMessage, multiplierMessage));
                this.context.showFarming = true;
                this.context.showingFarmingCondition = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_WATER_STATUS_CONTAINER_SELECTOR), false);
            }
        }

        public static final class FertilizerStatus {
            private static final String FARMING_FERTILIZER_STATUS_CONTAINER_SELECTOR = "#BetterLookAtFarmingFertilizerStatusContainer";
            private static final String FARMING_FERTILIZER_STATUS_LEFT_LABEL_SELECTOR = "#BetterLookAtFarmingFertilizerStatusLeftLabel";
            private static final String FARMING_FERTILIZER_STATUS_RIGHT_LABEL_SELECTOR = "#BetterLookAtFarmingFertilizerStatusRightLabel";

            private final Context context;

            public FertilizerStatus(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtFarmingFertilizerStatusComponent farmingFertilizerStatus) {
                var fertilizerStatusMessage = BetterLookAtI18n.Farming.FertilizerStatus.fertilizer();
                var waterGrowthStatusMessage = BetterLookAtI18n.Farming.GrowthStatus.status(farmingFertilizerStatus.status());
                var multiplierMessage = Message.raw(" (%.1fx)".formatted(farmingFertilizerStatus.multiplier())).color(BetterLookAtColor.PRIMARY);
                this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_FERTILIZER_STATUS_CONTAINER_SELECTOR), true);
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(FARMING_FERTILIZER_STATUS_LEFT_LABEL_SELECTOR), fertilizerStatusMessage);
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(FARMING_FERTILIZER_STATUS_RIGHT_LABEL_SELECTOR), Message.join(waterGrowthStatusMessage, multiplierMessage));
                this.context.showFarming = true;
                this.context.showingFarmingCondition = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(FARMING_FERTILIZER_STATUS_CONTAINER_SELECTOR), false);
            }
        }
    }
}
