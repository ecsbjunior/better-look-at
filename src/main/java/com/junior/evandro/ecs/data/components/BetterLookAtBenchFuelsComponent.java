package com.junior.evandro.ecs.data.components;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.junior.evandro.ecs.IBetterLookAtComponent;

import java.util.List;

public record BetterLookAtBenchFuelsComponent(List<ItemStack> items) implements IBetterLookAtComponent { }
