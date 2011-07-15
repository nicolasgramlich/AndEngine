package org.anddev.andengine.opengl.texture.atlas.bitmap;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas.BitmapTextureFormat;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
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

	public static BitmapTextureAtlas createForTextureAtlasSourceSize(final BitmapTextureFormat pBitmapTextureFormat, final TextureRegion pTextureRegion) {
		return BitmapTextureAtlasFactory.createForTextureRegionSize(pBitmapTextureFormat, pTextureRegion, TextureOptions.DEFAULT);
	}

	public static BitmapTextureAtlas createForTextureRegionSize(final BitmapTextureFormat pBitmapTextureFormat, final TextureRegion pTextureRegion, final TextureOptions pTextureOptions) {
		final int textureRegionWidth = pTextureRegion.getWidth();
		final int textureRegionHeight = pTextureRegion.getHeight();
		return new BitmapTextureAtlas(MathUtils.nextPowerOfTwo(textureRegionWidth), MathUtils.nextPowerOfTwo(textureRegionHeight), pBitmapTextureFormat, pTextureOptions);
	}

	public static BitmapTextureAtlas createForTextureAtlasSourceSize(final BitmapTextureFormat pBitmapTextureFormat, final IBitmapTextureAtlasSource pBitmapTextureAtlasSource) {
		return BitmapTextureAtlasFactory.createForTextureAtlasSourceSize(pBitmapTextureFormat, pBitmapTextureAtlasSource, TextureOptions.DEFAULT);
	}

	public static BitmapTextureAtlas createForTextureAtlasSourceSize(final BitmapTextureFormat pBitmapTextureFormat, final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final TextureOptions pTextureOptions) {
		final int textureSourceWidth = pBitmapTextureAtlasSource.getWidth();
		final int textureSourceHeight = pBitmapTextureAtlasSource.getHeight();
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
