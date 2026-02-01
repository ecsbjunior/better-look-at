package com.junior.evandro.ui.data;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.config.BetterLookAtPosition;
import com.junior.evandro.ecs.data.components.*;
import com.junior.evandro.messages.BetterLookAtI18n;
import com.junior.evandro.ui.BetterLookAtCustomHud;
import com.junior.evandro.ui.data.records.BetterLookAtDataRecord;
import com.junior.evandro.ui.core.BetterLookAtAnchor;
import com.junior.evandro.utils.BetterLookAtBooleanUtils;

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
            var benchFuelComponent, var benchTierComponent, var blockIconComponent, var consumableComponent,
            var entityIconComponent, var benchInputsComponent, var benchOutputsComponent, var invulnerableComponent,
            var recommendedToolsComponent, var processingBenchStateComponent
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

            this.update(false, context.uiCommandBuilder);
        }
    }

    @Override
    public void hidden() {
        var context = new Context();

        context.root.hide();

        this.update(false, context.uiCommandBuilder);
    }

    public static final class Context {
        private boolean showBench;
        private boolean showMetadata;
        private boolean shouldSpacing;
        private boolean showingBenchFuels;
        private boolean showingBenchInputs;
        private boolean showingBenchOutputs;
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

        public Context() {
            this.showBench = false;
            this.showMetadata = false;
            this.shouldSpacing = false;
            this.showingBenchFuels = false;
            this.showingBenchInputs = false;
            this.showingBenchOutputs = false;
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
        }
    }

    public static final class Root {
        private static final String ROOT_CONTAINER = "#BetterLookAtHud";

        private final Context context;

        public Root(Context context) {
            this.context = context;
        }

        private void show() {
            var position = BetterLookAt.CONFIG.get().getPosition();
            var anchorBuilder = new BetterLookAtAnchor.Builder().withWidth(360);
            if (position.equals(BetterLookAtPosition.TOP_LEFT)) anchorBuilder.withTop(12).withLeft(30);
            else if (position.equals(BetterLookAtPosition.TOP_RIGHT)) anchorBuilder.withTop(12).withRight(-10);
            else if (position.equals(BetterLookAtPosition.TOP_CENTER)) anchorBuilder.withTop(40).withLeft(0).withRight(-40);
            else if (position.equals(BetterLookAtPosition.BOTTOM_LEFT)) anchorBuilder.withBottom(12).withLeft(30);
            else if (position.equals(BetterLookAtPosition.BOTTOM_RIGHT)) anchorBuilder.withBottom(12).withRight(-10);
            this.context.uiCommandBuilder.set("%s.Visible".formatted(ROOT_CONTAINER), true);
            this.context.uiCommandBuilder.setObject("%s.Anchor".formatted(ROOT_CONTAINER), anchorBuilder.build());
        }

        private void hide() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(ROOT_CONTAINER), false);
        }
    }

    public static final class Title {
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

    public static final class Icon {
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

    public static final class ProgressBar {
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

    public static final class Plugin {
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

    public static final class RecommendedTools {
        private static final String DATA_RECOMMENDED_TOOLS_SELECTOR = "#BetterLookAtDataRecommendedTools";
        private static final String DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR = "#BetterLookAtDataRecommendedToolsContainer";

        private final Context context;

        public RecommendedTools(Context context) {
            this.context = context;
        }

        private void show(BetterLookAtRecommendedToolsComponent recommendedTools) {
            var anchorBuilder = new BetterLookAtAnchor.Builder();
            if (this.context.shouldSpacing) anchorBuilder.withTop(4);
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR), true);
            this.context.uiCommandBuilder.set("%s.ItemStacks".formatted(DATA_RECOMMENDED_TOOLS_SELECTOR), recommendedTools.tools());
            this.context.uiCommandBuilder.setObject("%s.Anchor".formatted(DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR), anchorBuilder.build());
            this.context.shouldSpacing = true;
        }

        private void hide() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_RECOMMENDED_TOOLS_CONTAINER_SELECTOR), false);
        }
    }

    public static final class Chest {
        private static final String DATA_CHEST_SELECTOR = "#BetterLookAtDataChest";
        private static final String DATA_CHEST_CONTAINER_SELECTOR = "#BetterLookAtDataChestContainer";

        private final Context context;

        public Chest(Context context) {
            this.context = context;
        }

        private void show(BetterLookAtChestComponent chest) {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_CHEST_CONTAINER_SELECTOR), true);
            this.context.uiCommandBuilder.set("%s.ItemStacks".formatted(DATA_CHEST_SELECTOR), chest.items());
        }

        private void hide() {
            this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_CHEST_CONTAINER_SELECTOR), false);
        }
    }

    public static final class Metadata {
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

    public static final class Bench {
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

        public static final class Fuels {
            private static final String DATA_BENCH_FUELS_LABEL_SELECTOR = "#BetterLookAtDataBenchFuelsLabel";
            private static final String DATA_BENCH_FUELS_ITEMS_SELECTOR = "#BetterLookAtDataBenchFuelsItems";
            private static final String DATA_BENCH_FUELS_CONTAINER_SELECTOR = "#BetterLookAtDataBenchFuelsContainer";

            private final Context context;

            public Fuels(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtBenchFuelsComponent fuels) {
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(DATA_BENCH_FUELS_LABEL_SELECTOR), BetterLookAtI18n.benchFuels());
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_FUELS_CONTAINER_SELECTOR), true);
                this.context.uiCommandBuilder.set("%s.ItemStacks".formatted(DATA_BENCH_FUELS_ITEMS_SELECTOR), fuels.items());
                this.context.showBench = true;
                this.context.showingBenchFuels = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_FUELS_CONTAINER_SELECTOR), false);
            }
        }

        public static final class Inputs {
            private static final String DATA_BENCH_INPUTS_LABEL_SELECTOR = "#BetterLookAtDataBenchInputsLabel";
            private static final String DATA_BENCH_INPUTS_ITEMS_SELECTOR = "#BetterLookAtDataBenchInputsItems";
            private static final String DATA_BENCH_INPUTS_CONTAINER_SELECTOR = "#BetterLookAtDataBenchInputsContainer";

            private final Context context;

            public Inputs(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtBenchInputsComponent inputs) {
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(DATA_BENCH_INPUTS_LABEL_SELECTOR), BetterLookAtI18n.benchInputs());
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_INPUTS_CONTAINER_SELECTOR), true);
                this.context.uiCommandBuilder.set("%s.ItemStacks".formatted(DATA_BENCH_INPUTS_ITEMS_SELECTOR), inputs.items());
                this.context.showBench = true;
                this.context.showingBenchInputs = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_INPUTS_CONTAINER_SELECTOR), false);
            }
        }

        public static final class Outputs {
            private static final String DATA_BENCH_OUTPUTS_LABEL_SELECTOR = "#BetterLookAtDataBenchOutputsLabel";
            private static final String DATA_BENCH_OUTPUTS_ITEMS_SELECTOR = "#BetterLookAtDataBenchOutputsItems";
            private static final String DATA_BENCH_OUTPUTS_CONTAINER_SELECTOR = "#BetterLookAtDataBenchOutputsContainer";

            private final Context context;

            public Outputs(Context context) {
                this.context = context;
            }

            private void show(BetterLookAtBenchOutputsComponent outputs) {
                this.context.uiCommandBuilder.set(
                    "%s.TextSpans".formatted(DATA_BENCH_OUTPUTS_LABEL_SELECTOR), BetterLookAtI18n.benchOutputs());
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_OUTPUTS_CONTAINER_SELECTOR), true);
                this.context.uiCommandBuilder.set("%s.ItemStacks".formatted(DATA_BENCH_OUTPUTS_ITEMS_SELECTOR), outputs.items());
                this.context.showBench = true;
                this.context.showingBenchOutputs = true;
            }

            private void hide() {
                this.context.uiCommandBuilder.set("%s.Visible".formatted(DATA_BENCH_OUTPUTS_CONTAINER_SELECTOR), false);
            }
        }
    }
}
