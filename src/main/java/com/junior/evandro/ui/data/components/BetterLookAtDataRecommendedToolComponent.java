package com.junior.evandro.ui.data.components;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;

import javax.annotation.Nonnull;

public class BetterLookAtDataRecommendedToolComponent extends BetterLookAtDataComponent {
    private static final String DATA_RECOMMENDED_TOOL_PATH =
        BetterLookAtDataComponent.BASE_PATH + "BetterLookAtDataRecommendedTool.ui";
    private static final String DATA_RECOMMENDED_TOOL_ITEM_PATH =
        BetterLookAtDataComponent.BASE_PATH + "BetterLookAtDataRecommendedToolItem.ui";

    private static final String DATA_RECOMMENDED_TOOL_ID = "#DataRecommendedToolContainer";
    private static final String DATA_RECOMMENDED_TOOL_ITEM_ID = "#DataRecommendedToolItem";
    private static final String DATA_RECOMMENDED_TOOL_ITEM_CONTAINER_ID = "#DataRecommendedToolItemContainer";

    private int index = 0;
    @Nonnull
    private final UICommandBuilder uiCommandBuilder;

    public BetterLookAtDataRecommendedToolComponent(@Nonnull UICommandBuilder uiCommandBuilder) {
        this.uiCommandBuilder = uiCommandBuilder;
    }

    public void render(String recommendedTool) {
        this.uiCommandBuilder.append(
            BetterLookAtDataRecommendedToolComponent.DATA_RECOMMENDED_TOOL_ID,
            BetterLookAtDataRecommendedToolComponent.DATA_RECOMMENDED_TOOL_ITEM_PATH);

        this.uiCommandBuilder.set(BetterLookAtDataRecommendedToolComponent.getRecommendedToolItemAt(this.index++), recommendedTool);
    }

    public void append(String parent) {
        if (this.index == 0) {
            this.uiCommandBuilder.append(parent, BetterLookAtDataRecommendedToolComponent.DATA_RECOMMENDED_TOOL_PATH);
        }
    }

    private static String getRecommendedToolItemAt(int index) {
        return String.join(" ",
            BetterLookAtDataRecommendedToolComponent.getRecommendedToolAt(index),
            BetterLookAtDataRecommendedToolComponent.getRecommendedToolItem());
    }

    private static String getRecommendedToolAt(int index) {
        return String.format("%s[%d]", BetterLookAtDataRecommendedToolComponent.DATA_RECOMMENDED_TOOL_ID, index);
    }

    private static String getRecommendedToolItem() {
        return String.format("%s.ItemId", BetterLookAtDataRecommendedToolComponent.DATA_RECOMMENDED_TOOL_ITEM_ID);
    }
}
