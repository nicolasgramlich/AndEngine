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
	
	private ArrayList<Texture> mPendingTextures = new ArrayList<Texture>();
	private ArrayList<Texture> mLoadedTextures = new ArrayList<Texture>();

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public TextureManager() {
		
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

	public void addTexturePendingForBeingLoadedToHardware(final Texture pTexture) {
		this.mPendingTextures.add(pTexture);
	}
	
	public void reloadLoadedToPendingTextures() {		
		final ArrayList<Texture> loadedTextures = this.mLoadedTextures;
		for(int i = loadedTextures.size() - 1; i >= 0; i--)
			loadedTextures.get(i).setLoadedToHardware(false);

		this.mPendingTextures.addAll(loadedTextures);
		
		loadedTextures.clear();
	}
	
	public void loadPendingTextureToHardware(final GL10 pGL) {
		final ArrayList<Texture> pendingTextures = this.mPendingTextures;
		final int pendingTexutureCount = pendingTextures.size();
		if(pendingTexutureCount > 0){
			for(int i = 0; i < pendingTexutureCount; i++){
				final Texture pendingTexture = pendingTextures.get(i);
				if(!pendingTexture.isLoadedToHardware()){
					pendingTexture.loadToHardware(pGL);
					this.mLoadedTextures.add(pendingTexture);
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
