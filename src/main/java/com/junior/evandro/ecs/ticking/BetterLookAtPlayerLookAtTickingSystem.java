package com.junior.evandro.ecs.ticking;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.ecs.data.requests.BetterLookAtHiddenDataRequest;
import com.junior.evandro.ecs.data.requests.BetterLookAtShowDetailedDataRequest;
import com.junior.evandro.mappers.BetterLookAtMapper;
import com.junior.evandro.ecs.BetterLookAtWorld;
import com.junior.evandro.ecs.IBetterLookAtComponent;

import javax.annotation.Nonnull;
import java.util.List;

public class BetterLookAtPlayerLookAtTickingSystem extends EntityTickingSystem<EntityStore> {
    private static final double MAX_DISTANCE = 5.0;

    @Nonnull
    private final Query<EntityStore> query;
    @Nonnull
    private final BetterLookAtWorld betterLookAtWorld;

    public BetterLookAtPlayerLookAtTickingSystem(
        @Nonnull
        BetterLookAtWorld betterLookAtWorld,
        @Nonnull
        ComponentType<EntityStore, Player> playerComponentType
    ) {
        super();
        this.query = Query.and(playerComponentType);
        this.betterLookAtWorld = betterLookAtWorld;
    }

    @Override
    public void tick(
        float dt,
        int index,
        @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        @Nonnull Store<EntityStore> store,
        @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        var storeRef = archetypeChunk.getReferenceTo(index);

        this.handleWithDetailedDataInteraction(storeRef, commandBuffer);

        var targetEntityRef = TargetUtil.getTargetEntity(storeRef, (float) (BetterLookAtPlayerLookAtTickingSystem.MAX_DISTANCE * 1.20), commandBuffer);

        if (targetEntityRef != null) {
            BetterLookAtMapper
                .fromEntity(storeRef, targetEntityRef)
                .thenAccept((components) ->
                    this.handleWithTargetDataComponents(storeRef, components));
            return;
        }

        var targetBlockPosition = TargetUtil.getTargetBlock(storeRef, BetterLookAtPlayerLookAtTickingSystem.MAX_DISTANCE, commandBuffer);

        if (targetBlockPosition == null) {
            this.betterLookAtWorld.registerComponent(BetterLookAt.dataEntityId, new BetterLookAtHiddenDataRequest());
        }

        BetterLookAtMapper
            .fromBlock(storeRef, targetBlockPosition)
            .thenAccept((components) ->
                this.handleWithTargetDataComponents(storeRef, components));
    }

    private void handleWithTargetDataComponents(Ref<EntityStore> storeRef, List<IBetterLookAtComponent> targetDataComponents) {
        for (var targetDataComponent : targetDataComponents) {
            this.betterLookAtWorld.registerComponent(BetterLookAt.dataEntityId, targetDataComponent);
        }

        this.betterLookAtWorld.execute(storeRef);

        for (var targetDataComponent : targetDataComponents) {
            this.betterLookAtWorld.unregisterComponent(BetterLookAt.dataEntityId, targetDataComponent);
        }
    }

    private void handleWithDetailedDataInteraction(
        @Nonnull Ref<EntityStore> storeRef,
        @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        var movementStatesComponent = commandBuffer.getComponent(storeRef, MovementStatesComponent.getComponentType());

        if (movementStatesComponent == null) {
            return;
        }

        var movementStates = movementStatesComponent.getMovementStates();

        if (movementStates.walking) {
            this.betterLookAtWorld.registerComponent(BetterLookAt.dataEntityId, new BetterLookAtShowDetailedDataRequest());
        }
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return this.query;
    }
}
