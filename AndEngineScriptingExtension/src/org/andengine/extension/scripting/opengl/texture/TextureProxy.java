package org.andengine.extension.scripting.opengl.texture;

import org.andengine.opengl.texture.ITextureStateListener;
import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;


public abstract class TextureProxy extends Texture {
    private final long mAddress;

    public TextureProxy(final long pAddress,
        final TextureManager pTextureManager, final PixelFormat pPixelFormat,
        final TextureOptions pTextureOptions,
        final ITextureStateListener pTextureStateListener)
        throws IllegalArgumentException {
        super(pTextureManager, pPixelFormat, pTextureOptions,
            pTextureStateListener);
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
