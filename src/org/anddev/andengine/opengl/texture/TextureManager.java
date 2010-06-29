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

	private final HashSet<Texture> mManagedTextures = new HashSet<Texture>();

	private final ArrayList<Texture> mLoadedTextures = new ArrayList<Texture>();

	private final ArrayList<Texture> mTexturesToBeLoaded = new ArrayList<Texture>();
	private final ArrayList<Texture> mTexturesToBeUnloaded = new ArrayList<Texture>();

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

	public void unloadTexture(final Texture pTexture) {
		if(this.mManagedTextures.contains(pTexture) && this.mLoadedTextures.contains(pTexture) && !this.mTexturesToBeUnloaded.contains(pTexture)){
			this.mTexturesToBeUnloaded.add(pTexture);
		}
	}

	public void loadTextures(final Texture ... pTextures) {
		for(int i = pTextures.length - 1; i >= 0; i--) {
			this.loadTexture(pTextures[i]);
		}
	}
	
	public void unloadTextures(final Texture ... pTextures) {
		for(int i = pTextures.length - 1; i >= 0; i--) {
			this.unloadTexture(pTextures[i]);
		}
	}

	public void reloadTextures() {
		final HashSet<Texture> managedTextures = this.mManagedTextures;
		for(final Texture texture : managedTextures) { // TODO Can the use of the iterator be avoided somehow?
			texture.setLoadedToHardware(false);
		}

		this.mTexturesToBeLoaded.addAll(this.mLoadedTextures); // TODO Check if addAll uses iterator internally!
		this.mLoadedTextures.clear();
		
		this.mManagedTextures.removeAll(this.mTexturesToBeUnloaded); // TODO Check if removeAll uses iterator internally!
		this.mTexturesToBeUnloaded.clear();
	}

	public void ensureTexturesLoadedToHardware(final GL10 pGL) {
		final ArrayList<Texture> loadedTextures = this.mLoadedTextures;

		final ArrayList<Texture> texturesToBeLoaded = this.mTexturesToBeLoaded;
		final int texturesToBeLoadedCount = texturesToBeLoaded.size();
		
		if(texturesToBeLoadedCount > 0){
			for(int i = texturesToBeLoadedCount - 1; i >= 0; i--){
				final Texture textureToBeLoaded = texturesToBeLoaded.remove(i);
				if(!textureToBeLoaded.isLoadedToHardware()){
					textureToBeLoaded.loadToHardware(pGL);
				}
				loadedTextures.add(textureToBeLoaded);
			}
		}
		
		/* */
		final HashSet<Texture> managedTextures = this.mManagedTextures;
		final ArrayList<Texture> texturesToBeUnloaded = this.mTexturesToBeUnloaded;
		final int texturesToBeUnloadedCount = texturesToBeUnloaded.size();
		
		if(texturesToBeUnloadedCount > 0){
			for(int i = texturesToBeUnloadedCount - 1; i >= 0; i--){
				final Texture textureToBeUnloaded = texturesToBeUnloaded.remove(i);
				if(textureToBeUnloaded.isLoadedToHardware()){
					textureToBeUnloaded.unloadFromHardware(pGL);
				}
				loadedTextures.remove(textureToBeUnloaded);
				managedTextures.remove(textureToBeUnloaded);
			}
		}

		/* Finally invoke the GC if anything has changed. */
		if(texturesToBeLoadedCount > 0 || texturesToBeUnloadedCount > 0){
			System.gc();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
