package org.anddev.andengine.opengl.texture.region;

import org.anddev.andengine.util.Library;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:52:26 - 20.08.2010
 */
public class TextureRegionLibrary extends Library<BaseTextureRegion> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureRegionLibrary() {
		super();
	}

	public TextureRegionLibrary(final int pInitialCapacity) {
		super(pInitialCapacity);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public TextureRegion get(final int pID) {
		return (TextureRegion) super.get(pID);
	}

	public TiledTextureRegion getTiled(final int pID) {
		return (TiledTextureRegion) this.mItems.get(pID);
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
