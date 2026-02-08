package com.junior.evandro.utils;

import com.hypixel.hytale.server.core.inventory.ItemStack;

public class BetterLookAtItemStackUtils {
    public static ItemStack shallowClone(ItemStack itemStack) {
        return new ItemStack(itemStack.getItemId(), itemStack.getQuantity());
    }
}
