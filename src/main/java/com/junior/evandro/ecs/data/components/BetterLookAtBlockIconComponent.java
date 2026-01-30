package com.junior.evandro.ecs.data.components;

import com.junior.evandro.ecs.IBetterLookAtComponent;

import javax.annotation.Nonnull;

public record BetterLookAtBlockIconComponent(@Nonnull String value) implements IBetterLookAtComponent { }
