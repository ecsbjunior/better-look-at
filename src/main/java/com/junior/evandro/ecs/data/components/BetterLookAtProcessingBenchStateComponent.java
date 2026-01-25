package com.junior.evandro.ecs.data.components;

import com.junior.evandro.ecs.IBetterLookAtComponent;

public record BetterLookAtProcessingBenchStateComponent(float value, float maxValue) implements IBetterLookAtComponent { }
