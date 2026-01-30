package com.junior.evandro.utils;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class BetterLookAtMovementStateUtil {
    public static BetterLookAtDataInteraction toDataInteraction(
        @Nonnull Ref<EntityStore> storeRef,
        @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        var movementStatesComponent = commandBuffer.getComponent(storeRef, MovementStatesComponent.getComponentType());

        if (movementStatesComponent == null) {
            return BetterLookAtDataInteraction.NONE;
        }

        var movementStates = movementStatesComponent.getMovementStates();

        if (movementStates.walking) {
            return BetterLookAtDataInteraction.SHOW_DETAILED_DATA;
        }

        return BetterLookAtDataInteraction.NONE;
    }
}
