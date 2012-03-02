package org.andengine.opengl.texture.bitmap;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.TextureManager;

import android.content.Context;
import android.content.res.Resources;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 10:45:48 - 02.03.2012
 */
public class ResourceBitmapTexture extends BitmapTexture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Resources mResources;
	private final int mDrawableResourceID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ResourceBitmapTexture(final TextureManager pTextureManager, final Context pContext, final int pDrawableResourceID) throws IOException {
		this(pTextureManager, pContext.getResources(), pDrawableResourceID);
	}

	public ResourceBitmapTexture(final TextureManager pTextureManager, final Resources pResources, final int pDrawableResourceID) throws IOException {
		super(pTextureManager);

		this.mResources = pResources;
		this.mDrawableResourceID = pDrawableResourceID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected InputStream onGetInputStream() throws IOException {
		return this.mResources.openRawResource(this.mDrawableResourceID);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
