package com.junior.evandro.mappers;

import com.hypixel.hytale.builtin.adventure.farming.config.modifiers.FertilizerGrowthModifierAsset;
import com.hypixel.hytale.builtin.adventure.farming.config.modifiers.LightLevelGrowthModifierAsset;
import com.hypixel.hytale.builtin.adventure.farming.config.modifiers.WaterGrowthModifierAsset;
import com.hypixel.hytale.builtin.adventure.farming.states.FarmingBlock;
import com.hypixel.hytale.builtin.adventure.farming.states.TilledSoilBlock;
import com.hypixel.hytale.math.util.HashUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.Range;
import com.hypixel.hytale.protocol.Rangef;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.farming.FarmingData;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.farming.FarmingStageData;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.farming.GrowthModifierAsset;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.junior.evandro.ecs.IBetterLookAtComponent;
import com.junior.evandro.ecs.data.BetterLookAtFarmingGrowthStatus;
import com.junior.evandro.ecs.data.components.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BetterLookAtFarmingMapper {
    public static void handleRemainingTime(
        long elapsedTime,
        long stageDurationTime,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        targetDataComponents.add(
            new BetterLookAtFarmingRemainingTimeComponent(stageDurationTime - elapsedTime));
    }

    public static void handleFarmingStages(
        int currentStageIndex,
        long totalTimeInSeconds,
        long elapsedTimeInSeconds,
        @Nonnull FarmingStageData[] stages,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        var index = 0;
        var stagesComponent = new ArrayList<BetterLookAtFarmingStageComponent>();

        // Concluded States
        while (index < currentStageIndex) {
            stagesComponent.add(new BetterLookAtFarmingStageComponent(1, 1));
            index++;
        }

        var currentStage = new BetterLookAtFarmingStageComponent(elapsedTimeInSeconds, totalTimeInSeconds);
        stagesComponent.add(currentStage);
        targetDataComponents.add(currentStage);

        // Waiting Stages
        while (++index < stages.length - 1) {
            stagesComponent.add(new BetterLookAtFarmingStageComponent(0, 1));
        }

        targetDataComponents.add(new BetterLookAtFarmingStagesComponent(stagesComponent));
    }

    public static void handleFarmingGrowthStatus(
        @Nonnull WorldChunk worldChunk,
        WorldTimeResource worldTimeResource,
        Vector3i blockPosition,
        FarmingData farmingData,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        var growthModifiers = farmingData.getGrowthModifiers();

        if (growthModifiers == null) {
            return;
        }

        for (var growthModifier : growthModifiers) {
            var growthModifierAsset = GrowthModifierAsset.getAssetMap().getAsset(growthModifier);

            if (growthModifierAsset == null) {
                continue;
            }

            switch (growthModifierAsset) {
                case WaterGrowthModifierAsset water -> {
                    var hasWater = BetterLookAtFarmingMapper.hasWater(worldChunk, blockPosition, worldTimeResource);
                    var growthStatus = hasWater ? BetterLookAtFarmingGrowthStatus.Ok : BetterLookAtFarmingGrowthStatus.Missing;
                    targetDataComponents.add(new BetterLookAtFarmingWaterStatusComponent(growthStatus, water.getModifier()));
                }
                case FertilizerGrowthModifierAsset fertilizer -> {
                    var hasFertilizer = BetterLookAtFarmingMapper.hasFertilizer(worldChunk, blockPosition);
                    var growthStatus = hasFertilizer ? BetterLookAtFarmingGrowthStatus.Ok : BetterLookAtFarmingGrowthStatus.Missing;
                    targetDataComponents.add(new BetterLookAtFarmingFertilizerStatusComponent(growthStatus, fertilizer.getModifier()));
                }
                case LightLevelGrowthModifierAsset lightLevel -> {
                    var hasLight = BetterLookAtFarmingMapper.hasLight(worldChunk, blockPosition, worldTimeResource, lightLevel);
                    var growthStatus = hasLight ? BetterLookAtFarmingGrowthStatus.Ok : BetterLookAtFarmingGrowthStatus.Missing;
                    targetDataComponents.add(new BetterLookAtFarmingLightStatusComponent(growthStatus, lightLevel.getModifier()));
                }
                default -> {

                }
            }
        }
    }

    public static void handleFarmingCurrentStageInfo(
        int stage,
        int maxStage,
        long elapsedTime,
        long stageDurationTime,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        targetDataComponents.add(
            new BetterLookAtFarmingCurrentStageInfoComponent(stage, maxStage, elapsedTime, stageDurationTime));
    }

    public static long getElapsedTime(
        WorldTimeResource worldTimeResource,
        @Nonnull FarmingBlock farmingBlock
    ) {
        return worldTimeResource.getGameTime().getEpochSecond() - farmingBlock.getLastTickGameTime().getEpochSecond();
    }

    public static long getStageDurationTime(
        float growthProgress,
        Vector3i blockPosition,
        FarmingData farmingData,
        Rangef stageDurationRange,
        WorldTimeResource worldTimeResource,
        @Nonnull WorldChunk worldChunk,
        @Nonnull FarmingBlock farmingBlock
    ) {
        var random = HashUtil.random(farmingBlock.getGeneration(), blockPosition.x, blockPosition.y, blockPosition.z);
        var baseTotalDurationTime = stageDurationRange.min + (stageDurationRange.max - stageDurationRange.min) * random;
        var baseStageDurationTime = Math.round(baseTotalDurationTime * (1.0 - growthProgress % 1.0));
        var growthMultiplier =
            BetterLookAtFarmingMapper.getGrowthModifier(worldChunk, worldTimeResource, blockPosition, farmingData);
        return Math.round(baseStageDurationTime / growthMultiplier);
    }

    private static double getGrowthModifier(
        @Nonnull WorldChunk worldChunk,
        WorldTimeResource worldTimeResource,
        Vector3i blockPosition,
        FarmingData farmingData
    ) {
        var growthModifiers = farmingData.getGrowthModifiers();
        var growthMultiplier = 1.0;

        if (growthModifiers == null) {
            return growthMultiplier;
        }

        for (var growthModifier : growthModifiers) {
            var growthModifierAsset = GrowthModifierAsset.getAssetMap().getAsset(growthModifier);

            growthMultiplier *= handleGrowthModifierAsset(
                worldChunk, blockPosition,
                worldTimeResource, growthModifierAsset
            );
        }

        return growthMultiplier;
    }

    private static double handleGrowthModifierAsset(
        WorldChunk worldChunk,
        Vector3i blockPosition,
        WorldTimeResource worldTimeResource,
        GrowthModifierAsset growthModifierAsset
    ) {
        return switch (growthModifierAsset) {
            case WaterGrowthModifierAsset waterGrowthModifier ->
                handleWaterGrowthModifier(worldChunk, blockPosition, worldTimeResource, waterGrowthModifier);
            case FertilizerGrowthModifierAsset fertilizerGrowthModifier ->
                handleFertilizerGrowthModifier(worldChunk, blockPosition, fertilizerGrowthModifier);
            case LightLevelGrowthModifierAsset lightLevelGrowthModifier ->
                handleLightLevelGrowthModifier(worldChunk, blockPosition, worldTimeResource, lightLevelGrowthModifier);
            case null, default -> 1.0;
        };
    }

    private static double handleWaterGrowthModifier(
        WorldChunk worldChunk,
        Vector3i blockPosition,
        WorldTimeResource worldTimeResource,
        WaterGrowthModifierAsset waterGrowthModifier
    ) {
        var modifier = 1.0;

        if (BetterLookAtFarmingMapper.hasWater(worldChunk, blockPosition, worldTimeResource)) {
            modifier = waterGrowthModifier.getModifier();
        }

        return modifier;
    }

    private static boolean hasWater(
        WorldChunk worldChunk,
        Vector3i blockPosition,
        WorldTimeResource worldTimeResource
    ) {
        var soil = BetterLookAtFarmingMapper.getSoil(worldChunk, blockPosition);

        if (soil == null) {
            return false;
        }

        var gameTime = worldTimeResource.getGameTime();
        var isSoilWatered = soil.getWateredUntil() != null && gameTime.isBefore(soil.getWateredUntil());

        return soil.hasExternalWater() || isSoilWatered;
    }

    private static double handleFertilizerGrowthModifier(
        WorldChunk worldChunk,
        Vector3i blockPosition,
        FertilizerGrowthModifierAsset fertilizerGrowthModifier
    ) {
        var modifier = 1.0;

        if (BetterLookAtFarmingMapper.hasFertilizer(worldChunk, blockPosition)) {
            modifier = fertilizerGrowthModifier.getModifier();
        }

        return modifier;
    }

    private static boolean hasFertilizer(
        WorldChunk worldChunk,
        Vector3i blockPosition
    ) {
        var soil = BetterLookAtFarmingMapper.getSoil(worldChunk, blockPosition);

        return soil != null && soil.isFertilized();
    }

    private static TilledSoilBlock getSoil(WorldChunk worldChunk, Vector3i blockPosition) {
        var chunkStoreRef = worldChunk.getBlockComponentEntity(blockPosition.x, blockPosition.y - 1, blockPosition.z);

        if (chunkStoreRef == null) {
            return null;
        }

        return chunkStoreRef.getStore().getComponent(chunkStoreRef, TilledSoilBlock.getComponentType());
    }

    private static double handleLightLevelGrowthModifier(
        WorldChunk worldChunk,
        Vector3i blockPosition,
        WorldTimeResource worldTimeResource,
        LightLevelGrowthModifierAsset lightLevelGrowthModifier
    ) {
        var modifier = 1.0;

        if (BetterLookAtFarmingMapper.hasLight(worldChunk, blockPosition, worldTimeResource, lightLevelGrowthModifier)) {
            modifier = lightLevelGrowthModifier.getModifier();
        }

        return modifier;
    }

    private static boolean hasLight(
        WorldChunk worldChunk,
        Vector3i blockPosition,
        WorldTimeResource worldTimeResource,
        LightLevelGrowthModifierAsset lightLevelGrowthModifier
    ) {
        var blockChunk = worldChunk.getBlockChunk();

        if (blockChunk == null) {
            return false;
        }

        var skyLight = blockChunk.getSkyLight(blockPosition.x, blockPosition.y, blockPosition.z);

        var redLight = blockChunk.getRedBlockLight(blockPosition.x, blockPosition.y, blockPosition.z);
        var greenLight = blockChunk.getGreenBlockLight(blockPosition.x, blockPosition.y, blockPosition.z);
        var blueLight = blockChunk.getBlueBlockLight(blockPosition.x, blockPosition.y, blockPosition.z);

        var hasSunLight = hasSunLight(skyLight, worldTimeResource, lightLevelGrowthModifier);
        var hasArtificialLight = hasArtificialLight(redLight, greenLight, blueLight, lightLevelGrowthModifier);

        return hasSunLight || hasArtificialLight;
    }

    private static boolean hasSunLight(
        byte skyLight,
        WorldTimeResource worldTimeResource,
        LightLevelGrowthModifierAsset lightLevelGrowthModifier
    ) {
        var range = lightLevelGrowthModifier.getSunlight();
        var sunlightFactor = worldTimeResource.getSunlightFactor();
        var daylight = sunlightFactor * skyLight;
        return isInRange(range, daylight);
    }

    private static boolean hasArtificialLight(
        byte redLight,
        byte greenLight,
        byte blueLight,
        LightLevelGrowthModifierAsset lightLevelGrowthModifier
    ) {
        var artificialLight = lightLevelGrowthModifier.getArtificialLight();
        var redRange = artificialLight.getRed();
        var greenRange = artificialLight.getGreen();
        var blueRange = artificialLight.getBlue();
        return isInRange(redRange, redLight) && isInRange(greenRange, greenLight) && isInRange(blueRange, blueLight);
    }

    private static boolean isInRange(@Nonnull Range range, int value) {
        return range.min <= value && value <= range.max;
    }

    private static boolean isInRange(@Nonnull Rangef range, double value) {
        return range.min <= value && value <= range.max;
    }
}
