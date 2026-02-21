package com.junior.evandro.mappers;

import com.hypixel.hytale.builtin.adventure.farming.config.modifiers.FertilizerGrowthModifierAsset;
import com.hypixel.hytale.builtin.adventure.farming.config.modifiers.LightLevelGrowthModifierAsset;
import com.hypixel.hytale.builtin.adventure.farming.config.modifiers.WaterGrowthModifierAsset;
import com.hypixel.hytale.builtin.adventure.farming.states.TilledSoilBlock;
import com.hypixel.hytale.math.util.HashUtil;
import com.hypixel.hytale.protocol.Range;
import com.hypixel.hytale.protocol.Rangef;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.farming.GrowthModifierAsset;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
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
        @Nonnull BetterLookAtBlockContext context,
        long elapsedTime,
        long stageDurationTime,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (context.farming == null) {
            return;
        }

        var index = 0;
        var stagesComponent = new ArrayList<BetterLookAtFarmingStageComponent>();

        // Concluded States
        while (index < context.farming.growthProgressIndex()) {
            stagesComponent.add(new BetterLookAtFarmingStageComponent(1, 1));
            index++;
        }

        var currentStage = new BetterLookAtFarmingStageComponent(elapsedTime, stageDurationTime);
        stagesComponent.add(currentStage);
        targetDataComponents.add(currentStage);

        // Waiting Stages
        while (++index < context.farming.stages().length - 1) {
            stagesComponent.add(new BetterLookAtFarmingStageComponent(0, 1));
        }

        targetDataComponents.add(new BetterLookAtFarmingStagesComponent(stagesComponent));
    }

    public static void handleFarmingGrowthStatus(
        @Nonnull BetterLookAtBlockContext context,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (context.farming == null) {
            return;
        }

        var growthModifiers = context.farming.data().getGrowthModifiers();

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
                    var hasWater = BetterLookAtFarmingMapper.hasWater(context);
                    var growthStatus = hasWater ? BetterLookAtFarmingGrowthStatus.Ok : BetterLookAtFarmingGrowthStatus.Missing;
                    targetDataComponents.add(new BetterLookAtFarmingWaterStatusComponent(growthStatus, water.getModifier()));
                }
                case FertilizerGrowthModifierAsset fertilizer -> {
                    var hasFertilizer = BetterLookAtFarmingMapper.hasFertilizer(context);
                    var growthStatus = hasFertilizer ? BetterLookAtFarmingGrowthStatus.Ok : BetterLookAtFarmingGrowthStatus.Missing;
                    targetDataComponents.add(new BetterLookAtFarmingFertilizerStatusComponent(growthStatus, fertilizer.getModifier()));
                }
                case LightLevelGrowthModifierAsset lightLevel -> {
                    var hasLight = BetterLookAtFarmingMapper.hasLight(context, lightLevel);
                    var growthStatus = hasLight ? BetterLookAtFarmingGrowthStatus.Ok : BetterLookAtFarmingGrowthStatus.Missing;
                    targetDataComponents.add(new BetterLookAtFarmingLightStatusComponent(growthStatus, lightLevel.getModifier()));
                }
                default -> {

                }
            }
        }
    }

    public static void handleFarmingCurrentStageInfo(
        @Nonnull BetterLookAtBlockContext context,
        long elapsedTime,
        long stageDurationTime,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        if (context.farming == null) {
            return;
        }

        targetDataComponents.add(new BetterLookAtFarmingCurrentStageInfoComponent(
            context.farming.growthProgressIndex() + 1,
            context.farming.stages().length - 1,
            elapsedTime,
            stageDurationTime
        ));
    }

    public static long getElapsedTime(@Nonnull BetterLookAtBlockContext context) {
        if (context.farming == null) {
            return 0;
        }

        return
            context.worldTimeResource.getGameTime().getEpochSecond() -
            context.farming.block().getLastTickGameTime().getEpochSecond();
    }

    public static long getStageDurationTime(@Nonnull BetterLookAtBlockContext context) {
        if (context.farming == null) {
            return 0;
        }

        var stageDuration = context.farming.currentStage().getDuration();

        if (stageDuration == null) {
            return 0;
        }

        var random = HashUtil.random(context.farming.block().getGeneration(), context.blockPosition.x, context.blockPosition.y, context.blockPosition.z);
        var baseTotalDurationTime = stageDuration.min + (stageDuration.max - stageDuration.min) * random;
        var baseStageDurationTime = Math.round(baseTotalDurationTime * (1.0 - context.farming.growthProgress() % 1.0));
        var growthMultiplier = BetterLookAtFarmingMapper.getGrowthModifier(context);

        return Math.round(baseStageDurationTime / growthMultiplier);
    }

    private static double getGrowthModifier(@Nonnull BetterLookAtBlockContext context) {
        if (context.farming == null) {
            return 0.0;
        }

        var growthModifiers = context.farming.data().getGrowthModifiers();
        var growthMultiplier = 1.0;

        if (growthModifiers == null) {
            return growthMultiplier;
        }

        for (var growthModifier : growthModifiers) {
            growthMultiplier *= handleGrowthModifierAsset(
                context, GrowthModifierAsset.getAssetMap().getAsset(growthModifier));
        }

        return growthMultiplier;
    }

    private static double handleGrowthModifierAsset(
        @Nonnull BetterLookAtBlockContext context,
        GrowthModifierAsset growthModifierAsset
    ) {
        return switch (growthModifierAsset) {
            case WaterGrowthModifierAsset waterGrowthModifier ->
                handleWaterGrowthModifier(context, waterGrowthModifier);
            case FertilizerGrowthModifierAsset fertilizerGrowthModifier ->
                handleFertilizerGrowthModifier(context, fertilizerGrowthModifier);
            case LightLevelGrowthModifierAsset lightLevelGrowthModifier ->
                handleLightLevelGrowthModifier(context, lightLevelGrowthModifier);
            case null, default -> 1.0;
        };
    }

    private static double handleWaterGrowthModifier(
        @Nonnull BetterLookAtBlockContext context,
        WaterGrowthModifierAsset waterGrowthModifier
    ) {
        var modifier = 1.0;

        if (BetterLookAtFarmingMapper.hasWater(context)) {
            modifier = waterGrowthModifier.getModifier();
        }

        return modifier;
    }

    private static boolean hasWater(@Nonnull BetterLookAtBlockContext context) {
        var soil = BetterLookAtFarmingMapper.getSoil(context);

        if (soil == null) {
            return false;
        }

        var gameTime = context.worldTimeResource.getGameTime();
        var isSoilWatered = soil.getWateredUntil() != null && gameTime.isBefore(soil.getWateredUntil());

        return soil.hasExternalWater() || isSoilWatered;
    }

    private static double handleFertilizerGrowthModifier(
        @Nonnull BetterLookAtBlockContext context,
        FertilizerGrowthModifierAsset fertilizerGrowthModifier
    ) {
        var modifier = 1.0;

        if (BetterLookAtFarmingMapper.hasFertilizer(context)) {
            modifier = fertilizerGrowthModifier.getModifier();
        }

        return modifier;
    }

    private static boolean hasFertilizer(@Nonnull BetterLookAtBlockContext context) {
        var soil = BetterLookAtFarmingMapper.getSoil(context);

        return soil != null && soil.isFertilized();
    }

    private static TilledSoilBlock getSoil(@Nonnull BetterLookAtBlockContext context) {
        var chunkStoreRef = context.worldChunk.getBlockComponentEntity(
            context.blockPosition.x, context.blockPosition.y - 1, context.blockPosition.z);

        if (chunkStoreRef == null) {
            return null;
        }

        return chunkStoreRef.getStore().getComponent(chunkStoreRef, TilledSoilBlock.getComponentType());
    }

    private static double handleLightLevelGrowthModifier(
        @Nonnull BetterLookAtBlockContext context,
        LightLevelGrowthModifierAsset lightLevelGrowthModifier
    ) {
        var modifier = 1.0;

        if (BetterLookAtFarmingMapper.hasLight(context, lightLevelGrowthModifier)) {
            modifier = lightLevelGrowthModifier.getModifier();
        }

        return modifier;
    }

    private static boolean hasLight(
        @Nonnull BetterLookAtBlockContext context,
        LightLevelGrowthModifierAsset lightLevelGrowthModifier
    ) {
        var blockChunk = context.worldChunk.getBlockChunk();

        if (blockChunk == null) {
            return false;
        }

        var skyLight = blockChunk.getSkyLight(context.blockPosition.x, context.blockPosition.y, context.blockPosition.z);

        var redLight = blockChunk.getRedBlockLight(context.blockPosition.x, context.blockPosition.y, context.blockPosition.z);
        var greenLight = blockChunk.getGreenBlockLight(context.blockPosition.x, context.blockPosition.y, context.blockPosition.z);
        var blueLight = blockChunk.getBlueBlockLight(context.blockPosition.x, context.blockPosition.y, context.blockPosition.z);

        var hasSunLight = hasSunLight(skyLight, context.worldTimeResource, lightLevelGrowthModifier);
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
