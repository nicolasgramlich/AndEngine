package org.andengine.extension.scripting.opengl.texture.region;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.BaseTextureRegion;


public abstract class BaseTextureRegionProxy extends BaseTextureRegion {
    private final long mAddress;

    public BaseTextureRegionProxy(final long pAddress, final ITexture pTexture) {
        super(pTexture);
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
