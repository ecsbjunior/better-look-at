package com.junior.evandro.ui;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.junior.evandro.BetterLookAt;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

public class BetterLookAtMultipleHudManager extends BetterLookAtHudManager {
    public BetterLookAtMultipleHudManager() {
        MultipleHud.init();
    }

    @Override
    public void showCustomHud(@Nonnull Player player, @Nonnull PlayerRef playerRef, @Nonnull BetterLookAtCustomHud hud) {
        MultipleHud.setCustomHud(player, playerRef, "BetterLookAtMultipleHud", hud);
    }

    @Override
    public void hideCustomHud(@NonNullDecl Player player, @NonNullDecl PlayerRef playerRef) {
        MultipleHud.hideCustomHud(player, playerRef, "BetterLookAtMultipleHud");
    }

    public static final class MultipleHud {
        public static final String MULTIPLE_HUD_CLASS_NAME = "com.buuz135.mhud.MultipleHUD";
        private static final String MULTIPLE_HUD_GET_INSTANCE_METHOD_NAME = "getInstance";
        private static final String MULTIPLE_HUD_SET_CUSTOM_HUD_METHOD_NAME = "setCustomHud";
        private static final String MULTIPLE_HUD_HIDE_CUSTOM_HUD_METHOD_NAME = "hideCustomHud";

        private static Method GET_INSTANCE;
        private static Method SET_CUSTOM_HUD;
        private static Method HIDE_CUSTOM_HUD;

        public static void init() {
            try {
                var multipleHud = MultipleHud.load();

                if (multipleHud == null) {
                    return;
                }

                GET_INSTANCE = multipleHud.getMethod(MULTIPLE_HUD_GET_INSTANCE_METHOD_NAME);
                SET_CUSTOM_HUD = multipleHud.getMethod(MULTIPLE_HUD_SET_CUSTOM_HUD_METHOD_NAME,
                    Player.class, PlayerRef.class, String.class, CustomUIHud.class);
                HIDE_CUSTOM_HUD = multipleHud.getMethod(MULTIPLE_HUD_HIDE_CUSTOM_HUD_METHOD_NAME,
                    Player.class, PlayerRef.class, String.class);
            } catch (NoSuchMethodException noSuchMethodException) {
                BetterLookAt.LOGGER.atWarning().log("Could not found method for '%s' plugin (%s)".formatted(
                    MULTIPLE_HUD_CLASS_NAME, noSuchMethodException.getMessage()));
            }
        }

        public static Class<?> load() {
            try {
                var pluginManager = PluginManager.get();
                var bridge = pluginManager.getBridgeClassLoader();
                return bridge.loadClass(MULTIPLE_HUD_CLASS_NAME);
            } catch (ClassNotFoundException classNotFoundException) {
                BetterLookAt.LOGGER.atWarning().log("Could not found '%s' plugin (%s)".formatted(
                    MULTIPLE_HUD_CLASS_NAME, classNotFoundException.getMessage()));
            }

            return null;
        }

        public static boolean isAvailable() {
            return PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD")) != null;
        }

        public static void setCustomHud(
            @Nonnull Player player,
            @Nonnull PlayerRef playerRef,
            String identifier,
            CustomUIHud customHud
        ) {
            try {
                var multipleHud = GET_INSTANCE.invoke(null);

                if (multipleHud == null) {
                    return;
                }

                SET_CUSTOM_HUD.invoke(multipleHud, player, playerRef, identifier, customHud);
            } catch (Exception exception) {
                BetterLookAt.LOGGER.atWarning().log("Could not call setCustomHud from %s (%s)".formatted(
                    MULTIPLE_HUD_CLASS_NAME, exception.getMessage()));
            }
        }

        public static void hideCustomHud(
            @Nonnull Player player,
            @Nonnull PlayerRef playerRef,
            String identifier
        ) {
            try {
                var multipleHud = GET_INSTANCE.invoke(null);

                if (multipleHud == null) {
                    return;
                }

                HIDE_CUSTOM_HUD.invoke(multipleHud, player, playerRef, identifier);
            } catch (Exception exception) {
                BetterLookAt.LOGGER.atWarning().log("Could not call hideCustomHud from %s (%s)".formatted(
                    MULTIPLE_HUD_CLASS_NAME, exception.getMessage()));
            }
        }
    }
}
