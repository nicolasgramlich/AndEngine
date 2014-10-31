package org.andengine.extension.texturepacker.opengl.texture.util.texturepacker;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.TextureManager;

import android.content.res.AssetManager;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:24:00 - 12.09.2011
 */
public class AssetTexturePackParser extends TexturePackParser {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final AssetManager mAssetManager;
	private final String mAssetBasePath;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AssetTexturePackParser(final TextureManager pTextureManager, final AssetManager pAssetManager, final String pAssetBasePath) {
		super(pTextureManager);

		this.mAssetManager = pAssetManager;
		this.mAssetBasePath = pAssetBasePath;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected InputStream onGetInputStream(final String pFilename) throws IOException {
		return this.mAssetManager.open(this.mAssetBasePath + pFilename);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
