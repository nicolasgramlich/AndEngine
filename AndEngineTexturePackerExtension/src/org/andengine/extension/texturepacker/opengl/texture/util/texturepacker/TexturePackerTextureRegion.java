package org.andengine.extension.texturepacker.opengl.texture.util.texturepacker;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.TextureRegion;

/**
 * 
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:28:48 - 15.08.2011
 */
public class TexturePackerTextureRegion extends TextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mID;
	private final String mSource;
	private final boolean mTrimmed;
	private final int mSourceX;
	private final int mSourceY;
	private final int mSourceWidth;
	private final int mSourceHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TexturePackerTextureRegion(final ITexture pTexture, final int pX, final int pY, final int pWidth, final int pHeight, final int pID, final String pSource, final boolean pRotated, final boolean pTrimmed, final int pSourceX, final int pSourceY, final int pSourceWidth, final int pSourceHeight) {
		super(pTexture, pX, pY, pWidth, pHeight, pRotated);
		this.mID = pID;
		this.mSource = pSource;
		this.mTrimmed = pTrimmed;
		this.mSourceX = pSourceX;
		this.mSourceY = pSourceY;
		this.mSourceWidth = pSourceWidth;
		this.mSourceHeight = pSourceHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getID() {
		return this.mID;
	}

	public String getSource() {
		return this.mSource;
	}

	public boolean isTrimmed() {
		return this.mTrimmed;
	}

	public int getSourceX() {
		return this.mSourceX;
	}

	public int getSourceY() {
		return this.mSourceY;
	}

	public int getSourceWidth() {
		return this.mSourceWidth;
	}

	public int getSourceHeight() {
		return this.mSourceHeight;
	}

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
