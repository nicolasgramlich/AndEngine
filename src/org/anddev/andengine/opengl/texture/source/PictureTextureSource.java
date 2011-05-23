package org.anddev.andengine.opengl.texture.source;

import org.anddev.andengine.util.Debug;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Picture;

/**
 * @author Nicolas Gramlich
 * @since 12:52:58 - 21.05.2011
 */
public abstract class PictureTextureSource implements ITextureSource {
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

	public PictureTextureSource(final Picture pPicture) {
		this(pPicture, pPicture.getWidth(), pPicture.getHeight());
	}

	public PictureTextureSource(final Picture pPicture, final int pWidth, final int pHeight) {
		this.mPicture = pPicture;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	@Override
	public abstract PictureTextureSource clone();

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
	public Bitmap onLoadBitmap() {
		final Picture picture = this.mPicture;
		if(picture == null) {
			Debug.e("Failed loading Bitmap in PictureTextureSource.");
			return null;
		}

		final Bitmap bitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, Config.ARGB_8888);
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
