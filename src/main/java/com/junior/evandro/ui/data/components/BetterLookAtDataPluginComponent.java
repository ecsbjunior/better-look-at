package com.junior.evandro.ui.data.components;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;

import javax.annotation.Nonnull;

public class BetterLookAtDataPluginComponent extends BetterLookAtDataComponent {
    private static final String DATA_PLUGIN_PATH =
        BetterLookAtDataComponent.BASE_PATH + "BetterLookAtDataPlugin.ui";
    private static final String DATA_PLUGIN_ITEM_PATH =
        BetterLookAtDataComponent.BASE_PATH + "BetterLookAtDataPluginItem.ui";

    private static final String DATA_PLUGIN_ID = "#DataPlugin";

    @Nonnull
    private final UICommandBuilder uiCommandBuilder;

    public BetterLookAtDataPluginComponent(@Nonnull UICommandBuilder uiCommandBuilder) {
        this.uiCommandBuilder = uiCommandBuilder;
    }

    public void append(String parent) {
        this.uiCommandBuilder.append(parent, BetterLookAtDataPluginComponent.DATA_PLUGIN_PATH);
    }

    public void render(Message plugin) {
        this.uiCommandBuilder.set(BetterLookAtDataPluginComponent.getDataPlugin(), plugin);
    }

    private static String getDataPlugin() {
        return String.format("%s.TextSpans", BetterLookAtDataPluginComponent.DATA_PLUGIN_ID);
    }
}
