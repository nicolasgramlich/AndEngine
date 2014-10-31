package org.andengine.extension.scripting.opengl.texture;

import org.andengine.opengl.texture.TextureOptions;


public class TextureOptionsProxy extends TextureOptions {
    private final long mAddress;

    public TextureOptionsProxy(final long pAddress, final int pMinFilter,
        final int pMagFilter, final int pWrapT, final int pWrapS,
        final boolean pPreMultiplyAlpha) {
        super(pMinFilter, pMagFilter, pWrapT, pWrapS, pPreMultiplyAlpha);
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
