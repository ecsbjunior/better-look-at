package com.junior.evandro;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.junior.evandro.ecs.BetterLookAtWorld;
import com.junior.evandro.ecs.data.BetterLookAtDataSystem;
import com.junior.evandro.ecs.data.entities.BetterLookAtDataEntity;
import com.junior.evandro.ecs.ticking.BetterLookAtPlayerLookAtTickingSystem;
import com.junior.evandro.ui.BetterLookAtMultipleHudManager;
import com.junior.evandro.utils.BetterLookAtBlockUtils;
import com.junior.evandro.utils.BetterLookAtItemUtils;
import com.junior.evandro.ui.BetterLookAtHudManager;
import com.junior.evandro.ui.BetterLookAtVanillaHudManager;

import javax.annotation.Nonnull;

public class BetterLookAt extends JavaPlugin {
    public static final String NAME = "BetterLookAt";
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public static BetterLookAtHudManager hudManager = new BetterLookAtVanillaHudManager();
    public static BetterLookAtWorld WORLD = new BetterLookAtWorld();
    public static int ENTITY_ID = BetterLookAt.WORLD.registerEntity(BetterLookAtDataEntity::new);

    public BetterLookAt(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();

        BetterLookAt.WORLD.registerSystem(new BetterLookAtDataSystem());

        this.getEntityStoreRegistry().registerSystem(new BetterLookAtPlayerLookAtTickingSystem());
    }

    @Override
    protected void start() {
        super.start();

        BetterLookAtItemUtils.init();
        BetterLookAtBlockUtils.init();

        if (BetterLookAtMultipleHudManager.MultipleHud.isAvailable()) {
            BetterLookAt.hudManager = new BetterLookAtMultipleHudManager();
        }
    }
}
