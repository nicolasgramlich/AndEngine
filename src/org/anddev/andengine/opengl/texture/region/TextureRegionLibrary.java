package org.anddev.andengine.opengl.texture.region;

import org.anddev.andengine.opengl.texture.ITexture;
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

	private final ITexture mTexture;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureRegionLibrary(final ITexture pTexture) {
		super();

		this.mTexture = pTexture;
	}

	public TextureRegionLibrary(final ITexture pTexture, final int pInitialCapacity) {
		super(pInitialCapacity);

		this.mTexture = pTexture;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public ITexture getTexture() {
		return mTexture;
	}

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
