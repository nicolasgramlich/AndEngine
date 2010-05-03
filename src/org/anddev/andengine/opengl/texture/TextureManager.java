package org.anddev.andengine.opengl.texture;

import java.util.ArrayList;
import java.util.HashSet;

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

	private final ArrayList<Texture> mTexturesToBeLoaded = new ArrayList<Texture>();
	private final ArrayList<Texture> mLoadedTextures = new ArrayList<Texture>();

	private final HashSet<Texture> mManagedTextures = new HashSet<Texture>();

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

	protected void clear() {
		this.mTexturesToBeLoaded.clear();
		this.mLoadedTextures.clear();
		this.mManagedTextures.clear();
	}

	public void loadTexture(final Texture pTexture) {
		if(this.mManagedTextures.contains(pTexture) == false) {
			this.mManagedTextures.add(pTexture);
			this.mTexturesToBeLoaded.add(pTexture);
		}
	}

	public void loadTextures(final Texture ... pTextures) {
		for(int i = pTextures.length - 1; i >= 0; i--) {
			this.loadTexture(pTextures[i]);
		}
	}

	public void reloadTextures() {
		final ArrayList<Texture> loadedTextures = this.mLoadedTextures;
		for(int i = loadedTextures.size() - 1; i >= 0; i--) {
			loadedTextures.get(i).setLoadedToHardware(false);
		}

		this.mTexturesToBeLoaded.addAll(loadedTextures);

		loadedTextures.clear();
	}

	public void ensureTexturesLoadedToHardware(final GL10 pGL) {
		final ArrayList<Texture> pendingTextures = this.mTexturesToBeLoaded;
		final int pendingTextureCount = pendingTextures.size();
		if(pendingTextureCount > 0){
			for(int i = 0; i < pendingTextureCount; i++){
				final Texture pendingTexture = pendingTextures.get(i);
				if(!pendingTexture.isLoadedToHardware()){
					pendingTexture.loadToHardware(pGL);
				}
				this.mLoadedTextures.add(pendingTexture);
			}

			pendingTextures.clear();
			System.gc();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
