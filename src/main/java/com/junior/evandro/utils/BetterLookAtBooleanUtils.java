package com.junior.evandro.utils;

public class BetterLookAtBooleanUtils {
    public static void ifTrue(boolean value, Runnable onTrue, Runnable onFalse) {
        if (value) {
            onTrue.run();
        } else {
            onFalse.run();
        }
    }
}
