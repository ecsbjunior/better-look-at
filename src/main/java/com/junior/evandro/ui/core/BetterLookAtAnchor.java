package com.junior.evandro.ui.core;

import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;

public class BetterLookAtAnchor {
    public static class Builder {
        private int top = Integer.MIN_VALUE;
        private int bottom = Integer.MIN_VALUE;
        private int left = Integer.MIN_VALUE;
        private int right = Integer.MIN_VALUE;
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
            if (this.top != Integer.MIN_VALUE) anchor.setTop(Value.of(this.top));
            if (this.bottom != Integer.MIN_VALUE) anchor.setBottom(Value.of(this.bottom));
            if (this.left != Integer.MIN_VALUE) anchor.setLeft(Value.of(this.left));
            if (this.right != Integer.MIN_VALUE) anchor.setRight(Value.of(this.right));
            if (this.width >= 0) anchor.setWidth(Value.of(this.width));
            if (this.height >= 0) anchor.setHeight(Value.of(this.height));
            return anchor;
        }
    }
}
