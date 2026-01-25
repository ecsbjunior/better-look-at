package com.junior.evandro.mappers;

import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.modules.blockhealth.BlockHealthModule;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.role.Role;
import com.junior.evandro.ecs.IBetterLookAtComponent;
import com.junior.evandro.ecs.data.components.*;
import com.junior.evandro.utils.BetterLookAtBlockUtils;
import com.junior.evandro.utils.BetterLookAtItemUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BetterLookAtMapper {
    public static CompletableFuture<List<IBetterLookAtComponent>> fromBlock(
        @Nonnull Ref<EntityStore> storeRef,
        Vector3i targetBlockPosition
    ) {
        var store = storeRef.getStore();
        var future = new CompletableFuture<List<IBetterLookAtComponent>>();
        var targetDataComponents = new ArrayList<IBetterLookAtComponent>();

        if (targetBlockPosition == null) {
            future.complete(targetDataComponents);
            return future;
        }

        var world = store.getExternalData().getWorld();

        world.execute(() -> {
            var targetBlockType = world.getBlockType(targetBlockPosition);

            if (targetBlockType == null) {
                return;
            }

            var item = targetBlockType.getItem();
            var recommendedTools = BetterLookAtItemUtils.getRecommendedTools(targetBlockType);

            if (item == null) {
                return;
            }

            BetterLookAtMapper.handleFuel(item, targetDataComponents);
            BetterLookAtMapper.handleIcon(item, targetDataComponents);
            BetterLookAtMapper.handleTitle(item, targetDataComponents);
            BetterLookAtMapper.handleHealth(item, world, targetBlockPosition, targetDataComponents);
            BetterLookAtMapper.handlePlugin(item, targetDataComponents);
            BetterLookAtMapper.handleConsumable(item, targetDataComponents);
            BetterLookAtMapper.handleRecommendedTools(recommendedTools, targetDataComponents);
            BetterLookAtMapper.handleProcessingBenchState(world, targetBlockPosition, targetDataComponents);

            future.complete(targetDataComponents);
        });

        return future;
    }

    public static CompletableFuture<List<IBetterLookAtComponent>> fromEntity(
        @Nonnull Ref<EntityStore> storeRef,
        @Nonnull Ref<EntityStore> targetEntityRef
    ) {
        var store = storeRef.getStore();
        var future = new CompletableFuture<List<IBetterLookAtComponent>>();
        var targetDataComponents = new ArrayList<IBetterLookAtComponent>();

        var npcEntityComponentType = NPCEntity.getComponentType();

        if (npcEntityComponentType == null) {
            future.complete(targetDataComponents);
            return future;
        }

        var targetEntity = store.getComponent(targetEntityRef, npcEntityComponentType);

        if (targetEntity == null) {
            future.complete(targetDataComponents);
            return future;
        }

        var targetEntityRole = targetEntity.getRole();

        if (targetEntityRole == null) {
            future.complete(targetDataComponents);
            return future;
        }

        BetterLookAtMapper.handleTitle(targetEntityRole, targetDataComponents);
        BetterLookAtMapper.handleHealth(targetEntityRole, targetEntityRef, targetDataComponents);
        BetterLookAtMapper.handleInvulnerable(targetEntityRole, targetDataComponents);

        future.complete(targetDataComponents);
        return future;
    }

    private static void handleFuel(@Nonnull Item item, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        targetDataComponents.add(new BetterLookAtFuelComponent(item.getFuelQuality()));
    }

    private static void handleIcon(@Nonnull Item item, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        targetDataComponents.add(new BetterLookAtIconComponent(item.getId()));
    }

    private static void handleTitle(@Nonnull Item item, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        targetDataComponents.add(new BetterLookAtTitleComponent(Message.translation(item.getTranslationKey())));
    }

    private static void handleHealth(
        @Nonnull Item item,
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
        for (var recommendedTool : recommendedTools) {
            targetDataComponents.add(new BetterLookAtRecommendedToolComponent(recommendedTool));
        }
    }

    private static void handleProcessingBenchState(
        @Nonnull World world,
        @Nonnull Vector3i targetBlockPosition,
        @Nonnull List<IBetterLookAtComponent> targetDataComponents
    ) {
        // TODO(evandro):
        @SuppressWarnings("deprecation")
        var targetBlockState = world.getState(targetBlockPosition.x, targetBlockPosition.y, targetBlockPosition.z, true);

        if (targetBlockState instanceof ProcessingBenchState processingBenchState) {
            var recipe = processingBenchState.getRecipe();
            var inputProgress = processingBenchState.getInputProgress();
            var maxInputProgress = recipe != null ? recipe.getTimeSeconds() : 0.0;

            var benchTierLevel = processingBenchState.getBench().getTierLevel(processingBenchState.getTierLevel());
            var craftingTimeReductionModifier = benchTierLevel != null ? benchTierLevel.getCraftingTimeReductionModifier() : 0.0;

            var realMaxInputProgress = (float) (maxInputProgress * (1.0F - craftingTimeReductionModifier));

            targetDataComponents.add(new BetterLookAtProcessingBenchStateComponent(inputProgress, realMaxInputProgress));
        }
    }

    private static void handleTitle(@Nonnull Role entity, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        targetDataComponents.add(new BetterLookAtTitleComponent(Message.translation(entity.getNameTranslationKey())));
    }

    private static void handleHealth(@Nonnull Role entity, @Nonnull Ref<EntityStore> entityRef, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        var targetEntityStatsComponent = entityRef.getStore().getComponent(entityRef, EntityStatMap.getComponentType());

        if (targetEntityStatsComponent == null) {
            return;
        }

        var targetEntityHealthStat = targetEntityStatsComponent.get(DefaultEntityStatTypes.getHealth());

        if (targetEntityHealthStat == null) {
            return;
        }

        targetDataComponents.add(
            new BetterLookAtHealthComponent(targetEntityHealthStat.get(), entity.getInitialMaxHealth()));
    }

    private static void handleInvulnerable(@Nonnull Role entity, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        targetDataComponents.add(new BetterLookAtInvulnerableComponent(entity.isInvulnerable()));
    }
}
