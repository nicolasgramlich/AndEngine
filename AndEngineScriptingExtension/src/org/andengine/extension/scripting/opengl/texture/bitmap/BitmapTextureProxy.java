package org.andengine.extension.scripting.opengl.texture.bitmap;

import java.io.IOException;

import org.andengine.opengl.texture.ITextureStateListener;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.util.adt.io.in.IInputStreamOpener;


public class BitmapTextureProxy extends BitmapTexture {
    private final long mAddress;

    public BitmapTextureProxy(final long pAddress,
        final TextureManager pTextureManager,
        final IInputStreamOpener pInputStreamOpener) throws IOException {
        super(pTextureManager, pInputStreamOpener);
        this.mAddress = pAddress;
    }

    public BitmapTextureProxy(final long pAddress,
        final TextureManager pTextureManager,
        final IInputStreamOpener pInputStreamOpener,
        final BitmapTextureFormat pBitmapTextureFormat)
        throws IOException {
        super(pTextureManager, pInputStreamOpener, pBitmapTextureFormat);
        this.mAddress = pAddress;
    }

    public BitmapTextureProxy(final long pAddress,
        final TextureManager pTextureManager,
        final IInputStreamOpener pInputStreamOpener,
        final TextureOptions pTextureOptions) throws IOException {
        super(pTextureManager, pInputStreamOpener, pTextureOptions);
        this.mAddress = pAddress;
    }

    public BitmapTextureProxy(final long pAddress,
        final TextureManager pTextureManager,
        final IInputStreamOpener pInputStreamOpener,
        final BitmapTextureFormat pBitmapTextureFormat,
        final TextureOptions pTextureOptions) throws IOException {
        super(pTextureManager, pInputStreamOpener, pBitmapTextureFormat,
            pTextureOptions);
        this.mAddress = pAddress;
    }

    public BitmapTextureProxy(final long pAddress,
        final TextureManager pTextureManager,
        final IInputStreamOpener pInputStreamOpener,
        final BitmapTextureFormat pBitmapTextureFormat,
        final TextureOptions pTextureOptions,
        final ITextureStateListener pTextureStateListener)
        throws IOException {
        super(pTextureManager, pInputStreamOpener, pBitmapTextureFormat,
            pTextureOptions, pTextureStateListener);
        this.mAddress = pAddress;
    }

    public static native void nativeInitClass();
}
