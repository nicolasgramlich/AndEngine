package org.andengine.opengl.texture.atlas.bitmap.source;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;

import java.io.InputStream;

/**
 * (c) 20112 David Kovac
 *
 * @author David Kovac
 * @since 18:35:23 - 18.01.2012
 */
public abstract class DatabaseBitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    protected final int mWidth;
    protected final int mHeight;

    protected final Context mContext;
    protected final Uri mUri;

    // ===========================================================
    // Constructors
    // ===========================================================

    public DatabaseBitmapTextureAtlasSource(final Context pContext, final Uri pUri) {
        this(pContext, pUri, 0, 0);
    }

    public DatabaseBitmapTextureAtlasSource(final Context pContext, final Uri pUri, final int pTexturePositionX, final int pTexturePositionY) {
        super(pTexturePositionX, pTexturePositionY);
        this.mContext = pContext;
        this.mUri = pUri;

        final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inJustDecodeBounds = true;

        InputStream in = this.onLoadBitmapFromDatabase(this.mContext.getContentResolver(), this.mUri);
        BitmapFactory.decodeStream(in, null, decodeOptions);

        this.mWidth = decodeOptions.outWidth;
        this.mHeight = decodeOptions.outHeight;
    }

    protected DatabaseBitmapTextureAtlasSource(final Context pContext, final Uri pUri, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
        super(pTexturePositionX, pTexturePositionY);
        this.mContext = pContext;
        this.mUri = pUri;
        this.mWidth = pWidth;
        this.mHeight = pHeight;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public int getWidth() {
        return this.mWidth;
    }

    @Override
    public int getHeight() {
        return this.mHeight;
    }

    protected abstract InputStream onLoadBitmapFromDatabase(ContentResolver pContentResolver, Uri pUri);

    @Override
    public Bitmap onLoadBitmap(Bitmap.Config pBitmapConfig) {
        final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inPreferredConfig = pBitmapConfig;

        InputStream in = this.onLoadBitmapFromDatabase(this.mContext.getContentResolver(), this.mUri);

        return BitmapFactory.decodeStream(in, null, decodeOptions);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
