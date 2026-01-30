package com.junior.evandro.ecs;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class BetterLookAtWorld {
    private int nextEntityId = 0;
    @Nonnull
    private final List<BetterLookAtEntity> entities = new ArrayList<>();
    @Nonnull
    private final List<IBetterLookAtSystem> systems = new ArrayList<>();

    public void execute(int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk) {
        for (var system : this.systems) {
            system.execute(index, archetypeChunk, this);
        }
    }

    public <T extends BetterLookAtEntity> int registerEntity(IntFunction<T> entityFactory) {
        this.entities.add(entityFactory.apply(this.nextEntityId++));
        return this.nextEntityId - 1;
    }

    public void registerComponent(int entityId, IBetterLookAtComponent component) {
        this.entities.get(entityId).addComponent(component);
    }

    public <T extends IBetterLookAtComponent> void unregisterComponent(int entityId, Class<T> component) {
        this.entities.get(entityId).removeComponent(component);
    }

    public <T extends BetterLookAtEntity> @Nonnull List<T> getEntities(Class<T> entity) {
        return this.entities.stream().filter(entity::isInstance).map(entity::cast).toList();
    }

    public void registerSystem(IBetterLookAtSystem system) {
        this.systems.add(system);
    }
}
