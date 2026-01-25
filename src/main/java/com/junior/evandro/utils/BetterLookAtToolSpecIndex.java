package com.junior.evandro.utils;

import com.hypixel.hytale.server.core.asset.type.item.config.ItemToolSpec;

import java.util.*;

public class BetterLookAtToolSpecIndex {
    private static final Comparator<BetterLookAtPair<String, ItemToolSpec>> POWER_COMPARATOR =
        Comparator.comparing(BetterLookAtToolSpecIndex::powerOf).reversed();

    private final NavigableSet<BetterLookAtPair<String, ItemToolSpec>> byPower = new TreeSet<>(POWER_COMPARATOR);

    public void add(String itemId, ItemToolSpec itemToolSpec) {
        byPower.add(new BetterLookAtPair<>(itemId, itemToolSpec));
    }

    public  String bestItemId() {
        return byPower.first().first;
    }

    public List<String> topItemId(int n) {
        return this.byPower.stream().limit(n).map(x -> x.first).toList();
    }

    private static double powerOf(BetterLookAtPair<String, ItemToolSpec> x) {
        return x.second.getPower();
    }
}
