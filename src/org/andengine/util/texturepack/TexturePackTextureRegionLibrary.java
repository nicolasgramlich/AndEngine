package org.andengine.util.texturepack;

import java.util.HashMap;

import android.util.SparseArray;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:34:23 - 15.08.2011
 */
public class TexturePackTextureRegionLibrary {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SparseArray<TexturePackTextureRegion> mIDMapping;
	private final HashMap<String, TexturePackTextureRegion> mSourceMapping;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TexturePackTextureRegionLibrary(final int pInitialCapacity) {
		this.mIDMapping = new SparseArray<TexturePackTextureRegion>(pInitialCapacity);
		this.mSourceMapping = new HashMap<String, TexturePackTextureRegion>(pInitialCapacity);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public SparseArray<TexturePackTextureRegion> getIDMapping() {
		return this.mIDMapping;
	}

	public HashMap<String, TexturePackTextureRegion> getSourceMapping() {
		return this.mSourceMapping;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void put(final TexturePackTextureRegion pTexturePackTextureRegion) {
		this.throwOnCollision(pTexturePackTextureRegion);

		this.mIDMapping.put(pTexturePackTextureRegion.getID(), pTexturePackTextureRegion);
		this.mSourceMapping.put(pTexturePackTextureRegion.getSource(), pTexturePackTextureRegion);
	}

	public void remove(final int pID) {
		this.mIDMapping.remove(pID);
	}

	public TexturePackTextureRegion get(final int pID) {
		return this.mIDMapping.get(pID);
	}

	public TexturePackTextureRegion get(final String pSource) {
		return this.mSourceMapping.get(pSource);
	}

	public TexturePackTextureRegion get(final String pSource, final boolean pStripExtension) {
		if (pStripExtension) {
			final int indexOfExtension = pSource.lastIndexOf('.');
			if (indexOfExtension == -1) {
				return this.get(pSource);
			} else {
				final String stripped = pSource.substring(0, indexOfExtension);
				return this.mSourceMapping.get(stripped);
			}
		} else {
			return this.get(pSource);
		}
	}

	private void throwOnCollision(final TexturePackTextureRegion pTexturePackTextureRegion) throws IllegalArgumentException {
		if (this.mIDMapping.get(pTexturePackTextureRegion.getID()) != null) {
			throw new IllegalArgumentException("Collision with ID: '" + pTexturePackTextureRegion.getID() + "'.");
		} else if (this.mSourceMapping.get(pTexturePackTextureRegion.getSource()) != null) {
			throw new IllegalArgumentException("Collision with Source: '" + pTexturePackTextureRegion.getSource() + "'.");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
