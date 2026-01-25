package com.junior.evandro.utils;

import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BetterLookAtItemUtils {
    public static final HashMap<String, BetterLookAtToolSpecIndex> ITEM_TOOL_SPEC_INDEX_BY_GATHER_TYPE = new HashMap<>();

    public static void init() {
        var itemAssetsMap = Item.getAssetMap().getAssetMap();

        for (var item : itemAssetsMap.values()) {
            var itemTool = item.getTool();

            if (itemTool == null || itemTool.getSpecs() == null) {
                continue;
            }

            for (var itemToolSpec : itemTool.getSpecs()) {
                var itemToolSpecByGatherType = BetterLookAtItemUtils.ITEM_TOOL_SPEC_INDEX_BY_GATHER_TYPE
                    .computeIfAbsent(itemToolSpec.getGatherType(), k -> new BetterLookAtToolSpecIndex());

                itemToolSpecByGatherType.add(item.getId(), itemToolSpec);
            }
        }
    }

    public static List<String> getRecommendedTools(BlockType blockType) {
        List<String> recommendedTools = new ArrayList<>();
        var blockGathering = blockType.getGathering();
        if (blockGathering != null) {
            var blockBreaking = blockGathering.getBreaking();
            if (blockBreaking != null) {
                var blockGatherType = blockBreaking.getGatherType();
                if (blockGatherType != null) {
                    var itemToolSpecIndex = BetterLookAtItemUtils.ITEM_TOOL_SPEC_INDEX_BY_GATHER_TYPE.get(blockGatherType);
                    if (itemToolSpecIndex != null) {
                        recommendedTools = itemToolSpecIndex.topItemId(5);
                    }
                }
            }
        }
        return recommendedTools;
    }
}
