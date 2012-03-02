package org.andengine.opengl.texture.bitmap;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.TextureManager;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 10:45:48 - 02.03.2012
 */
public class AssetBitmapTexture extends BitmapTexture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final AssetManager mAssetManager;
	private final String mAssetPath;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AssetBitmapTexture(final TextureManager pTextureManager, final Context pContext, final String pAssetPath) throws IOException {
		this(pTextureManager, pContext.getAssets(), pAssetPath);
	}

	public AssetBitmapTexture(final TextureManager pTextureManager, final AssetManager pAssetManager, final String pAssetPath) throws IOException {
		super(pTextureManager);

		this.mAssetManager = pAssetManager;
		this.mAssetPath = pAssetPath;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected InputStream onGetInputStream() throws IOException {
		return this.mAssetManager.open(this.mAssetPath);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
