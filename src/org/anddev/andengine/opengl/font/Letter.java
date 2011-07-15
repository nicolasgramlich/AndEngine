/**
 * 
 */
package org.anddev.andengine.opengl.font;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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
	public final char mCharacter;

	// ===========================================================
	// Constructors
	// ===========================================================

	Letter(final char pCharacter, final int pAdvance, final int pWidth, final int pHeight, final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight) {
		this.mCharacter = pCharacter;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.mCharacter;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(this.getClass() != obj.getClass()) {
			return false;
		}
		final Letter other = (Letter) obj;
		if(this.mCharacter != other.mCharacter) {
			return false;
		}
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}