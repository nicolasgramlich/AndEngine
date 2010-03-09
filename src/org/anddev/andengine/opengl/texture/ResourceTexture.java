package org.anddev.andengine.opengl.texture;

import org.anddev.andengine.opengl.texture.source.ResourceTextureSource;

import android.content.Context;

/**
 * @author Nicolas Gramlich
 * @since 15:10:01 - 09.03.2010
 */
public class ResourceTexture extends Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ResourceTexture(final Context pContext, final int pDrawableResourceID) {
		super(new ResourceTextureSource(pContext, pDrawableResourceID));
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
