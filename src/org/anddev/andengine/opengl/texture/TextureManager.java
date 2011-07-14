package org.anddev.andengine.opengl.texture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.util.Debug;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	private final HashSet<ITexture> mTexturesManaged = new HashSet<ITexture>();

	private final ArrayList<ITexture> mTexturesLoaded = new ArrayList<ITexture>();

	private final ArrayList<ITexture> mTexturesToBeLoaded = new ArrayList<ITexture>();
	private final ArrayList<ITexture> mTexturesToBeUnloaded = new ArrayList<ITexture>();

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
	 * @param pTexture the {@link ITexture} to be loaded before the very next frame is drawn (Or prevent it from being unloaded then).
	 * @return <code>true</code> when the {@link ITexture} was previously not managed by this {@link TextureManager}, <code>false</code> if it was already managed.
	 */
	public boolean loadTexture(final ITexture pTexture) {
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
	 * @param pTexture the {@link ITexture} to be unloaded before the very next frame is drawn (Or prevent it from being loaded then).
	 * @return <code>true</code> when the {@link ITexture} was already managed by this {@link TextureManager}, <code>false</code> if it was not managed.
	 */
	public boolean unloadTexture(final ITexture pTexture) {
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

	public void loadTextures(final ITexture ... pTextures) {
		for(int i = pTextures.length - 1; i >= 0; i--) {
			this.loadTexture(pTextures[i]);
		}
	}

	public void unloadTextures(final ITexture ... pTextures) {
		for(int i = pTextures.length - 1; i >= 0; i--) {
			this.unloadTexture(pTextures[i]);
		}
	}

	public void reloadTextures() {
		final HashSet<ITexture> managedTextures = this.mTexturesManaged;
		for(final ITexture texture : managedTextures) { // TODO Can the use of the iterator be avoided somehow?
			texture.setLoadedToHardware(false);
		}

		this.mTexturesToBeLoaded.addAll(this.mTexturesLoaded); // TODO Check if addAll uses iterator internally!
		this.mTexturesLoaded.clear();

		this.mTexturesManaged.removeAll(this.mTexturesToBeUnloaded); // TODO Check if removeAll uses iterator internally!
		this.mTexturesToBeUnloaded.clear();
	}

	public void updateTextures(final GL10 pGL) {
		final HashSet<ITexture> texturesManaged = this.mTexturesManaged;
		final ArrayList<ITexture> texturesLoaded = this.mTexturesLoaded;
		final ArrayList<ITexture> texturesToBeLoaded = this.mTexturesToBeLoaded;
		final ArrayList<ITexture> texturesToBeUnloaded = this.mTexturesToBeUnloaded;

		/* First reload Textures that need to be updated. */
		final int textursLoadedCount = texturesLoaded.size();

		if(textursLoadedCount > 0){
			for(int i = textursLoadedCount - 1; i >= 0; i--){
				final ITexture textureToBeReloaded = texturesLoaded.get(i);
				if(textureToBeReloaded.isUpdateOnHardwareNeeded()){
					try {
						textureToBeReloaded.reloadToHardware(pGL);
					} catch(IOException e) {
						Debug.e(e);
					}
				}
			}
		}

		/* Then load pending Textures. */
		final int texturesToBeLoadedCount = texturesToBeLoaded.size();

		if(texturesToBeLoadedCount > 0){
			for(int i = texturesToBeLoadedCount - 1; i >= 0; i--){
				final ITexture textureToBeLoaded = texturesToBeLoaded.remove(i);
				if(!textureToBeLoaded.isLoadedToHardware()){
					try {
						textureToBeLoaded.loadToHardware(pGL);
					} catch(IOException e) {
						Debug.e(e);
					}
				}
				texturesLoaded.add(textureToBeLoaded);
			}
		}

		/* Then unload pending Textures. */
		final int texturesToBeUnloadedCount = texturesToBeUnloaded.size();

		if(texturesToBeUnloadedCount > 0){
			for(int i = texturesToBeUnloadedCount - 1; i >= 0; i--){
				final ITexture textureToBeUnloaded = texturesToBeUnloaded.remove(i);
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
