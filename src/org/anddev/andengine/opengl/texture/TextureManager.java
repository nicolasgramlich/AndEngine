package org.anddev.andengine.opengl.texture;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Nicolas Gramlich
 * @since 17:48:46 - 08.03.2010
 */
public class TextureManager {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static final ArrayList<Texture> mTexturesToBeLoaded = new ArrayList<Texture>();
	private static final ArrayList<Texture> mLoadedTextures = new ArrayList<Texture>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static void clear() {
		TextureManager.mTexturesToBeLoaded.clear();
		TextureManager.mLoadedTextures.clear();
	}

	public static void loadTexture(final Texture pTexture) {
		TextureManager.mTexturesToBeLoaded.add(pTexture);
	}

	public static void loadTextures(final Texture ... pTextures) {
		for(int i = pTextures.length - 1; i >= 0; i--) {
			TextureManager.mTexturesToBeLoaded.add(pTextures[i]);
		}
	}

	public static void reloadTextures() {
		final ArrayList<Texture> loadedTextures = TextureManager.mLoadedTextures;
		for(int i = loadedTextures.size() - 1; i >= 0; i--) {
			loadedTextures.get(i).setLoadedToHardware(false);
		}

		TextureManager.mTexturesToBeLoaded.addAll(loadedTextures);

		loadedTextures.clear();
	}

	public static void ensureTexturesLoadedToHardware(final GL10 pGL) {
		final ArrayList<Texture> pendingTextures = TextureManager.mTexturesToBeLoaded;
		final int pendingTextureCount = pendingTextures.size();
		if(pendingTextureCount > 0){
			for(int i = 0; i < pendingTextureCount; i++){
				final Texture pendingTexture = pendingTextures.get(i);
				if(!pendingTexture.isLoadedToHardware()){
					pendingTexture.loadToHardware(pGL);
					TextureManager.mLoadedTextures.add(pendingTexture);
				}
			}

			pendingTextures.clear();
			System.gc();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
