/**
 * 
 */
package org.anddev.andengine.opengl.text;

/**
 * @author Nicolas Gramlich
 * @since 10:30:22 - 03.04.2010
 */
public class Glyph {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public final int mAdvance;
	public final int mWidth;
	public final int mHeight;
	public final float mTextureX;
	public final float mTextureY;
	public final float mTextureWidth;
	public final float mTextureHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Glyph(final int pAdvance, final int pWidth, final int pHeight, final float pTextureU, final float pTextureV, final float pTextureWidthU, final float pTextureHeightV) {
		this.mAdvance = pAdvance;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.mTextureX = pTextureU;
		this.mTextureY = pTextureV;
		this.mTextureWidth = pTextureWidthU;
		this.mTextureHeight = pTextureHeightV;
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