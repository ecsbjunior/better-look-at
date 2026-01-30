package com.junior.evandro.utils;

import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;

public class BetterLookAtAnchor {
    public static class Builder {
        private int top = 0;
        private int bottom = 0;
        private int left = 0;
        private int right = 0;
        private int width = -1;
        private int height = -1;

        public Builder withTop(int top) {
            this.top = top;
            return this;
        }

        public Builder withBottom(int bottom) {
            this.bottom = bottom;
            return this;
        }

        public Builder withLeft(int left) {
            this.left = left;
            return this;
        }

        public Builder withRight(int right) {
            this.right = right;
            return this;
        }

        public Builder withWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder withHeight(int height) {
            this.height = height;
            return this;
        }

        public Anchor build() {
            var anchor = new Anchor();
            anchor.setTop(Value.of(this.top));
            anchor.setBottom(Value.of(this.bottom));
            anchor.setLeft(Value.of(this.left));
            anchor.setRight(Value.of(this.right));
            if (this.width >= 0) anchor.setWidth(Value.of(this.width));
            if (this.height >= 0) anchor.setHeight(Value.of(this.height));
            return anchor;
        }
    }
}
