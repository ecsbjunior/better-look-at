package com.junior.evandro.ecs.data.components;

import com.hypixel.hytale.server.core.Message;
import com.junior.evandro.ecs.IBetterLookAtComponent;

import javax.annotation.Nonnull;

public record BetterLookAtTitleComponent(@Nonnull Message value) implements IBetterLookAtComponent { }
