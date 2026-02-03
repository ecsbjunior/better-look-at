package com.junior.evandro.utils;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;

public class BetterLookAtNPCRole implements JsonAssetWithMap<String, DefaultAssetMap<String, BetterLookAtNPCRole>> {
    public static final AssetBuilderCodec<String, BetterLookAtNPCRole> CODEC = AssetBuilderCodec.builder(
        BetterLookAtNPCRole.class,
        BetterLookAtNPCRole::new,
        Codec.STRING,
        (npcRole, id) -> npcRole.id = id,
        (npcRole) -> npcRole.id,
        (npcRole, data) -> npcRole.data = data,
        (npcRole) -> npcRole.data
    )
    .build();

    private static AssetStore<String, BetterLookAtNPCRole, DefaultAssetMap<String, BetterLookAtNPCRole>> ASSET_STORE;

    protected String id;
    protected AssetExtraInfo.Data data;

    public BetterLookAtNPCRole(String id) {
        this.id = id;
    }

    protected BetterLookAtNPCRole() {

    }

    public static AssetStore<String, BetterLookAtNPCRole, DefaultAssetMap<String, BetterLookAtNPCRole>> getAssetStore() {
        if (ASSET_STORE == null) {
            ASSET_STORE = AssetRegistry.getAssetStore(BetterLookAtNPCRole.class);
        }

        return ASSET_STORE;
    }

    public static DefaultAssetMap<String, BetterLookAtNPCRole> getAssetMap() {
        return BetterLookAtNPCRole.getAssetStore().getAssetMap();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
