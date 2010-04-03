/**
 * 
 */
package org.anddev.andengine.opengl.text;

/**
 * @author Nicolas Gramlich
 * @since 10:30:22 - 03.04.2010
 */
public class Letter {
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

	public Letter(final int pAdvance, final int pWidth, final int pHeight, final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight) {
		this.mAdvance = pAdvance;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.mTextureX = pTextureX;
		this.mTextureY = pTextureY;
		this.mTextureWidth = pTextureWidth;
		this.mTextureHeight = pTextureHeight;
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