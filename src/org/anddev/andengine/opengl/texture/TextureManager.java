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

	private final HashSet<Texture> mTexturesManaged = new HashSet<Texture>();

	private final ArrayList<Texture> mTexturesLoaded = new ArrayList<Texture>();

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
		this.mTexturesLoaded.clear();
		this.mTexturesManaged.clear();
	}

	/**
	 * @param pTexture the {@link Texture} to be loaded before the very next frame is drawn (Or prevent it from being unloaded then).
	 * @return <code>true</code> when the {@link Texture} was previously not managed by this {@link TextureManager}, <code>false</code> if it was already managed.
	 */
	public boolean loadTexture(final Texture pTexture) {
		if(this.mTexturesManaged.contains(pTexture)) {
			/* Just make sure it doesn't get deleted. */
			this.mTexturesToBeUnloaded.remove(pTexture);
			return false;
		} else {
			this.mTexturesManaged.add(pTexture);
			this.mTexturesToBeLoaded.add(pTexture);
			return true;
		}
	}

	/**
	 * @param pTexture the {@link Texture} to be unloaded before the very next frame is drawn (Or prevent it from being loaded then).
	 * @return <code>true</code> when the {@link Texture} was already managed by this {@link TextureManager}, <code>false</code> if it was not managed.
	 */
	public boolean unloadTexture(final Texture pTexture) {
		if(this.mTexturesManaged.contains(pTexture)) {
			/* If the Texture is loaded, unload it.
			 * If the Texture is about to be loaded, stop it from being loaded. */
			if(this.mTexturesLoaded.contains(pTexture)){
				this.mTexturesToBeUnloaded.add(pTexture);
			} else if(this.mTexturesToBeLoaded.remove(pTexture)){
				this.mTexturesManaged.remove(pTexture);
			}
			return true;
		} else {
			return false;
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
		final HashSet<Texture> managedTextures = this.mTexturesManaged;
		for(final Texture texture : managedTextures) { // TODO Can the use of the iterator be avoided somehow?
			texture.setLoadedToHardware(false);
		}

		this.mTexturesToBeLoaded.addAll(this.mTexturesLoaded); // TODO Check if addAll uses iterator internally!
		this.mTexturesLoaded.clear();

		this.mTexturesManaged.removeAll(this.mTexturesToBeUnloaded); // TODO Check if removeAll uses iterator internally!
		this.mTexturesToBeUnloaded.clear();
	}

	public void updateTextures(final GL10 pGL) {
		final HashSet<Texture> texturesManaged = this.mTexturesManaged;
		final ArrayList<Texture> texturesLoaded = this.mTexturesLoaded;
		final ArrayList<Texture> texturesToBeLoaded = this.mTexturesToBeLoaded;
		final ArrayList<Texture> texturesToBeUnloaded = this.mTexturesToBeUnloaded;

		/* First reload Textures that need to be updated. */
		final int textursLoadedCount = texturesLoaded.size();

		if(textursLoadedCount > 0){
			for(int i = textursLoadedCount - 1; i >= 0; i--){
				final Texture textureToBeUpdated = texturesLoaded.get(i);
				if(textureToBeUpdated.isUpdateOnHardwareNeeded()){
					textureToBeUpdated.unloadFromHardware(pGL);
					textureToBeUpdated.loadToHardware(pGL);
				}
			}
		}

		/* Then load pending Textures. */
		final int texturesToBeLoadedCount = texturesToBeLoaded.size();

		if(texturesToBeLoadedCount > 0){
			for(int i = texturesToBeLoadedCount - 1; i >= 0; i--){
				final Texture textureToBeLoaded = texturesToBeLoaded.remove(i);
				if(!textureToBeLoaded.isLoadedToHardware()){
					textureToBeLoaded.loadToHardware(pGL);
				}
				texturesLoaded.add(textureToBeLoaded);
			}
		}

		/* Then unload pending Textures. */
		final int texturesToBeUnloadedCount = texturesToBeUnloaded.size();

		if(texturesToBeUnloadedCount > 0){
			for(int i = texturesToBeUnloadedCount - 1; i >= 0; i--){
				final Texture textureToBeUnloaded = texturesToBeUnloaded.remove(i);
				if(textureToBeUnloaded.isLoadedToHardware()){
					textureToBeUnloaded.unloadFromHardware(pGL);
				}
				texturesLoaded.remove(textureToBeUnloaded);
				texturesManaged.remove(textureToBeUnloaded);
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
