package com.junior.evandro.ecs.data.entities;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.junior.evandro.ecs.BetterLookAtEntity;
import com.junior.evandro.ecs.data.components.*;
import com.junior.evandro.utils.BetterLookAtColor;
import com.junior.evandro.utils.BetterLookAtMessage;
import com.junior.evandro.ui.data.BetterLookAtDataHud;

import java.util.List;

public class BetterLookAtDataEntity extends BetterLookAtEntity {
    private BetterLookAtDataHud dataHud;

    public BetterLookAtDataEntity(int id) {
        super(id);
    }

    public void show(Ref<EntityStore> storeRef) {
        var store = storeRef.getStore();
        var player = store.getComponent(storeRef, Player.getComponentType());
        var playerRef = store.getComponent(storeRef, PlayerRef.getComponentType());

        assert player != null;
        assert playerRef != null;

        this.dataHud = new BetterLookAtDataHud(playerRef);

        this.dataHud.show(player);

        this.dataHud.appendIcon();
        this.dataHud.appendContent();
    }

    public void hidden(Ref<EntityStore> storeRef) {
        var store = storeRef.getStore();
        var player = store.getComponent(storeRef, Player.getComponentType());

        assert player != null;

        this.dataHud.hidden(player);
    }

    public void render() {
        this.dataHud.render();
    }

    public void updateFuel(BetterLookAtFuelComponent fuelComponent) {
        var isFuel = fuelComponent.value() > 1;

        var fuelMessage = Message.raw("Fuel: ");
        var isFuelMessage = BetterLookAtMessage.toMessage(isFuel);
        var fuelQualityMessage = BetterLookAtMessage.showIf(
            BetterLookAtMessage.wrapParentheses(BetterLookAtMessage.toMessage(fuelComponent.value())), isFuel);

        this.dataHud.updateMetadata(Message.join(fuelMessage, isFuelMessage, fuelQualityMessage));
    }

    public void updateIcon(BetterLookAtIconComponent iconComponent) {
        this.dataHud.updateIcon(iconComponent.value());
    }

    public void updateTitle(BetterLookAtTitleComponent titleComponent) {
        this.dataHud.updateTitle(titleComponent.value());
    }

    public void updatePlugin(BetterLookAtPluginComponent pluginComponent) {
        var pluginMessage = Message.raw(pluginComponent.value()).color(BetterLookAtColor.PRIMARY).bold(true);

        this.dataHud.updatePlugin(pluginMessage);
    }

    public void updateHealth(BetterLookAtHealthComponent healthComponent) {
        this.dataHud.updateHealth(healthComponent.value(), healthComponent.maxValue());
    }

    public void updateConsumable(BetterLookAtConsumableComponent consumableComponent) {
        var consumableMessage = Message.raw("Consumable: ");
        var isConsumableMessage = BetterLookAtMessage.toMessage(consumableComponent.value());

        this.dataHud.updateMetadata(Message.join(consumableMessage, isConsumableMessage));
    }

    public void updateInvulnerable(BetterLookAtInvulnerableComponent invulnerableComponent) {
        var invulnerableMessage = Message.raw("Invulnerable: ");
        var isInvulnerableMessage = BetterLookAtMessage.toMessage(invulnerableComponent.value());

        this.dataHud.updateMetadata(Message.join(invulnerableMessage, isInvulnerableMessage));
    }

    public void updateRecommendedTools(List<BetterLookAtRecommendedToolComponent> recommendedToolComponents) {
        if (recommendedToolComponents.isEmpty()) {
            return;
        }

        this.dataHud.updateRecommendedTools(
            recommendedToolComponents.stream().map(BetterLookAtRecommendedToolComponent::value).toList());
    }

    public void updateProcessingBenchState(BetterLookAtProcessingBenchStateComponent processingBenchStateComponent) {
        var progress = processingBenchStateComponent.value();
        var maxProgress = processingBenchStateComponent.maxValue();

        if (maxProgress < progress || maxProgress == 0) {
            return;
        }

        this.dataHud.updateProcessingBenchState(progress, maxProgress);
    }
}
