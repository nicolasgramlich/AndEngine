package org.andengine.extension.scripting.opengl.texture.region;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.TextureRegion;


public class TextureRegionProxy extends TextureRegion {
    private final long mAddress;

    public TextureRegionProxy(final long pAddress, final ITexture pTexture,
        final float pTextureX, final float pTextureY,
        final float pTextureWidth, final float pTextureHeight,
        final boolean pRotated) {
        super(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight,
            pRotated);
        this.mAddress = pAddress;
    }

    public TextureRegionProxy(final long pAddress, final ITexture pTexture,
        final float pTextureX, final float pTextureY,
        final float pTextureWidth, final float pTextureHeight) {
        super(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight);
        this.mAddress = pAddress;
    }

    public TextureRegionProxy(final long pAddress, final ITexture pTexture,
        final float pTextureX, final float pTextureY,
        final float pTextureWidth, final float pTextureHeight,
        final float pScale) {
        super(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight,
            pScale);
        this.mAddress = pAddress;
    }

    public TextureRegionProxy(final long pAddress, final ITexture pTexture,
        final float pTextureX, final float pTextureY,
        final float pTextureWidth, final float pTextureHeight,
        final float pScale, final boolean pRotated) {
        super(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight,
            pScale, pRotated);
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
