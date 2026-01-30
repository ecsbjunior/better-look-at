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
    public static Config<BetterLookAtConfig> CONFIG;

    public BetterLookAt(@Nonnull JavaPluginInit init) {
        super(init);

        BetterLookAt.CONFIG = this.withConfig(BetterLookAt.NAME, BetterLookAtConfig.CODEC);
    }

    @Override
    protected void setup() {
        super.setup();

        BetterLookAt.CONFIG.save();

        BetterLookAt.WORLD.registerSystem(new BetterLookAtDataSystem());

        this.getCommandRegistry().registerCommand(new BetterLookAtCommand());
        this.getEntityStoreRegistry().registerSystem(new BetterLookAtPlayerLookAtTickingSystem());

        BetterLookAt.LOGGER.atInfo().log("The plugin has been successfully setup");
    }

    @Override
    protected void start() {
        super.start();

        BetterLookAtItemUtils.init();
        BetterLookAtBlockUtils.init();

        if (BetterLookAtMultipleHudManager.MultipleHud.isAvailable()) {
            BetterLookAt.hudManager = new BetterLookAtMultipleHudManager();
            BetterLookAt.LOGGER.atInfo().log(
                "The HUD MANAGER has been configured with %s",
                BetterLookAtMultipleHudManager.MultipleHud.MULTIPLE_HUD_CLASS_NAME);
        }

        BetterLookAt.LOGGER.atInfo().log("The plugin has been successfully started");
    }
}
