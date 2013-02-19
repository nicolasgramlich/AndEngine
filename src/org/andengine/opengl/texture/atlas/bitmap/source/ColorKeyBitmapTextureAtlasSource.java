package org.andengine.opengl.texture.atlas.bitmap.source;

import org.andengine.util.adt.color.Color;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 22:16:41 - 06.08.2010
 */
public class ColorKeyBitmapTextureAtlasSource extends ColorSwapBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorKeyBitmapTextureAtlasSource(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final Color pColorKeyColor) {
		super(pBitmapTextureAtlasSource, pColorKeyColor, Color.TRANSPARENT);
	}

	public ColorKeyBitmapTextureAtlasSource(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final int pColorKeyColorARGBPackedInt) {
		super(pBitmapTextureAtlasSource, pColorKeyColorARGBPackedInt, Color.TRANSPARENT_ARGB_PACKED_INT);
	}

	public ColorKeyBitmapTextureAtlasSource(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final Color pColorKeyColor, final int pTolerance) {
		super(pBitmapTextureAtlasSource, pColorKeyColor, pTolerance, Color.TRANSPARENT);
	}

	public ColorKeyBitmapTextureAtlasSource(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final int pColorKeyColorARGBPackedInt, final int pTolerance) {
		super(pBitmapTextureAtlasSource, pColorKeyColorARGBPackedInt, pTolerance, Color.TRANSPARENT_ARGB_PACKED_INT);
	}

	@Override
	public ColorKeyBitmapTextureAtlasSource deepCopy() {
		return new ColorKeyBitmapTextureAtlasSource(this.mBitmapTextureAtlasSource, this.mColorKeyColorARGBPackedInt, this.mTolerance);
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
