package com.junior.evandro.utils;

import com.hypixel.hytale.server.core.asset.AssetModule;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BetterLookAtLoader {
    public static final class Holder {
        private static final List<String> ASSET_PACK_NAMES = init();
        private static final HashMap<String, String> PLUGIN_BY_BLOCK_ID = Block.init();
        private static final HashMap<String, String> PLUGIN_BY_ENTITY_ID = Entity.init();
        private static final HashMap<String, BetterLookAtToolSpecIndex> ITEM_TOOL_SPEC_INDEX_BY_GATHER_TYPE = Item.init();
    }

    private static List<String> init() {
        var assetPackNames = new ArrayList<String>();

        for (var assetPack : AssetModule.get().getAssetPacks()) {
            assetPackNames.add(assetPack.getName());
        }

        return assetPackNames;
    }

    private static String normalizeAssetPackName(String assetPackName) {
        var normalizedAssetPackName = assetPackName;

        if (normalizedAssetPackName.length() > 30) {
            var commaIndex = normalizedAssetPackName.indexOf(':');

            if (commaIndex != -1) {
                normalizedAssetPackName = normalizedAssetPackName.substring(commaIndex + 1, normalizedAssetPackName.length() - 1);
            }
        }

        return normalizedAssetPackName;
    }

    public static final class Item {
        private static HashMap<String, BetterLookAtToolSpecIndex> init() {
            var result = new HashMap<String, BetterLookAtToolSpecIndex>();

            var itemAssetsMap = com.hypixel.hytale.server.core.asset.type.item.config.Item.getAssetMap().getAssetMap();

            for (var item : itemAssetsMap.values()) {
                var itemTool = item.getTool();

                if (itemTool == null || itemTool.getSpecs() == null) {
                    continue;
                }

                for (var itemToolSpec : itemTool.getSpecs()) {
                    result.computeIfAbsent(itemToolSpec.getGatherType(), k -> new BetterLookAtToolSpecIndex())
                        .add(item.getId(), itemToolSpec);
                }
            }

            return result;
        }

        public static List<String> getRecommendedTools(BlockType blockType) {
            List<String> recommendedTools = new ArrayList<>();

            var blockGathering = blockType.getGathering();

            if (blockGathering == null) {
                return  recommendedTools;
            }

            var blockBreaking = blockGathering.getBreaking();

            if (blockBreaking == null) {
                return recommendedTools;
            }

            var blockGatherType = blockBreaking.getGatherType();

            if (blockGatherType == null) {
                return recommendedTools;
            }

            var itemToolSpecIndex = Holder.ITEM_TOOL_SPEC_INDEX_BY_GATHER_TYPE.get(blockGatherType);

            if (itemToolSpecIndex != null) {
                recommendedTools = itemToolSpecIndex.topItemId(5);
            }

            return recommendedTools;
        }
    }

    public static final class Block {
        private static HashMap<String, String> init() {
            var result = new HashMap<String, String>();

            var blockTypeAssetsMap = BlockType.getAssetMap();

            for (var assetPackName : Holder.ASSET_PACK_NAMES) {
                var blockTypeKeys = blockTypeAssetsMap.getKeysForPack(assetPackName);

                if (blockTypeKeys == null) {
                    continue;
                }

                for (var blockTypeKey : blockTypeKeys) {
                    result.put(blockTypeKey, normalizeAssetPackName(assetPackName));
                }
            }

            return result;
        }

        public static String getPlugin(String blockId) {
            return Holder.PLUGIN_BY_BLOCK_ID.get(blockId);
        }
    }

    public static final class Entity {
        private static HashMap<String, String> init() {
            var result = new HashMap<String, String>();

            var npcRoleAssetMap = BetterLookAtNPCRole.getAssetMap();

            for (var assetPackName : Holder.ASSET_PACK_NAMES) {
                var npcRoleKeys = npcRoleAssetMap.getKeysForPack(assetPackName);

                if (npcRoleKeys == null) {
                    continue;
                }

                for (var npcRoleKey : npcRoleKeys) {
                    result.put(npcRoleKey, normalizeAssetPackName(assetPackName));
                }
            }

            return result;
        }

        public static String getPlugin(String entityId) {
            return Holder.PLUGIN_BY_ENTITY_ID.get(entityId);
        }
    }
}
