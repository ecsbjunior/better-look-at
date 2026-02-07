package com.junior.evandro.ecs.data.components;

import com.junior.evandro.ecs.IBetterLookAtComponent;

import java.util.List;

public record BetterLookAtFarmingStagesComponent(List<BetterLookAtFarmingStageComponent> stages) implements IBetterLookAtComponent { }
