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
	
	private ArrayList<TextureAtlas> mPendingTextureAtlas = new ArrayList<TextureAtlas>();

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

	public void addTextureAtlasPendingForBeingLoadedToHardware(final TextureAtlas pTextureAtlas) {
		this.mPendingTextureAtlas.add(pTextureAtlas);
	}
	
	public void loadPendingTextureAtlasToHardware(final GL10 pGL) {
		final ArrayList<TextureAtlas> pendingTextureAtlas = this.mPendingTextureAtlas;
		final int pendingTexutureAtlasCount = pendingTextureAtlas.size();
		if(pendingTexutureAtlasCount > 0){
			for(int i = 0; i < pendingTexutureAtlasCount; i++){
				if(!pendingTextureAtlas.get(i).isLoadedToHardware()){
					pendingTextureAtlas.get(i).loadToHardware(pGL);
				}
			}
			
			pendingTextureAtlas.clear();
			System.gc();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
