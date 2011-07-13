package org.anddev.andengine.opengl.texture.bitmap;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.TextureFormat;
import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 09:38:51 - 03.05.2010
 */
public class BitmapTextureFactory {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public static BitmapTexture createForTextureSourceSize(final TextureFormat pTextureFormat, final TextureRegion pTextureRegion) {
		return BitmapTextureFactory.createForTextureRegionSize(pTextureFormat, pTextureRegion, TextureOptions.DEFAULT);
	}

	public static BitmapTexture createForTextureRegionSize(final TextureFormat pTextureFormat, final TextureRegion pTextureRegion, final TextureOptions pTextureOptions) {
		final int textureRegionWidth = pTextureRegion.getWidth();
		final int textureRegionHeight = pTextureRegion.getHeight();
		return new BitmapTexture(MathUtils.nextPowerOfTwo(textureRegionWidth), MathUtils.nextPowerOfTwo(textureRegionHeight), pTextureFormat, pTextureOptions);
	}

	public static BitmapTexture createForTextureSourceSize(final TextureFormat pTextureFormat, final IBitmapTextureSource pBitmapTextureSource) {
		return BitmapTextureFactory.createForTextureSourceSize(pTextureFormat, pBitmapTextureSource, TextureOptions.DEFAULT);
	}

	public static BitmapTexture createForTextureSourceSize(final TextureFormat pTextureFormat, final IBitmapTextureSource pBitmapTextureSource, final TextureOptions pTextureOptions) {
		final int textureSourceWidth = pBitmapTextureSource.getWidth();
		final int textureSourceHeight = pBitmapTextureSource.getHeight();
		return new BitmapTexture(MathUtils.nextPowerOfTwo(textureSourceWidth), MathUtils.nextPowerOfTwo(textureSourceHeight), pTextureFormat, pTextureOptions);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
