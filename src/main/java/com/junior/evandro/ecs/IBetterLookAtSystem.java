package com.junior.evandro.ecs;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public interface IBetterLookAtSystem {
    void execute(Ref<EntityStore> storeRef, BetterLookAtWorld worldSystem);
}
