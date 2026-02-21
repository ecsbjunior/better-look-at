package com.junior.evandro.mappers;

import com.hypixel.hytale.builtin.adventure.farming.states.FarmingBlock;
import com.hypixel.hytale.builtin.crafting.state.BenchState;
import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.farming.FarmingData;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.farming.FarmingStageData;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.meta.BlockState;
import com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.FillerBlockUtil;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.junior.evandro.ecs.ticking.BetterLookAtPlayerLookAtTickingSystem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@SuppressWarnings("removal")
public class BetterLookAtBlockContext {
    @Nonnull public final World world;
    @Nonnull public final Store<EntityStore> store;
    @Nonnull public final Ref<EntityStore> storeRef;
    @Nonnull public final WorldChunk worldChunk;
    @Nonnull public final Vector3i blockPosition;
    @Nonnull public final Vector3i baseBlockPosition;
    @Nonnull public final WorldTimeResource worldTimeResource;
    @Nonnull public final BlockType blockType;
    @Nullable public final BlockState blockState;
    @Nullable public final Ref<ChunkStore> chunkStoreRef;
    @Nullable public final Store<ChunkStore> chunkStore;
    @Nullable public final Item item;
    @Nullable public final ItemContainerState itemContainerState;
    @Nullable public final Bench bench;
    @Nullable public final Farming farming;

    private BetterLookAtBlockContext(
        @Nonnull World world,
        @Nonnull Store<EntityStore> store,
        @Nonnull Ref<EntityStore> storeRef,
        @Nonnull WorldChunk worldChunk,
        @Nonnull Vector3i blockPosition,
        @Nonnull Vector3i baseBlockPosition,
        @Nonnull WorldTimeResource worldTimeResource,
        @Nonnull BlockType blockType,
        @Nullable BlockState blockState,
        @Nullable Ref<ChunkStore> chunkStoreRef,
        @Nullable Store<ChunkStore> chunkStore,
        @Nullable Item item,
        @Nullable Bench bench,
        @Nullable Farming farming,
        @Nullable ItemContainerState itemContainerState
    ) {
        this.world = world;
        this.store = store;
        this.storeRef = storeRef;
        this.worldChunk = worldChunk;
        this.blockPosition = blockPosition;
        this.baseBlockPosition = baseBlockPosition;
        this.worldTimeResource = worldTimeResource;
        this.blockType = blockType;
        this.blockState = blockState;
        this.chunkStoreRef = chunkStoreRef;
        this.chunkStore = chunkStore;
        this.item = item;
        this.bench = bench;
        this.farming = farming;
        this.itemContainerState = itemContainerState;
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
        var world = store.getExternalData().getWorld();
        var worldChunk = world.getChunkIfInMemory(ChunkUtil.indexChunkFromBlock(blockPosition.x, blockPosition.z));

        if (worldChunk == null) {
            return null;
        }

        var chunkRef = worldChunk.getBlockComponentEntity(blockPosition.x, blockPosition.y, blockPosition.z);
        var chunkStore = chunkRef != null ? chunkRef.getStore() : null;
        var worldTimeResource = world.getEntityStore().getStore().getResource(WorldTimeResource.getResourceType());

        @SuppressWarnings("removal")
        var blockFiller = worldChunk.getFiller(blockPosition.x, blockPosition.y, blockPosition.z);
        var baseBlockPosition = blockPosition.clone();

        if (blockFiller != 0) {
            var newX = blockPosition.x - FillerBlockUtil.unpackX(blockFiller);
            var newY = blockPosition.y - FillerBlockUtil.unpackY(blockFiller);
            var newZ = blockPosition.z - FillerBlockUtil.unpackZ(blockFiller);
            baseBlockPosition = new Vector3i(newX, newY, newZ);
        }

        var blockType = worldChunk.getBlockType(baseBlockPosition.x, baseBlockPosition.y, baseBlockPosition.z);

        if (blockType == null) {
            return null;
        }

        var blockState = worldChunk.getState(baseBlockPosition.x, baseBlockPosition.y, baseBlockPosition.z);

        var item = blockType.getItem();


        ItemContainerState itemContainerState = null;
        if (blockState instanceof ItemContainerState) {
            itemContainerState = (ItemContainerState) blockState;
        }

        return new BetterLookAtBlockContext(
            world,
            store,
            storeRef,
            worldChunk,
            blockPosition,
            baseBlockPosition,
            worldTimeResource,
            blockType,
            blockState,
            chunkRef,
            chunkStore,
            item,
            Bench.create(blockState),
            Farming.create(chunkRef, blockType),
            itemContainerState
        );
    }

    public record Bench(@Nonnull BenchState state, @Nonnull ProcessingBenchState processingState) {
        @Nullable
        private static Bench create(@Nullable BlockState blockState) {
            if (blockState == null) {
                return null;
            }

            BenchState state = null;
            if (blockState instanceof BenchState) {
                state = (BenchState) blockState;
            }

            if (state == null) {
                return null;
            }

            ProcessingBenchState processingState = null;
            if (blockState instanceof ProcessingBenchState) {
                processingState = (ProcessingBenchState) blockState;
            }

            if (processingState == null) {
                return null;
            }

            return new Bench(
                state,
                processingState
            );
        }
    }

    public record Farming(
        float growthProgress,
        int growthProgressIndex,
        @Nonnull FarmingData data,
        @Nonnull FarmingBlock block,
        @Nonnull String stageType,
        @Nonnull FarmingStageData[] stages,
        @Nonnull FarmingStageData currentStage,
        @Nonnull Map<String, FarmingStageData[]> stagesByStageType
    ) {
        @Nullable
        private static Farming create(Ref<ChunkStore> chunkRef, @Nonnull BlockType blockType) {
            if (chunkRef == null) {
                return null;
            }

            var data = blockType.getFarming();

            if (data == null) {
                return null;
            }

            var stagesByStageType = data.getStages();

            if (stagesByStageType == null) {
                return null;
            }

            var store = chunkRef.getStore();
            var block = store.getComponent(chunkRef, FarmingBlock.getComponentType());

            if (block == null) {
                return null;
            }

            var growthProgress = block.getGrowthProgress();
            var growthProgressIndex = (int) growthProgress;

            if (growthProgressIndex < 0) {
                return null;
            }

            var stageType = block.getCurrentStageSet();

            if (stageType == null) {
                return null;
            }

            var stages = stagesByStageType.get(stageType);

            if (stages == null) {
                return null;
            }

            var currentStage = stages[growthProgressIndex];

            if (currentStage == null) {
                return null;
            }

            return new Farming(
                growthProgress,
                growthProgressIndex,
                data,
                block,
                stageType,
                stages,
                currentStage,
                stagesByStageType
            );
        }
    }
}
