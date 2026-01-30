package com.junior.evandro.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BetterLookAtEntity {
    private final int id;
    private final List<IBetterLookAtComponent> components = new ArrayList<>();

    public BetterLookAtEntity(int id) {
        this.id = id;
    }

    public List<IBetterLookAtComponent> getComponents() {
        return this.components;
    }

    public <T extends IBetterLookAtComponent> Optional<T> getComponent(Class<T> component) {
        return this.components.stream().filter(component::isInstance).map(component::cast).findFirst();
    }

    public void addComponent(IBetterLookAtComponent component) {
        this.components.add(component);
    }

    public <T extends IBetterLookAtComponent> boolean removeComponent(Class<T> component) {
        return this.components.removeIf(component::isInstance);
    }

    public int getId() {
        return this.id;
    }
}
