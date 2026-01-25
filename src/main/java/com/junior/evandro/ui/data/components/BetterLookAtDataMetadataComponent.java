package com.junior.evandro.ui.data.components;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;

import javax.annotation.Nonnull;

public class BetterLookAtDataMetadataComponent extends BetterLookAtDataComponent {
    private static final String DATA_METADATA_PATH =
        BetterLookAtDataComponent.BASE_PATH + "BetterLookAtDataMetadata.ui";
    private static final String DATA_METADATA_ITEM_PATH =
        BetterLookAtDataComponent.BASE_PATH + "BetterLookAtDataMetadataItem.ui";

    private static final String DATA_METADATA_ID = "#DataMetadataContainer";
    private static final String DATA_METADATA_ITEM_ID = "#DataMetadataItem";
    private static final String DATA_METADATA_ITEM_CONTAINER_ID = "#DataMetadataItemContainer";

    private int index = 0;
    @Nonnull
    private final UICommandBuilder uiCommandBuilder;

    public BetterLookAtDataMetadataComponent(@Nonnull UICommandBuilder uiCommandBuilder) {
        this.uiCommandBuilder = uiCommandBuilder;
    }

    public void render(Message message) {
        this.uiCommandBuilder.append(
            BetterLookAtDataMetadataComponent.DATA_METADATA_ID,
            BetterLookAtDataMetadataComponent.DATA_METADATA_ITEM_PATH);

        this.uiCommandBuilder.set(BetterLookAtDataMetadataComponent.getDataMetadataItemAt(this.index++), message);
    }

    public void append(String parent) {
        if (this.index == 0) {
            this.uiCommandBuilder.append(parent, BetterLookAtDataMetadataComponent.DATA_METADATA_PATH);
        }
    }

    private static String getDataMetadataItemAt(int index) {
        return String.join(" ",
            BetterLookAtDataMetadataComponent.getDataMetadataAt(index),
            BetterLookAtDataMetadataComponent.getDataMetadataItem());
    }

    private static String getDataMetadataAt(int index) {
        return String.format("%s[%d]", BetterLookAtDataMetadataComponent.DATA_METADATA_ID, index);
    }

    private static String getDataMetadataItem() {
        return String.format("%s.TextSpans", BetterLookAtDataMetadataComponent.DATA_METADATA_ITEM_ID);
    }
}
