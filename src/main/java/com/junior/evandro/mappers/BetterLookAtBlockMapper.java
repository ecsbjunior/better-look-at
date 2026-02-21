package com.junior.evandro.mappers;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.blockhealth.BlockHealthModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.ecs.IBetterLookAtComponent;
import com.junior.evandro.ecs.data.components.*;
import com.junior.evandro.utils.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BetterLookAtBlockMapper {
    public static List<IBetterLookAtComponent> from(
        int index,
        @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        var context = BetterLookAtBlockContext.create(index, commandBuffer, archetypeChunk);
        var targetDataComponents = new ArrayList<IBetterLookAtComponent>();

        if (context == null) {
            return targetDataComponents;
        }

        BetterLookAtBlockMapper.handleIcon(context, targetDataComponents);
        BetterLookAtBlockMapper.handleChest(context, targetDataComponents);
        BetterLookAtBlockMapper.handleTitle(context, targetDataComponents);
        BetterLookAtBlockMapper.handleHealth(context, targetDataComponents);
        BetterLookAtBlockMapper.handlePlugin(context, targetDataComponents);
        BetterLookAtBlockMapper.handleFarming(context, targetDataComponents);
        BetterLookAtBlockMapper.handleRecommendedTools(context, targetDataComponents);
        BetterLookAtBlockMapper.handleProcessingBenchState(context, targetDataComponents);
        BetterLookAtBlockMapper.handleBenchContainers(context, targetDataComponents);

        var dataInteraction = BetterLookAtMovementStateUtil.toDataInteraction(context.storeRef, commandBuffer);

        if (dataInteraction.equals(BetterLookAtDataInteraction.SHOW_DETAILED_DATA)) {
            BetterLookAtBlockMapper.handleFuel(context, targetDataComponents);
            BetterLookAtBlockMapper.handleBenchTier(context, targetDataComponents);
            BetterLookAtBlockMapper.handleConsumable(context, targetDataComponents);
        }

        return targetDataComponents;
    }

    private static void handleFuel(
        @Nonnull BetterLookAtBlockContext blockContext,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (blockContext.item != null) {
            targetDataComponents.add(new BetterLookAtFuelComponent(blockContext.item.getFuelQuality()));
        }
    }

    private static void handleIcon(
        @Nonnull BetterLookAtBlockContext blockContext,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (blockContext.item != null) {
            targetDataComponents.add(new BetterLookAtBlockIconComponent(blockContext.item.getId()));
        }
    }

    public static void handleChest(
        @Nonnull BetterLookAtBlockContext blockContext,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (blockContext.itemContainerState == null || !BetterLookAt.CONFIG.get().getShowChest()) {
            return;
        }

        var chest = blockContext.itemContainerState.getItemContainer();

        if (chest == null) {
            return;
        }

        var chestItems = new ArrayList<ItemStack>();

        for (var item : chest.toProtocolMap().values()) {
            var matchedChestItemIndex = -1;

            for (var i = 0; i < chestItems.size(); i++) {
                var chestItem = chestItems.get(i);

                if (chestItem == null) {
                    continue;
                }

                if (chestItem.getItemId().equals(item.itemId)) {
                    matchedChestItemIndex = i;
                    break;
                }
            }

            if (matchedChestItemIndex > -1) {
                var matchedChestItem = chestItems.get(matchedChestItemIndex);

                if (matchedChestItem == null) {
                    continue;
                }

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

    private static void handleTitle(
        @Nonnull BetterLookAtBlockContext blockContext,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (blockContext.item != null) {
            targetDataComponents.add(new BetterLookAtTitleComponent(
                Message.translation(blockContext.item.getTranslationKey())));
        }

    }

    private static void handleHealth(
        @Nonnull BetterLookAtBlockContext blockContext,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (blockContext.chunkStore == null || blockContext.chunkStoreRef == null) {
            return;
        }

        var blockHealthComponent = blockContext.chunkStore.getComponent(
            blockContext.chunkStoreRef, BlockHealthModule.get().getBlockHealthChunkComponentType());

        if (blockHealthComponent == null) {
            return;
        }

        var health = blockHealthComponent.getBlockHealth(blockContext.baseBlockPosition);
        var maxHealth = 1;

        if (health == maxHealth || maxHealth < health) {
            return;
        }

        targetDataComponents.add(new BetterLookAtHealthComponent(health, maxHealth));
    }

    private static void handlePlugin(
        @Nonnull BetterLookAtBlockContext blockContext,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (blockContext.item == null) {
            return;
        }

        var plugin = BetterLookAtLoader.Block.getPlugin(blockContext.item.getId());

        if (plugin != null) {
            targetDataComponents.add(new BetterLookAtPluginComponent(plugin));
        }
    }

    private static void handleFarming(
        @Nonnull BetterLookAtBlockContext blockContext,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (blockContext.farming == null) {
            return;
        }

        var elapsedTime = BetterLookAtFarmingMapper.getElapsedTime(blockContext);
        var stageDurationTime = BetterLookAtFarmingMapper.getStageDurationTime(blockContext);

        BetterLookAtFarmingMapper.handleRemainingTime(elapsedTime, stageDurationTime, targetDataComponents);
        BetterLookAtFarmingMapper.handleFarmingStages(blockContext, elapsedTime, stageDurationTime, targetDataComponents);
        BetterLookAtFarmingMapper.handleFarmingGrowthStatus(blockContext, targetDataComponents);
        BetterLookAtFarmingMapper.handleFarmingCurrentStageInfo(blockContext, elapsedTime, stageDurationTime, targetDataComponents);
    }

    private static void handleConsumable(
        @Nonnull BetterLookAtBlockContext blockContext,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (blockContext.item != null) {
            targetDataComponents.add(new BetterLookAtConsumableComponent(blockContext.item.isConsumable()));
        }
    }

    private static void handleRecommendedTools(
        @Nonnull BetterLookAtBlockContext blockContext,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        var recommendedTools = BetterLookAtLoader.Item
            .getRecommendedTools(blockContext.blockType)
            .stream().map(ItemStack::new)
            .toList();

        if (recommendedTools.isEmpty()) {
            return;
        }

        targetDataComponents.add(new BetterLookAtRecommendedToolsComponent(recommendedTools));
    }

    private static void handleProcessingBenchState(
        @Nonnull BetterLookAtBlockContext blockContext,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (blockContext.bench == null) {
            return;
        }

        var recipe = blockContext.bench.processingState().getRecipe();
        var inputProgress = blockContext.bench.processingState().getInputProgress();
        var maxInputProgress = recipe != null ? recipe.getTimeSeconds() : 0.0;

        var bench = blockContext.bench.processingState().getBench();

        if (bench == null) {
            return;
        }

        var benchTierLevel = bench.getTierLevel(blockContext.bench.processingState().getTierLevel());
        var craftingTimeReductionModifier = benchTierLevel != null ? benchTierLevel.getCraftingTimeReductionModifier() : 0.0;

        var realMaxInputProgress = (float) (maxInputProgress * (1.0F - craftingTimeReductionModifier));

        if (realMaxInputProgress < inputProgress || realMaxInputProgress == 0) {
            return;
        }

        targetDataComponents.add(new BetterLookAtProcessingBenchStateComponent(inputProgress, realMaxInputProgress));
    }

    private static void handleBenchTier(
        @Nonnull BetterLookAtBlockContext blockContext,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (blockContext.bench != null) {
            targetDataComponents.add(new BetterLookAtBenchTierComponent(blockContext.bench.state().getTierLevel()));
        }
    }

    private static void handleBenchContainers(
        @Nonnull BetterLookAtBlockContext blockContext,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (blockContext.bench == null || !BetterLookAt.CONFIG.get().getShowBench()) {
            return;
        }

        var FUEL_INDEX = 0;
        var INPUTS_INDEX = 1;
        var OUTPUTS_INDEX = 2;

        var containers = blockContext.bench.processingState().getItemContainer();

        if (containers == null) {
            return;
        }

        var fuelsContainer = containers.getContainer(FUEL_INDEX);

        if (fuelsContainer != null) {
            var fuels = new ArrayList<ItemStack>();

            for (short index = 0; index < fuelsContainer.getCapacity(); index++) {
                var fuel = fuelsContainer.getItemStack(index);

                if (fuel != null) {
                    fuels.add(BetterLookAtItemStackUtils.shallowClone(fuel));
                }
            }

            if (!fuels.isEmpty()) {
                targetDataComponents.add(new BetterLookAtBenchFuelsComponent(fuels));
            }
        }

        var inputsContainer = containers.getContainer(INPUTS_INDEX);

        if (inputsContainer != null) {
            var inputs = new ArrayList<ItemStack>();

            for (short index = 0; index < inputsContainer.getCapacity(); index++) {
                var input = inputsContainer.getItemStack(index);

                if (input != null) {
                    inputs.add(BetterLookAtItemStackUtils.shallowClone(input));
                }
            }

            if (!inputs.isEmpty()) {
                targetDataComponents.add(new BetterLookAtBenchInputsComponent(inputs));
            }
        }

        var outputsContainer = containers.getContainer(OUTPUTS_INDEX);

        if (outputsContainer != null) {
            var outputs = new ArrayList<ItemStack>();

            for (short index = 0; index < outputsContainer.getCapacity(); index++) {
                var output = outputsContainer.getItemStack(index);

                if (output != null) {
                    outputs.add(BetterLookAtItemStackUtils.shallowClone(output));
                }
            }

            if (!outputs.isEmpty()) {
                targetDataComponents.add(new BetterLookAtBenchOutputsComponent(outputs));
            }
        }
    }
}
