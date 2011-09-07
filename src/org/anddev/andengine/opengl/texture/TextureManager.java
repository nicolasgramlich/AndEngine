package org.anddev.andengine.opengl.texture;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture;
import org.anddev.andengine.util.Debug;

import android.content.res.AssetManager;

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
	private static final HashMap<String, ITexture> sTexturesMapped = new HashMap<String, ITexture>();

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

	public static synchronized void onCreate() {

	}

	public static synchronized void onDestroy() {
		final HashSet<ITexture> managedTextures = TextureManager.sTexturesManaged;
		for(final ITexture texture : managedTextures) { // TODO Can the use of the iterator be avoided somehow?
			texture.setLoadedToHardware(false);
		}

		TextureManager.sTexturesToBeLoaded.clear();
		TextureManager.sTexturesLoaded.clear();
		TextureManager.sTexturesManaged.clear();
		TextureManager.sTexturesMapped.clear();
	}

	public static synchronized boolean hasMappedTexture(final String pID) {
		if(pID == null) {
			throw new IllegalArgumentException("pID must not be null!");
		}
		return TextureManager.sTexturesMapped.containsKey(pID);
	}

	public static synchronized ITexture getMappedTexture(final String pID) {
		if(pID == null) {
			throw new IllegalArgumentException("pID must not be null!");
		}
		return TextureManager.sTexturesMapped.get(pID);
	}

	public static synchronized void addMappedTexture(final String pID, final ITexture pTexture) throws IllegalArgumentException {
		if(pID == null) {
			throw new IllegalArgumentException("pID must not be null!");
		}
		if(pTexture == null) {
			throw new IllegalArgumentException("pTexture must not be null!");
		}
		if(TextureManager.sTexturesMapped.containsKey(pID)) {
			throw new IllegalArgumentException("Collision for pID: '" + pID + "'.");
		}
		TextureManager.sTexturesMapped.put(pID, pTexture);
	}

	public static synchronized ITexture removedMappedTexture(final String pID) {
		if(pID == null) {
			throw new IllegalArgumentException("pID must not be null!");
		}
		return TextureManager.sTexturesMapped.remove(pID);
	}

	/**
	 * @param pTexture the {@link ITexture} to be loaded before the very next frame is drawn (Or prevent it from being unloaded then).
	 * @return <code>true</code> when the {@link ITexture} was previously not managed by this {@link TextureManager}, <code>false</code> if it was already managed.
	 */
	public static synchronized boolean loadTexture(final ITexture pTexture) {
		if(pTexture == null) {
			throw new IllegalArgumentException("pTexture must not be null!");
		}
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
		if(pTexture == null) {
			throw new IllegalArgumentException("pTexture must not be null!");
		}
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

	public static synchronized ITexture getTexture(final String pID, final AssetManager pAssetManager, final String pAssetPath) throws IOException {
		return TextureManager.getTexture(pID, pAssetManager, pAssetPath, TextureOptions.DEFAULT);
	}

	public static synchronized ITexture getTexture(final String pID, final AssetManager pAssetManager, final String pAssetPath, final TextureOptions pTextureOptions) throws IOException {
		if(TextureManager.hasMappedTexture(pID)) {
			return TextureManager.getMappedTexture(pID);
		} else {
			final ITexture texture = new BitmapTexture(pTextureOptions) {
				@Override
				protected InputStream onGetInputStream() throws IOException {
					return pAssetManager.open(pAssetPath);
				}
			};
			TextureManager.loadTexture(texture);
			TextureManager.addMappedTexture(pID, texture);
			
			return texture;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
