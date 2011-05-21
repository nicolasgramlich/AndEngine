package org.anddev.andengine.opengl.texture.source;

import org.anddev.andengine.util.Debug;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Bitmap.Config;

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

	private final Picture mPicture;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PictureTextureSource(final Picture pPicture) {
		this.mPicture = pPicture;
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
	public int getHeight() {
		return this.mPicture.getHeight();
	}

	@Override
	public int getWidth() {
		return this.mPicture.getWidth();
	}

	@Override
	public Bitmap onLoadBitmap() {
		final Picture picture = this.mPicture;
		if(picture == null) {
			Debug.e("Failed loading Bitmap in PictureTextureSource.");
			return null;
		}

		final Bitmap bitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);
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
