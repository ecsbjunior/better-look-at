package com.junior.evandro.utils;

import com.hypixel.hytale.server.core.asset.AssetModule;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;

import java.util.HashMap;

public class BetterLookAtBlockUtils {
    public static final HashMap<String, String> PLUGIN_BY_BLOCK_TYPE_ID = new HashMap<>();

    public static void init() {
        var assetModule = AssetModule.get();
        var assetPacks = assetModule.getAssetPacks();
        var blockTypeAssetsMap = BlockType.getAssetMap();

        for (var assetPack : assetPacks) {
            var assetPackName = assetPack.getName();

            var blockTypeKeys = blockTypeAssetsMap.getKeysForPack(assetPackName);

            if (blockTypeKeys == null) {
                continue;
            }

            for (var blockTypeKey : blockTypeKeys) {
                BetterLookAtBlockUtils.PLUGIN_BY_BLOCK_TYPE_ID.put(blockTypeKey, assetPackName);
            }
        }
    }

    public static String getPlugin(String blockTypeId) {
        return BetterLookAtBlockUtils.PLUGIN_BY_BLOCK_TYPE_ID.get(blockTypeId);
    }
}
