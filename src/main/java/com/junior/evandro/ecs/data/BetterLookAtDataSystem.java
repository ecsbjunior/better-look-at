package com.junior.evandro.ecs.data;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.junior.evandro.BetterLookAt;
import com.junior.evandro.ecs.BetterLookAtWorld;
import com.junior.evandro.ecs.IBetterLookAtSystem;
import com.junior.evandro.ecs.data.components.*;
import com.junior.evandro.ecs.data.entities.BetterLookAtDataEntity;
import com.junior.evandro.ui.data.records.BetterLookAtDataRecord;

import javax.annotation.Nonnull;

public class BetterLookAtDataSystem implements IBetterLookAtSystem {
    @Override
    public void execute(
        int index,
        @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        BetterLookAtWorld betterLookAtWorld
    ) {
        var holder = EntityUtils.toHolder(index, archetypeChunk);
        var player = holder.getComponent(Player.getComponentType());
        var playerRef = holder.getComponent(PlayerRef.getComponentType());

        if (player == null || playerRef == null || !playerRef.isValid()) {
            return;
        }

        for (var dataEntity : betterLookAtWorld.getEntities(BetterLookAtDataEntity.class)) {
            if (dataEntity.getComponents().isEmpty()) {
                BetterLookAt.hudManager.hidden(player, playerRef);
                continue;
            }

            var record = new BetterLookAtDataRecord(
                dataEntity.getComponent(BetterLookAtFuelComponent.class),
                dataEntity.getComponent(BetterLookAtChestComponent.class),
                dataEntity.getComponent(BetterLookAtTitleComponent.class),
                dataEntity.getComponent(BetterLookAtPluginComponent.class),
                dataEntity.getComponent(BetterLookAtHealthComponent.class),
                dataEntity.getComponent(BetterLookAtBlockIconComponent.class),
                dataEntity.getComponent(BetterLookAtConsumableComponent.class),
                dataEntity.getComponent(BetterLookAtEntityIconComponent.class),
                dataEntity.getComponent(BetterLookAtInvulnerableComponent.class),
                dataEntity.getComponent(BetterLookAtRecommendedToolsComponent.class),
                dataEntity.getComponent(BetterLookAtProcessingBenchStateComponent.class)
            );

            BetterLookAt.hudManager.show(player, playerRef, record);
        }
    }
}
