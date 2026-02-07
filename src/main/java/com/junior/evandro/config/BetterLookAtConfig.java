package com.junior.evandro.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;

public class BetterLookAtConfig {
    private boolean enabled = true;
    private boolean showBench = true;
    private boolean showChest = true;
    private boolean showRecommendedTool = true;
    private BetterLookAtScale benchScale = BetterLookAtScale.Medium;
    private BetterLookAtScale chestScale = BetterLookAtScale.Small;
    private BetterLookAtScale recommendedToolScale = BetterLookAtScale.Small;
    private BetterLookAtPosition position = BetterLookAtPosition.TopLeft;

    public static final BuilderCodec<BetterLookAtConfig> CODEC = BuilderCodec
        .builder(BetterLookAtConfig.class, BetterLookAtConfig::new)
        .append(EnabledField.create(), EnabledField::setter, EnabledField::getter).add()
        .append(ShowBenchField.create(), ShowBenchField::setter, ShowBenchField::getter).add()
        .append(ShowChestField.create(), ShowChestField::setter, ShowChestField::getter).add()
        .append(PositionField.create(), PositionField::setter, PositionField::getter).add()
        .append(ChestScaleField.create(), ChestScaleField::setter, ChestScaleField::getter).add()
        .append(BenchScaleField.create(), BenchScaleField::setter, BenchScaleField::getter).add()
        .append(ShowRecommendedToolField.create(), ShowRecommendedToolField::setter, ShowRecommendedToolField::getter).add()
        .append(RecommendedToolScaleField.create(), RecommendedToolScaleField::setter, RecommendedToolScaleField::getter).add()
        .build();

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getShowBench() {
        return this.showBench;
    }

    public void setShowBench(boolean showBench) {
        this.showBench = showBench;
    }

    public boolean getShowChest() {
        return this.showChest;
    }

    public void setShowChest(boolean showChest) {
        this.showChest = showChest;
    }

    public BetterLookAtPosition getPosition() {
        return this.position;
    }

    public void setPosition(BetterLookAtPosition position) {
        this.position = position;
    }

    public BetterLookAtScale getBenchScale() {
        return this.benchScale;
    }

    public void setBenchScale(BetterLookAtScale benchScale) {
        this.benchScale = benchScale;
    }

    public BetterLookAtScale getChestScale() {
        return this.chestScale;
    }

    public void setChestScale(BetterLookAtScale chestScale) {
        this.chestScale = chestScale;
    }

    public boolean getShowRecommendedTool() {
        return this.showRecommendedTool;
    }

    public void setShowRecommendedTool(boolean showRecommendedTool) {
        this.showRecommendedTool = showRecommendedTool;
    }

    public BetterLookAtScale getRecommendedToolScale() {
        return this.recommendedToolScale;
    }

    public void setRecommendedToolScale(BetterLookAtScale recommendedToolScale) {
        this.recommendedToolScale = recommendedToolScale;
    }

    private static class EnabledField {
        public static String FIELD_NAME = "Enabled";

        private static KeyedCodec<Boolean> create() {
            return new KeyedCodec<>(FIELD_NAME, Codec.BOOLEAN);
        }

        private static boolean getter(BetterLookAtConfig config, ExtraInfo extra) {
            return config.enabled;
        }

        private static void setter(BetterLookAtConfig config, Boolean value, ExtraInfo extra) {
            config.enabled = value;
        }
    }

    private static class ShowBenchField {
        public static String FIELD_NAME = "ShowBench";

        private static KeyedCodec<Boolean> create() {
            return new KeyedCodec<>(FIELD_NAME, Codec.BOOLEAN);
        }

        private static boolean getter(BetterLookAtConfig config, ExtraInfo extra) {
            return config.showBench;
        }

        private static void setter(BetterLookAtConfig config, Boolean value, ExtraInfo extra) {
            config.showBench = value;
        }
    }

    private static class ShowChestField {
        public static String FIELD_NAME = "ShowChest";

        private static KeyedCodec<Boolean> create() {
            return new KeyedCodec<>(FIELD_NAME, Codec.BOOLEAN);
        }

        private static boolean getter(BetterLookAtConfig config, ExtraInfo extra) {
            return config.showChest;
        }

        private static void setter(BetterLookAtConfig config, Boolean value, ExtraInfo extra) {
            config.showChest = value;
        }
    }

    private static class PositionField {
        public static String FIELD_NAME = "Position";

        private static KeyedCodec<BetterLookAtPosition> create() {
            return new KeyedCodec<>(FIELD_NAME, new EnumCodec<>(BetterLookAtPosition.class));
        }

        private static BetterLookAtPosition getter(BetterLookAtConfig config) {
            return config.position;
        }

        private static void setter(BetterLookAtConfig config, BetterLookAtPosition value) {
            config.position = value;
        }
    }

    private static class BenchScaleField {
        public static String FIELD_NAME = "BenchScale";

        private static KeyedCodec<BetterLookAtScale> create() {
            return new KeyedCodec<>(FIELD_NAME, new EnumCodec<>(BetterLookAtScale.class));
        }

        private static BetterLookAtScale getter(BetterLookAtConfig config) {
            return config.benchScale;
        }

        private static void setter(BetterLookAtConfig config, BetterLookAtScale value) {
            config.benchScale = value;
        }
    }

    private static class ChestScaleField {
        public static String FIELD_NAME = "ChestScale";

        private static KeyedCodec<BetterLookAtScale> create() {
            return new KeyedCodec<>(FIELD_NAME, new EnumCodec<>(BetterLookAtScale.class));
        }

        private static BetterLookAtScale getter(BetterLookAtConfig config) {
            return config.chestScale;
        }

        private static void setter(BetterLookAtConfig config, BetterLookAtScale value) {
            config.chestScale = value;
        }
    }

    private static class ShowRecommendedToolField {
        public static String FIELD_NAME = "ShowRecommendedTool";

        private static KeyedCodec<Boolean> create() {
            return new KeyedCodec<>(FIELD_NAME, Codec.BOOLEAN);
        }

        private static boolean getter(BetterLookAtConfig config) {
            return config.showRecommendedTool;
        }

        private static void setter(BetterLookAtConfig config, boolean value) {
            config.showRecommendedTool = value;
        }
    }

    private static class RecommendedToolScaleField {
        public static String FIELD_NAME = "RecommendedToolScale";

        private static KeyedCodec<BetterLookAtScale> create() {
            return new KeyedCodec<>(FIELD_NAME, new EnumCodec<>(BetterLookAtScale.class));
        }

        private static BetterLookAtScale getter(BetterLookAtConfig config) {
            return config.recommendedToolScale;
        }

        private static void setter(BetterLookAtConfig config, BetterLookAtScale value) {
            config.recommendedToolScale = value;
        }
    }
}
