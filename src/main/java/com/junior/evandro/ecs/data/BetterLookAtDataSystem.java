package com.junior.evandro.ecs.data;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.junior.evandro.ecs.BetterLookAtWorld;
import com.junior.evandro.ecs.IBetterLookAtSystem;
import com.junior.evandro.ecs.data.components.*;
import com.junior.evandro.ecs.data.entities.BetterLookAtDataEntity;
import com.junior.evandro.ecs.data.requests.BetterLookAtHiddenDataRequest;
import com.junior.evandro.ecs.data.requests.BetterLookAtShowDetailedDataRequest;

import java.util.Optional;

public class BetterLookAtDataSystem implements IBetterLookAtSystem {
    @Override
    public void execute(Ref<EntityStore> storeRef, BetterLookAtWorld betterLookAtWorld) {
        for (var dataEntity : betterLookAtWorld.getEntities(BetterLookAtDataEntity.class)) {
            var hiddenDataRequestOptional = dataEntity.getComponent(BetterLookAtHiddenDataRequest.class);
            var showDetailedDataRequestOptional = dataEntity.getComponent(BetterLookAtShowDetailedDataRequest.class);

            if (hiddenDataRequestOptional.isPresent()) {
                dataEntity.hidden(storeRef);
            } else {
                var fuelComponentOptional = Optional.<BetterLookAtFuelComponent>empty();
                var iconComponentOptional = dataEntity.getComponent(BetterLookAtIconComponent.class);
                var titleComponentOptional = dataEntity.getComponent(BetterLookAtTitleComponent.class);
                var pluginComponentOptional = dataEntity.getComponent(BetterLookAtPluginComponent.class);
                var healthComponentOptional = dataEntity.getComponent(BetterLookAtHealthComponent.class);
                var consulmableComponentOptional = Optional.<BetterLookAtConsumableComponent>empty();
                var invulnerableComponentOptional = Optional.<BetterLookAtInvulnerableComponent>empty();
                var recommendedToolComponentsOptional = dataEntity.getComponents(BetterLookAtRecommendedToolComponent.class);
                var processingBenchStateComponentOptional = dataEntity.getComponent(BetterLookAtProcessingBenchStateComponent.class);

                if (showDetailedDataRequestOptional.isPresent()) {
                    fuelComponentOptional = dataEntity.getComponent(BetterLookAtFuelComponent.class);
                    consulmableComponentOptional = dataEntity.getComponent(BetterLookAtConsumableComponent.class);
                    invulnerableComponentOptional = dataEntity.getComponent(BetterLookAtInvulnerableComponent.class);
                }

                dataEntity.show(storeRef);

                titleComponentOptional.ifPresent(dataEntity::updateTitle);
                iconComponentOptional.ifPresent(dataEntity::updateIcon);
                healthComponentOptional.ifPresent(dataEntity::updateHealth);
                processingBenchStateComponentOptional.ifPresent(dataEntity::updateProcessingBenchState);
                fuelComponentOptional.ifPresent(dataEntity::updateFuel);
                invulnerableComponentOptional.ifPresent(dataEntity::updateInvulnerable);
                consulmableComponentOptional.ifPresent(dataEntity::updateConsumable);
                dataEntity.updateRecommendedTools(recommendedToolComponentsOptional);
                pluginComponentOptional.ifPresent(dataEntity::updatePlugin);

                dataEntity.render();
            }

            if (hiddenDataRequestOptional.isPresent()) dataEntity.removeComponent(BetterLookAtHiddenDataRequest.class);
            if (showDetailedDataRequestOptional.isPresent()) dataEntity.removeComponent(BetterLookAtShowDetailedDataRequest.class);
        }
    }
}
