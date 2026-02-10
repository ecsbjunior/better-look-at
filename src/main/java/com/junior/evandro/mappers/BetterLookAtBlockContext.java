package com.junior.evandro.mappers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.junior.evandro.ecs.ticking.BetterLookAtPlayerLookAtTickingSystem;

public class BetterLookAtBlockContext {
    @Nonnull public final World world;
    @Nonnull public final Store<EntityStore> store;
    @Nonnull public final Ref<EntityStore> storeRef;
    @Nonnull public final WorldChunk worldChunk;
    @Nonnull public final Vector3i blockPosition;
    @Nonnull public final WorldTimeResource worldTimeResource;

    private BetterLookAtBlockContext(
        @Nonnull World world,
        @Nonnull Store<EntityStore> store,
        @Nonnull Ref<EntityStore> storeRef,
        @Nonnull WorldChunk worldChunk,
        @Nonnull Vector3i blockPosition,
        @Nonnull WorldTimeResource worldTimeResource
    ) {
        this.world = world;
        this.store = store;
        this.storeRef = storeRef;
        this.worldChunk = worldChunk;
        this.blockPosition = blockPosition;
        this.worldTimeResource = worldTimeResource;
    }

    @Nullable
    public static BetterLookAtBlockContext create(
        int index,
        CommandBuffer<EntityStore> commandBuffer,
        ArchetypeChunk<EntityStore> archetypeChunk
    ) {
        var storeRef = archetypeChunk.getReferenceTo(index);

        if (!storeRef.isValid()) {
            return null;
        }

        // TODO(evandro): change location of MAX_DISTANCE
        var blockPosition = TargetUtil.getTargetBlock(storeRef, BetterLookAtPlayerLookAtTickingSystem.MAX_DISTANCE, commandBuffer);

        if (blockPosition == null) {
            return null;
        }

        var store = storeRef.getStore();

        if (store == null) {
            return null;
        }

        var world = store.getExternalData().getWorld();

        if (world == null) {
            return null;
        }

        var worldChunk = world.getChunkIfInMemory(ChunkUtil.indexChunkFromBlock(blockPosition.x, blockPosition.z));

        if (worldChunk == null) {
            return null;
        }

        var worldTimeResource = world.getEntityStore().getStore().getResource(WorldTimeResource.getResourceType());

        if (worldTimeResource == null) {
            return null;
        }

        var blockContext = new BetterLookAtBlockContext(
            world,
            store,
            storeRef,
            worldChunk,
            blockPosition,
            worldTimeResource
        );

        return blockContext;
    }
}
