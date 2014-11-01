package org.andengine.extension.scripting.opengl.texture;

import org.andengine.opengl.texture.TextureManager;


public class TextureManagerProxy extends TextureManager {
    private final long mAddress;

    public TextureManagerProxy(final long pAddress) {
        super();
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
