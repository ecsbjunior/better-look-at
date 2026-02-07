package com.junior.evandro.ui.data.records;

import com.junior.evandro.ecs.data.components.*;

import java.util.Optional;

public record BetterLookAtDataRecord(
    Optional<BetterLookAtFuelComponent> fuelComponent,
    Optional<BetterLookAtChestComponent> chestComponent,
    Optional<BetterLookAtTitleComponent> titleComponent,
    Optional<BetterLookAtPluginComponent> pluginComponent,
    Optional<BetterLookAtHealthComponent> healthComponent,
    Optional<BetterLookAtBenchTierComponent> benchTierComponent,
    Optional<BetterLookAtBlockIconComponent> blockIconComponent,
    Optional<BetterLookAtBenchFuelsComponent> benchFuelComponent,
    Optional<BetterLookAtConsumableComponent> consumableComponent,
    Optional<BetterLookAtEntityIconComponent> entityIconComponent,
    Optional<BetterLookAtBenchInputsComponent> benchInputsComponent,
    Optional<BetterLookAtBenchOutputsComponent> benchOutputsComponent,
    Optional<BetterLookAtInvulnerableComponent> invulnerableComponent,
    Optional<BetterLookAtFarmingStagesComponent> farmingStagesComponent,
    Optional<BetterLookAtRecommendedToolsComponent> recommendedToolsComponent,
    Optional<BetterLookAtFarmingLightStatusComponent> farmingLightStatusComponent,
    Optional<BetterLookAtFarmingWaterStatusComponent> farmingWaterStatusComponent,
    Optional<BetterLookAtProcessingBenchStateComponent> processingBenchStateComponent,
    Optional<BetterLookAtFarmingRemainingTimeComponent> farmingRemainingTimeComponent,
    Optional<BetterLookAtFarmingCurrentStageInfoComponent> farmingCurrentStageInfoComponent,
    Optional<BetterLookAtFarmingFertilizerStatusComponent> farmingFertilizerStatusComponent
) { }
