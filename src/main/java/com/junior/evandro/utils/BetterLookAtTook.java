package com.junior.evandro.utils;

import com.junior.evandro.BetterLookAt;

public class BetterLookAtTook {
    public static void took(Runnable action) {
        var startTime = System.nanoTime();
        action.run();
        var endTime = System.nanoTime();
        BetterLookAt.LOGGER.atInfo().log("Took %fms", ((endTime - startTime) / 1_000_000.0));
    }
}
