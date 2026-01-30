package com.junior.evandro.ui.core;

public class BetterLookAtPadding {
    public int top = 0;
    public int bottom = 0;
    public int left = 0;
    public int right = 0;

    private BetterLookAtPadding(Builder builder) {
        this.top = builder.top;
        this.bottom = builder.bottom;
        this.left = builder.left;
        this.right = builder.right;
    }

    public static class Builder {
        private int top = 0;
        private int bottom = 0;
        private int left = 0;
        private int right = 0;

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

        public BetterLookAtPadding build() {
            return new BetterLookAtPadding(this);
        }
    }
}
