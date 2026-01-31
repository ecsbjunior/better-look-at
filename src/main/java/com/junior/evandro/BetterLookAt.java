package com.junior.evandro;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.junior.evandro.commands.BetterLookAtCommand;
import com.junior.evandro.config.BetterLookAtConfig;
import com.junior.evandro.ecs.BetterLookAtWorld;
import com.junior.evandro.ecs.data.BetterLookAtDataSystem;
import com.junior.evandro.ecs.data.entities.BetterLookAtDataEntity;
import com.junior.evandro.ecs.ticking.BetterLookAtPlayerLookAtTickingSystem;
import com.junior.evandro.utils.BetterLookAtBlockUtils;
import com.junior.evandro.utils.BetterLookAtItemUtils;
import com.junior.evandro.ui.BetterLookAtHudManager;

import javax.annotation.Nonnull;

public class BetterLookAt extends JavaPlugin {
    public static final String NAME = "BetterLookAt";
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static int ENTITY_ID;
    public static BetterLookAtWorld WORLD;
    public static Config<BetterLookAtConfig> CONFIG;
    public static BetterLookAtHudManager HUD_MANAGER;

    public BetterLookAt(@Nonnull JavaPluginInit init) {
        super(init);

        BetterLookAt.CONFIG = this.withConfig(BetterLookAt.NAME, BetterLookAtConfig.CODEC);
    }

    @Override
    protected void setup() {
        super.setup();

        BetterLookAt.CONFIG.save();

        BetterLookAt.WORLD = new BetterLookAtWorld();
        BetterLookAt.WORLD.registerSystem(new BetterLookAtDataSystem());

        BetterLookAt.ENTITY_ID = BetterLookAt.WORLD.registerEntity(BetterLookAtDataEntity::new);

        this.getCommandRegistry().registerCommand(new BetterLookAtCommand());
        this.getEntityStoreRegistry().registerSystem(new BetterLookAtPlayerLookAtTickingSystem());

        BetterLookAt.LOGGER.atInfo().log("The plugin has been successfully setup");
    }

    @Override
    protected void start() {
        super.start();

        BetterLookAtItemUtils.init();
        BetterLookAtBlockUtils.init();

        BetterLookAt.HUD_MANAGER = BetterLookAtHudManager.getInstance();

        BetterLookAt.LOGGER.atInfo().log("The plugin has been successfully started");
    }
}
