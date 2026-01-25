package com.junior.evandro;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.junior.evandro.ecs.BetterLookAtWorld;
import com.junior.evandro.ecs.data.BetterLookAtDataSystem;
import com.junior.evandro.ecs.data.entities.BetterLookAtDataEntity;
import com.junior.evandro.ecs.ticking.BetterLookAtPlayerLookAtTickingSystem;
import com.junior.evandro.utils.BetterLookAtBlockUtils;
import com.junior.evandro.utils.BetterLookAtItemUtils;

import javax.annotation.Nonnull;

public class BetterLookAt extends JavaPlugin {
    public static int dataEntityId;
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public BetterLookAt(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();

        var betterLookAtWorldSystem = new BetterLookAtWorld();

        BetterLookAt.dataEntityId = betterLookAtWorldSystem.registerEntity(BetterLookAtDataEntity::new);

        betterLookAtWorldSystem.registerSystem(new BetterLookAtDataSystem());

        var entityModule = EntityModule.get();
        var playerComponentType = entityModule.getPlayerComponentType();

        var entityStoreRegistry = this.getEntityStoreRegistry();

        entityStoreRegistry.registerSystem(new BetterLookAtPlayerLookAtTickingSystem(betterLookAtWorldSystem, playerComponentType));
    }

    @Override
    protected void start() {
        super.start();
        BetterLookAtItemUtils.init();
        BetterLookAtBlockUtils.init();
    }
}
