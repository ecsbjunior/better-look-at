package com.junior.evandro.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;

public class BetterLookAtConfig {
    private boolean enabled = true;
    private BetterLookAtPosition position = BetterLookAtPosition.TOP_LEFT;

    public static final BuilderCodec<BetterLookAtConfig> CODEC = BuilderCodec
        .builder(BetterLookAtConfig.class, BetterLookAtConfig::new)
        .append(EnabledField.create(), EnabledField::setter, EnabledField::getter).add()
        .append(PositionField.create(), PositionField::setter, PositionField::getter).add()
        .build();

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public BetterLookAtPosition getPosition() {
        return this.position;
    }

    public void setPosition(BetterLookAtPosition position) {
        this.position = position;
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
}
