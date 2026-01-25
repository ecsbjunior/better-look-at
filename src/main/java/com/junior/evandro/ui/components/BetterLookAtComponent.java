package com.junior.evandro.ui.components;

public interface BetterLookAtComponent<T> {
    void render(T content);
    void clear();
}
