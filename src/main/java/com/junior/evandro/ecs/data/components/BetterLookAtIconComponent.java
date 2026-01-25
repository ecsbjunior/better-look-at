package com.junior.evandro.ecs.data.components;

import com.junior.evandro.ecs.IBetterLookAtComponent;

import javax.annotation.Nonnull;

public record BetterLookAtIconComponent(@Nonnull String value) implements IBetterLookAtComponent { }
