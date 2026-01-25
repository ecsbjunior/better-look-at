package com.junior.evandro.ui.data.components;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;

import javax.annotation.Nonnull;

public class BetterLookAtDataProcessingBenchStateComponent extends BetterLookAtDataComponent {
    private static final String DATA_PROCESSING_BENCH_STATE_PATH =
        BetterLookAtDataComponent.BASE_PATH + "BetterLookAtDataProcessingBenchState.ui";

    private static final String DATA_PROCESSING_BENCH_STATE_ID = "#DataProcessingBenchStateContainer";
    private static final String DATA_PROCESSING_BENCH_STATE_PROGRESS_BAR_ID = "#DataProcessingBenchStateProgressBar";
    private static final String DATA_PROCESSING_BENCH_STATE_PROGRESS_BAR_TEXTURE_ID = "#DataProcessingBenchStateProgressBarTexture";

    @Nonnull
    private final UICommandBuilder uiCommandBuilder;

    public BetterLookAtDataProcessingBenchStateComponent(@Nonnull UICommandBuilder uiCommandBuilder) {
        this.uiCommandBuilder = uiCommandBuilder;
    }

    public void append(String parent) {
        this.uiCommandBuilder.append(parent,
            BetterLookAtDataProcessingBenchStateComponent.DATA_PROCESSING_BENCH_STATE_PATH);
    }

    public void render(float progress, float maxProgress) {
        this.setProcessingBenchStateProgressBar(progress, maxProgress);
        this.setProcessingBenchStateProgressBarTexture(progress, maxProgress);
    }

    private void setProcessingBenchStateProgressBar(float progress, float maxProgress) {
        if (maxProgress < progress) return;
        this.uiCommandBuilder.set(this.getDataProcessingBenchStateProgressBar(), progress / maxProgress);
    }

    private void setProcessingBenchStateProgressBarTexture(float progress, float maxProgress) {
        if (maxProgress < progress) return;
        this.uiCommandBuilder.set(this.getDataProcessingBenchStateProgressBarTexture(), progress / maxProgress);
    }

    private String getDataProcessingBenchStateProgressBar() {
        return String.format("%s.Value",
            BetterLookAtDataProcessingBenchStateComponent.DATA_PROCESSING_BENCH_STATE_PROGRESS_BAR_ID);
    }

    private String getDataProcessingBenchStateProgressBarTexture() {
        return String.format("%s.Value",
            BetterLookAtDataProcessingBenchStateComponent.DATA_PROCESSING_BENCH_STATE_PROGRESS_BAR_TEXTURE_ID);
    }
}
