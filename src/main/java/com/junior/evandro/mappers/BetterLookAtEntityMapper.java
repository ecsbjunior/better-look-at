package com.junior.evandro.mappers;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.role.Role;
import com.junior.evandro.ecs.IBetterLookAtComponent;
import com.junior.evandro.ecs.data.components.*;
import com.junior.evandro.ecs.ticking.BetterLookAtPlayerLookAtTickingSystem;
import com.junior.evandro.utils.BetterLookAtDataInteraction;
import com.junior.evandro.utils.BetterLookAtMovementStateUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BetterLookAtEntityMapper {
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

        var npcEntityComponentType = NPCEntity.getComponentType();

        if (npcEntityComponentType == null) {
            return targetDataComponents;
        }

        var targetEntityRef = TargetUtil.getTargetEntity(storeRef, (float) (BetterLookAtPlayerLookAtTickingSystem.MAX_DISTANCE * 1.20), commandBuffer);

        if (targetEntityRef == null) {
            return targetDataComponents;
        }

        var store = storeRef.getStore();
        var targetEntity = store.getComponent(targetEntityRef, npcEntityComponentType);

        if (targetEntity == null) {
            return targetDataComponents;
        }

        var targetEntityRole = targetEntity.getRole();

        if (targetEntityRole == null) {
            return targetDataComponents;
        }

        BetterLookAtEntityMapper.handleIcon(targetEntityRole, targetDataComponents);
        BetterLookAtEntityMapper.handleTitle(targetEntityRole, targetDataComponents);
        BetterLookAtEntityMapper.handleHealth(targetEntityRole, targetEntityRef, targetDataComponents);

        var dataInteraction = BetterLookAtMovementStateUtil.toDataInteraction(storeRef, commandBuffer);

        if (dataInteraction.equals(BetterLookAtDataInteraction.SHOW_DETAILED_DATA)) {
            BetterLookAtEntityMapper.handleInvulnerable(targetEntityRole, targetDataComponents);
        }

        return targetDataComponents;
    }

    private static void handleIcon(@Nonnull Role entity, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        targetDataComponents.add(new BetterLookAtEntityIconComponent(entity.getRoleName()));
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

        var health = targetEntityHealthStat.get();
        var maxHealth = entity.getInitialMaxHealth();

        if (maxHealth < health || maxHealth == 0) {
            return;
        }

        targetDataComponents.add(new BetterLookAtHealthComponent(health, maxHealth));
    }

    private static void handleInvulnerable(@Nonnull Role entity, @Nonnull List<IBetterLookAtComponent> targetDataComponents) {
        targetDataComponents.add(new BetterLookAtInvulnerableComponent(entity.isInvulnerable()));
    }
}
