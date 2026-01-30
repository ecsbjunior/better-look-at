package com.junior.evandro.ecs;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public interface IBetterLookAtSystem {
    void execute(int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, BetterLookAtWorld worldSystem);
}
