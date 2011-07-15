package org.anddev.andengine.opengl.texture.atlas.bitmap.source;

import org.anddev.andengine.opengl.texture.source.BaseTextureAtlasSource;
import org.anddev.andengine.util.Debug;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Picture;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:52:58 - 21.05.2011
 */
public abstract class PictureBitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final Picture mPicture;
	protected final int mWidth;
	protected final int mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PictureBitmapTextureAtlasSource(final Picture pPicture) {
		this(pPicture, 0, 0);
	}
	
	public PictureBitmapTextureAtlasSource(final Picture pPicture, final int pTexturePositionX, final int pTexturePositionY) {
		this(pPicture, pTexturePositionX, pTexturePositionY, pPicture.getWidth(), pPicture.getHeight());
	}

	public PictureBitmapTextureAtlasSource(final Picture pPicture, final int pTexturePositionX, final int pTexturePositionY, final float pScale) {
		this(pPicture, pTexturePositionX, pTexturePositionY, Math.round(pPicture.getWidth() * pScale), Math.round(pPicture.getHeight() * pScale));
	}

	public PictureBitmapTextureAtlasSource(final Picture pPicture, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		super(pTexturePositionX, pTexturePositionY);
		this.mPicture = pPicture;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public abstract PictureBitmapTextureAtlasSource clone();

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

	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig) {
		final Picture picture = this.mPicture;
		if(picture == null) {
			Debug.e("Failed loading Bitmap in PictureBitmapTextureAtlasSource.");
			return null;
		}

		final Bitmap bitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, pBitmapConfig);
		final Canvas canvas = new Canvas(bitmap);

		final float scaleX = (float)this.mWidth / this.mPicture.getWidth();
		final float scaleY = (float)this.mHeight / this.mPicture.getHeight();
		canvas.scale(scaleX, scaleY, 0, 0);

		picture.draw(canvas);

		return bitmap;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
