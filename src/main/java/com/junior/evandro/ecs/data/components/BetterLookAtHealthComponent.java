package com.junior.evandro.ecs.data.components;

import com.junior.evandro.ecs.IBetterLookAtComponent;

public record BetterLookAtHealthComponent(float value, int maxValue) implements IBetterLookAtComponent { }
