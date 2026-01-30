package com.junior.evandro.mappers;

import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.blockhealth.BlockHealthModule;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.meta.BlockState;
import com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.FillerBlockUtil;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.junior.evandro.ecs.IBetterLookAtComponent;
import com.junior.evandro.ecs.data.components.*;
import com.junior.evandro.ecs.ticking.BetterLookAtPlayerLookAtTickingSystem;
import com.junior.evandro.utils.BetterLookAtBlockUtils;
import com.junior.evandro.utils.BetterLookAtDataInteraction;
import com.junior.evandro.utils.BetterLookAtItemUtils;
import com.junior.evandro.utils.BetterLookAtMovementStateUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BetterLookAtBlockMapper {
    public static List<IBetterLookAtComponent> from(
        int index,
        @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        var storeRef = archetypeChunk.getReferenceTo(index);
        var targetDataComponents = new ArrayList<IBetterLookAtComponent>();

        if (!storeRef.isValid()) {
            return targetDataComponents;
        }

        var targetBlockPosition = TargetUtil.getTargetBlock(storeRef, BetterLookAtPlayerLookAtTickingSystem.MAX_DISTANCE, commandBuffer);

        if (targetBlockPosition == null) {
            return targetDataComponents;
        }

        var store = storeRef.getStore();
        var world = store.getExternalData().getWorld();
        var worldChunk = world.getChunkIfInMemory(ChunkUtil.indexChunkFromBlock(targetBlockPosition.x, targetBlockPosition.z));

        if (worldChunk == null) {
            return targetDataComponents;
        }

        @SuppressWarnings("removal")
        var targetBlockFilter = worldChunk.getFiller(targetBlockPosition.x, targetBlockPosition.y, targetBlockPosition.z);
        var targetBaseBlockPosition = targetBlockPosition;

        if (targetBlockFilter != 0) {
            var newX = targetBlockPosition.x - FillerBlockUtil.unpackX(targetBlockFilter);
            var newY = targetBlockPosition.y - FillerBlockUtil.unpackY(targetBlockFilter);
            var newZ = targetBlockPosition.z - FillerBlockUtil.unpackZ(targetBlockFilter);
            targetBaseBlockPosition = new Vector3i(newX, newY, newZ);
        }

        var targetBlockType = worldChunk.getBlockType(targetBaseBlockPosition.x, targetBaseBlockPosition.y, targetBaseBlockPosition.z);
        var targetBlockState = worldChunk.getState(targetBaseBlockPosition.x, targetBaseBlockPosition.y, targetBaseBlockPosition.z);

        if (targetBlockType == null) {
            return targetDataComponents;
        }

        var item = targetBlockType.getItem();
        var recommendedTools = BetterLookAtItemUtils.getRecommendedTools(targetBlockType);

        if (item == null) {
            return targetDataComponents;
        }


        BetterLookAtBlockMapper.handleIcon(item, targetDataComponents);
        BetterLookAtBlockMapper.handleChest(targetBlockState, targetDataComponents);
        BetterLookAtBlockMapper.handleTitle(item, targetDataComponents);
        BetterLookAtBlockMapper.handleHealth(world, targetBaseBlockPosition, targetDataComponents);
        BetterLookAtBlockMapper.handlePlugin(item, targetDataComponents);
        BetterLookAtBlockMapper.handleRecommendedTools(recommendedTools, targetDataComponents);
        BetterLookAtBlockMapper.handleProcessingBenchState(targetBlockState, targetDataComponents);

        var dataInteraction = BetterLookAtMovementStateUtil.toDataInteraction(storeRef, commandBuffer);

        if (dataInteraction.equals(BetterLookAtDataInteraction.SHOW_DETAILED_DATA)) {
            BetterLookAtBlockMapper.handleFuel(item, targetDataComponents);
            BetterLookAtBlockMapper.handleConsumable(item, targetDataComponents);
        }

        return targetDataComponents;
    }

    private static void handleFuel(@Nonnull Item item, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        targetDataComponents.add(new BetterLookAtFuelComponent(item.getFuelQuality()));
    }

    private static void handleIcon(@Nonnull Item item, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        targetDataComponents.add(new BetterLookAtBlockIconComponent(item.getId()));
    }

    public static void handleChest(
        @SuppressWarnings("removal") BlockState targetBlockState,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (targetBlockState instanceof ItemContainerState itemContainerState) {
            var chest = itemContainerState.getItemContainer();

            var chestItems = new ArrayList<ItemStack>();

            for (var item : chest.toProtocolMap().values()) {
                var matchedChestItemIndex = -1;

                for (var i = 0; i < chestItems.size(); i++) {
                    if (chestItems.get(i).getItemId().equals(item.itemId)) {
                        matchedChestItemIndex = i;
                        break;
                    }
                }

                if (matchedChestItemIndex > -1) {
                    var matchedChestItem = chestItems.get(matchedChestItemIndex);
                    chestItems.set(matchedChestItemIndex, matchedChestItem.withQuantity(matchedChestItem.getQuantity() + item.quantity));
                    continue;
                }

                chestItems.add(new ItemStack(item.itemId, item.quantity));
            }

            if (chestItems.isEmpty()) {
                return;
            }

            targetDataComponents.add(new BetterLookAtChestComponent(chestItems));
        }
    }

    private static void handleTitle(@Nonnull Item item, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        targetDataComponents.add(new BetterLookAtTitleComponent(Message.translation(item.getTranslationKey())));
    }

    private static void handleHealth(
        @Nonnull World world,
        @Nonnull Vector3i targetBlockPosition,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        // TODO(evandro): found way to verify flowers and pickable things

        var chunkIndex = ChunkUtil.indexChunkFromBlock(targetBlockPosition.x, targetBlockPosition.z);
        var chunkStore = world.getChunkStore().getStore();
        var chunkStoreRef = chunkStore.getExternalData().getChunkReference(chunkIndex);

        if (chunkStoreRef == null) {
            return;
        }

        var blockHealthComponent = chunkStore.getComponent(chunkStoreRef, BlockHealthModule.get().getBlockHealthChunkComponentType());

        if (blockHealthComponent == null) {
            return;
        }

        var health = blockHealthComponent.getBlockHealth(targetBlockPosition);

        targetDataComponents.add(new BetterLookAtHealthComponent(health, 1));
    }

    private static void handlePlugin(@Nonnull Item item, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        var id = item.getId();
        var plugin = BetterLookAtBlockUtils.getPlugin(id);

        if (plugin != null) {
            targetDataComponents.add(new BetterLookAtPluginComponent(plugin));
        }
    }

    private static void handleConsumable(@Nonnull Item item, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        targetDataComponents.add(new BetterLookAtConsumableComponent(item.isConsumable()));
    }

    private static void handleRecommendedTools(@Nonnull List<String> recommendedTools, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        var tools = recommendedTools.stream().map(ItemStack::new).toList();

        if (tools.isEmpty()) {
            return;
        }

        targetDataComponents.add(new BetterLookAtRecommendedToolsComponent(tools));
    }

    private static void handleProcessingBenchState(
        @SuppressWarnings("removal") BlockState targetBlockState,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (targetBlockState instanceof ProcessingBenchState processingBenchState) {
            var recipe = processingBenchState.getRecipe();
            var inputProgress = processingBenchState.getInputProgress();
            var maxInputProgress = recipe != null ? recipe.getTimeSeconds() : 0.0;

            var bench = processingBenchState.getBench();

            if (bench == null) {
                return;
            }

            var benchTierLevel = bench.getTierLevel(processingBenchState.getTierLevel());
            var craftingTimeReductionModifier = benchTierLevel != null ? benchTierLevel.getCraftingTimeReductionModifier() : 0.0;

            var realMaxInputProgress = (float) (maxInputProgress * (1.0F - craftingTimeReductionModifier));

            if (realMaxInputProgress < inputProgress || realMaxInputProgress == 0) {
                return;
            }

            targetDataComponents.add(new BetterLookAtProcessingBenchStateComponent(inputProgress, realMaxInputProgress));
        }
    }
}
