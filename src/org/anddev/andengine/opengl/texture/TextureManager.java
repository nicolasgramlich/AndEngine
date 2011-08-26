package org.anddev.andengine.opengl.texture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

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

	private static final HashSet<ITexture> sTexturesManaged = new HashSet<ITexture>();

	private static final ArrayList<ITexture> sTexturesLoaded = new ArrayList<ITexture>();

	private static final ArrayList<ITexture> sTexturesToBeLoaded = new ArrayList<ITexture>();
	private static final ArrayList<ITexture> sTexturesToBeUnloaded = new ArrayList<ITexture>();

	// ===========================================================
	// Constructors
	// ===========================================================

	private TextureManager() {

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

	public static void onCreate() {

	}

	public static synchronized void onDestroy() {
		final HashSet<ITexture> managedTextures = TextureManager.sTexturesManaged;
		for(final ITexture texture : managedTextures) { // TODO Can the use of the iterator be avoided somehow?
			texture.setLoadedToHardware(false);
		}

		TextureManager.sTexturesToBeLoaded.clear();
		TextureManager.sTexturesLoaded.clear();
		TextureManager.sTexturesManaged.clear();
	}

	/**
	 * @param pTexture the {@link ITexture} to be loaded before the very next frame is drawn (Or prevent it from being unloaded then).
	 * @return <code>true</code> when the {@link ITexture} was previously not managed by this {@link TextureManager}, <code>false</code> if it was already managed.
	 */
	public static synchronized boolean loadTexture(final ITexture pTexture) {
		if(TextureManager.sTexturesManaged.contains(pTexture)) {
			/* Just make sure it doesn't get deleted. */
			TextureManager.sTexturesToBeUnloaded.remove(pTexture);
			return false;
		} else {
			TextureManager.sTexturesManaged.add(pTexture);
			TextureManager.sTexturesToBeLoaded.add(pTexture);
			return true;
		}
	}

	/**
	 * @param pTexture the {@link ITexture} to be unloaded before the very next frame is drawn (Or prevent it from being loaded then).
	 * @return <code>true</code> when the {@link ITexture} was already managed by this {@link TextureManager}, <code>false</code> if it was not managed.
	 */
	public static synchronized boolean unloadTexture(final ITexture pTexture) {
		if(TextureManager.sTexturesManaged.contains(pTexture)) {
			/* If the Texture is loaded, unload it.
			 * If the Texture is about to be loaded, stop it from being loaded. */
			if(TextureManager.sTexturesLoaded.contains(pTexture)){
				TextureManager.sTexturesToBeUnloaded.add(pTexture);
			} else if(TextureManager.sTexturesToBeLoaded.remove(pTexture)){
				TextureManager.sTexturesManaged.remove(pTexture);
			}
			return true;
		} else {
			return false;
		}
	}

	public static void loadTextures(final ITexture ... pTextures) {
		for(int i = pTextures.length - 1; i >= 0; i--) {
			TextureManager.loadTexture(pTextures[i]);
		}
	}

	public static void unloadTextures(final ITexture ... pTextures) {
		for(int i = pTextures.length - 1; i >= 0; i--) {
			TextureManager.unloadTexture(pTextures[i]);
		}
	}

	public static synchronized void onReload() {
		final HashSet<ITexture> managedTextures = TextureManager.sTexturesManaged;
		for(final ITexture texture : managedTextures) { // TODO Can the use of the iterator be avoided somehow?
			texture.setLoadedToHardware(false);
		}

		TextureManager.sTexturesToBeLoaded.addAll(TextureManager.sTexturesLoaded); // TODO Check if addAll uses iterator internally!
		TextureManager.sTexturesLoaded.clear();

		TextureManager.sTexturesManaged.removeAll(TextureManager.sTexturesToBeUnloaded); // TODO Check if removeAll uses iterator internally!
		TextureManager.sTexturesToBeUnloaded.clear();
	}

	public static synchronized void updateTextures() {
		final HashSet<ITexture> texturesManaged = TextureManager.sTexturesManaged;
		final ArrayList<ITexture> texturesLoaded = TextureManager.sTexturesLoaded;
		final ArrayList<ITexture> texturesToBeLoaded = TextureManager.sTexturesToBeLoaded;
		final ArrayList<ITexture> texturesToBeUnloaded = TextureManager.sTexturesToBeUnloaded;

		/* First reload Textures that need to be updated. */
		final int textursLoadedCount = texturesLoaded.size();

		if(textursLoadedCount > 0){
			for(int i = textursLoadedCount - 1; i >= 0; i--){
				final ITexture textureToBeReloaded = texturesLoaded.get(i);
				if(textureToBeReloaded.isUpdateOnHardwareNeeded()){
					try {
						textureToBeReloaded.reloadToHardware();
					} catch(final IOException e) {
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
						textureToBeLoaded.loadToHardware();
					} catch(final IOException e) {
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
					textureToBeUnloaded.unloadFromHardware();
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
