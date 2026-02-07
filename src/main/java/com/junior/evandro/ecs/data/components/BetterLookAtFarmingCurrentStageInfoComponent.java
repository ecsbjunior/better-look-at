package com.junior.evandro.ecs.data.components;

import com.junior.evandro.ecs.IBetterLookAtComponent;

public record BetterLookAtFarmingCurrentStageInfoComponent(
    int stage, int maxStage, long elapsedTime, long stageDurationTime) implements IBetterLookAtComponent { }
