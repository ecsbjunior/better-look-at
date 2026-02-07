package com.junior.evandro.ecs.data.components;

import com.junior.evandro.ecs.IBetterLookAtComponent;
import com.junior.evandro.ecs.data.BetterLookAtFarmingGrowthStatus;

public record BetterLookAtFarmingFertilizerStatusComponent(BetterLookAtFarmingGrowthStatus status, double multiplier) implements IBetterLookAtComponent { }
