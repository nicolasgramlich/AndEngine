package org.anddev.andengine.opengl.texture.region;

import android.util.SparseArray;

/**
 * @author Nicolas Gramlich
 * @since 13:00:24 - 16.08.2010
 */
public class TextureRegionLibrary {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final SparseArray<TextureRegion> mTextureRegions = new SparseArray<TextureRegion>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public void put(final int pID, final TextureRegion pTextureRegion) {
		this.mTextureRegions.put(pID, pTextureRegion);
	}
	
	public TextureRegion get(final int pID) {
		return this.mTextureRegions.get(pID);
	}
	
	public TiledTextureRegion getTiled(final int pID) {
		return (TiledTextureRegion)this.mTextureRegions.get(pID);
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
