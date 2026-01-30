package com.junior.evandro.ecs.ticking;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.mappers.BetterLookAtBlockMapper;
import com.junior.evandro.mappers.BetterLookAtEntityMapper;
import com.junior.evandro.ecs.IBetterLookAtComponent;

import javax.annotation.Nonnull;
import java.util.List;

public class BetterLookAtPlayerLookAtTickingSystem extends EntityTickingSystem<EntityStore> {
    public static final double MAX_DISTANCE = 5.0;

    @Nonnull
    private final ComponentType<EntityStore, PlayerRef> PLAYER_REF_COMPONENT_TYPE = PlayerRef.getComponentType();

    public BetterLookAtPlayerLookAtTickingSystem() {
        super();
    }

    @Override
    public void tick(
        float dt,
        int index,
        @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        @Nonnull Store<EntityStore> store,
        @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        List<IBetterLookAtComponent> targetDataComponents;

        targetDataComponents = BetterLookAtEntityMapper.from(index, archetypeChunk, commandBuffer);

        if (targetDataComponents.isEmpty()) {
            targetDataComponents = BetterLookAtBlockMapper.from(index, archetypeChunk, commandBuffer);
        }

        for (var targetDataComponent : targetDataComponents) {
            BetterLookAt.WORLD.registerComponent(BetterLookAt.ENTITY_ID, targetDataComponent);
        }

        BetterLookAt.WORLD.execute(index, archetypeChunk);

        for (var targetDataComponent : targetDataComponents) {
            BetterLookAt.WORLD.unregisterComponent(BetterLookAt.ENTITY_ID, targetDataComponent.getClass());
        }
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return this.PLAYER_REF_COMPONENT_TYPE;
    }
}
