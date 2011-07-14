package org.anddev.andengine.opengl.texture.bitmap;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTextureAtlas.BitmapTextureFormat;
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
public class BitmapTextureAtlasFactory {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public static BitmapTextureAtlas createForTextureSourceSize(final BitmapTextureFormat pBitmapTextureFormat, final TextureRegion pTextureRegion) {
		return BitmapTextureAtlasFactory.createForTextureRegionSize(pBitmapTextureFormat, pTextureRegion, TextureOptions.DEFAULT);
	}

	public static BitmapTextureAtlas createForTextureRegionSize(final BitmapTextureFormat pBitmapTextureFormat, final TextureRegion pTextureRegion, final TextureOptions pTextureOptions) {
		final int textureRegionWidth = pTextureRegion.getWidth();
		final int textureRegionHeight = pTextureRegion.getHeight();
		return new BitmapTextureAtlas(MathUtils.nextPowerOfTwo(textureRegionWidth), MathUtils.nextPowerOfTwo(textureRegionHeight), pBitmapTextureFormat, pTextureOptions);
	}

	public static BitmapTextureAtlas createForTextureSourceSize(final BitmapTextureFormat pBitmapTextureFormat, final IBitmapTextureSource pBitmapTextureSource) {
		return BitmapTextureAtlasFactory.createForTextureSourceSize(pBitmapTextureFormat, pBitmapTextureSource, TextureOptions.DEFAULT);
	}

	public static BitmapTextureAtlas createForTextureSourceSize(final BitmapTextureFormat pBitmapTextureFormat, final IBitmapTextureSource pBitmapTextureSource, final TextureOptions pTextureOptions) {
		final int textureSourceWidth = pBitmapTextureSource.getWidth();
		final int textureSourceHeight = pBitmapTextureSource.getHeight();
		return new BitmapTextureAtlas(MathUtils.nextPowerOfTwo(textureSourceWidth), MathUtils.nextPowerOfTwo(textureSourceHeight), pBitmapTextureFormat, pTextureOptions);
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
