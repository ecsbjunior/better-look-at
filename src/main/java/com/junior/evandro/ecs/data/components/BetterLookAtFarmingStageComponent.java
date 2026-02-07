package com.junior.evandro.ecs.data.components;

import com.junior.evandro.ecs.IBetterLookAtComponent;

public record BetterLookAtFarmingStageComponent(long value, long maxValue) implements IBetterLookAtComponent { }
