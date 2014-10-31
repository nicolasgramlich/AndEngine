package org.andengine.extension.scripting.util.adt.io.in;

import android.content.res.AssetManager;

import org.andengine.util.adt.io.in.AssetInputStreamOpener;


public class AssetInputStreamOpenerProxy extends AssetInputStreamOpener {
    private final long mAddress;

    public AssetInputStreamOpenerProxy(final long pAddress,
        final AssetManager pAssetManager, final String pAssetPath) {
        super(pAssetManager, pAssetPath);
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
