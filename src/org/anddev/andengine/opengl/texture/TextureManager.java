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
	
	public void loadPendingTextureToHardware(final GL10 pGL) {
		final ArrayList<Texture> pendingTextures = this.mPendingTextures;
		final int pendingTexutureCount = pendingTextures.size();
		if(pendingTexutureCount > 0){
			for(int i = 0; i < pendingTexutureCount; i++){
				if(!pendingTextures.get(i).isLoadedToHardware()){
					pendingTextures.get(i).loadToHardware(pGL);
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
