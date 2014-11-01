package org.andengine.extension.scripting.opengl.texture.bitmap;

import java.io.IOException;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.bitmap.ResourceBitmapTexture;

import android.content.res.Resources;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:27:41 - 05.03.2012
 */
public class ResourceBitmapTextureProxy extends ResourceBitmapTexture {
	// ===========================================================
	// Constants
	// ===========================================================

	public static native void nativeInitClass();

	// ===========================================================
	// Fields
	// ===========================================================

	private final long mAddress;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ResourceBitmapTextureProxy(final long pAddress, final TextureManager pTextureManager, final Resources pResources, final int pDrawableResourceID) throws IOException {
		super(pTextureManager, pResources, pDrawableResourceID);

		this.mAddress = pAddress;
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
