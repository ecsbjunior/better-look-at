package com.junior.evandro.ui.data.records;

import com.junior.evandro.ecs.data.components.*;

import java.util.Optional;

public record BetterLookAtDataRecord(
    Optional<BetterLookAtFuelComponent> fuelComponent,
    Optional<BetterLookAtChestComponent> chestComponent,
    Optional<BetterLookAtTitleComponent> titleComponent,
    Optional<BetterLookAtPluginComponent> pluginComponent,
    Optional<BetterLookAtHealthComponent> healthComponent,
    Optional<BetterLookAtBlockIconComponent> blockIconComponent,
    Optional<BetterLookAtConsumableComponent> consumableComponent,
    Optional<BetterLookAtEntityIconComponent> entityIconComponent,
    Optional<BetterLookAtInvulnerableComponent> invulnerableComponent,
    Optional<BetterLookAtRecommendedToolsComponent> recommendedToolsComponent,
    Optional<BetterLookAtProcessingBenchStateComponent> processingBenchStateComponent
) { }
