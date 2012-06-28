package org.andengine.opengl.texture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.io.in.AssetInputStreamOpener;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

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

	private final HashSet<ITexture> mTexturesManaged = new HashSet<ITexture>();
	private final HashMap<String, ITexture> mTexturesMapped = new HashMap<String, ITexture>();

	private final ArrayList<ITexture> mTexturesLoaded = new ArrayList<ITexture>();

	private final ArrayList<ITexture> mTexturesToBeLoaded = new ArrayList<ITexture>();
	private final ArrayList<ITexture> mTexturesToBeUnloaded = new ArrayList<ITexture>();

	private TextureWarmUpVertexBufferObject mTextureWarmUpVertexBufferObject;

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

	public synchronized void onCreate() {
		this.mTextureWarmUpVertexBufferObject = new TextureWarmUpVertexBufferObject();
	}

	public synchronized void onReload() {
		final HashSet<ITexture> managedTextures = this.mTexturesManaged;
		if(!managedTextures.isEmpty()) {
			for(final ITexture texture : managedTextures) { // TODO Can the use of the iterator be avoided somehow?
				texture.setNotLoadedToHardware();
			}
		}

		if(!this.mTexturesLoaded.isEmpty()) {
			this.mTexturesToBeLoaded.addAll(this.mTexturesLoaded); // TODO Check if addAll uses iterator internally!
			this.mTexturesLoaded.clear();
		}

		if(!this.mTexturesToBeUnloaded.isEmpty()) {
			this.mTexturesManaged.removeAll(this.mTexturesToBeUnloaded); // TODO Check if removeAll uses iterator internally!
			this.mTexturesToBeUnloaded.clear();
		}

		this.mTextureWarmUpVertexBufferObject.setNotLoadedToHardware();
	}

	public synchronized void onDestroy() {
		final HashSet<ITexture> managedTextures = this.mTexturesManaged;
		for(final ITexture texture : managedTextures) { // TODO Can the use of the iterator be avoided somehow?
			texture.setNotLoadedToHardware();
		}

		this.mTexturesToBeLoaded.clear();
		this.mTexturesLoaded.clear();
		this.mTexturesManaged.clear();
		this.mTexturesMapped.clear();

		this.mTextureWarmUpVertexBufferObject.dispose();
		this.mTextureWarmUpVertexBufferObject = null;
	}

	public synchronized boolean hasMappedTexture(final String pID) {
		if(pID == null) {
			throw new IllegalArgumentException("pID must not be null!");
		}
		return this.mTexturesMapped.containsKey(pID);
	}

	public synchronized ITexture getMappedTexture(final String pID) {
		if(pID == null) {
			throw new IllegalArgumentException("pID must not be null!");
		}
		return this.mTexturesMapped.get(pID);
	}

	public synchronized void addMappedTexture(final String pID, final ITexture pTexture) throws IllegalArgumentException {
		if(pID == null) {
			throw new IllegalArgumentException("pID must not be null!");
		} else if(pTexture == null) {
			throw new IllegalArgumentException("pTexture must not be null!");
		} else if(this.mTexturesMapped.containsKey(pID)) {
			throw new IllegalArgumentException("Collision for pID: '" + pID + "'.");
		}
		this.mTexturesMapped.put(pID, pTexture);
	}

	public synchronized ITexture removedMappedTexture(final String pID) {
		if(pID == null) {
			throw new IllegalArgumentException("pID must not be null!");
		}
		return this.mTexturesMapped.remove(pID);
	}

	/**
	 * @param pTexture the {@link ITexture} to be loaded before the very next frame is drawn (Or prevent it from being unloaded then).
	 * @return <code>true</code> when the {@link ITexture} was previously not managed by this {@link TextureManager}, <code>false</code> if it was already managed.
	 */
	public synchronized boolean loadTexture(final ITexture pTexture) {
		if(pTexture == null) {
			throw new IllegalArgumentException("pTexture must not be null!");
		}

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
	 * Must be called from the GL-{@link Thread}.
	 *
	 * @param pGLState
	 * @param pTexture the {@link ITexture} to be loaded right now, if it is not loaded.
	 * @return <code>true</code> when the {@link ITexture} was previously not managed by this {@link TextureManager}, <code>false</code> if it was already managed.
	 */
	public synchronized boolean loadTexture(final GLState pGLState, final ITexture pTexture) throws IOException {
		if(pTexture == null) {
			throw new IllegalArgumentException("pTexture must not be null!");
		}

		if(!pTexture.isLoadedToHardware()) {
			pTexture.loadToHardware(pGLState);
		} else if(pTexture.isUpdateOnHardwareNeeded()) {
			pTexture.reloadToHardware(pGLState);
		}

		if(this.mTexturesManaged.contains(pTexture)) {
			/* Just make sure it doesn't get deleted. */
			this.mTexturesToBeUnloaded.remove(pTexture);
			return false;
		} else {
			this.mTexturesManaged.add(pTexture);
			this.mTexturesLoaded.add(pTexture);
			return true;
		}
	}

	/**
	 * @param pTexture the {@link ITexture} to be unloaded before the very next frame is drawn (Or prevent it from being loaded then).
	 * @return <code>true</code> when the {@link ITexture} was already managed by this {@link TextureManager}, <code>false</code> if it was not managed.
	 */
	public synchronized boolean unloadTexture(final ITexture pTexture) {
		if(pTexture == null) {
			throw new IllegalArgumentException("pTexture must not be null!");
		}

		if(this.mTexturesManaged.contains(pTexture)) {
			/* If the Texture is loaded, unload it.
			 * If the Texture is about to be loaded, stop it from being loaded. */
			if(this.mTexturesLoaded.contains(pTexture)) {
				this.mTexturesToBeUnloaded.add(pTexture);
			} else if(this.mTexturesToBeLoaded.remove(pTexture)) {
				this.mTexturesManaged.remove(pTexture);
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Must be called from the GL-{@link Thread}.
	 *
	 * @param pGLState
	 * @param pTexture the {@link ITexture} to be unloaded right now, if it is loaded.
	 * @return <code>true</code> when the {@link ITexture} was already managed by this {@link TextureManager}, <code>false</code> if it was not managed.
	 */
	public synchronized boolean unloadTexture(final GLState pGLState, final ITexture pTexture) {
		if(pTexture == null) {
			throw new IllegalArgumentException("pTexture must not be null!");
		} else if(pTexture.isLoadedToHardware()) {
			pTexture.unloadFromHardware(pGLState);
		}

		if(this.mTexturesManaged.contains(pTexture)) {
			/* Just make sure it doesn't get loaded. */
			this.mTexturesLoaded.remove(pTexture);
			this.mTexturesToBeLoaded.remove(pTexture);

			return true;
		} else {
			return false;
		}
	}

	public synchronized void updateTextures(final GLState pGLState) {
		final HashSet<ITexture> texturesManaged = this.mTexturesManaged;
		final ArrayList<ITexture> texturesLoaded = this.mTexturesLoaded;
		final ArrayList<ITexture> texturesToBeLoaded = this.mTexturesToBeLoaded;
		final ArrayList<ITexture> texturesToBeUnloaded = this.mTexturesToBeUnloaded;

		/* First reload Textures that need to be updated. */
		for(int i = texturesLoaded.size() - 1; i >= 0; i--) {
			final ITexture textureToBeReloaded = texturesLoaded.get(i);
			if(textureToBeReloaded.isUpdateOnHardwareNeeded()) {
				try {
					textureToBeReloaded.reloadToHardware(pGLState);
				} catch (final IOException e) {
					Debug.e(e);
				}
			}
		}

		/* Then load pending Textures. */
		final int texturesToBeLoadedCount = texturesToBeLoaded.size();

		if(texturesToBeLoadedCount > 0) {
			for(int i = texturesToBeLoadedCount - 1; i >= 0; i--) {
				final ITexture textureToBeLoaded = texturesToBeLoaded.remove(i);
				if(!textureToBeLoaded.isLoadedToHardware()) {
					try {
						textureToBeLoaded.loadToHardware(pGLState);

						/* Execute the warm-up to ensure the texture data is actually moved to the GPU. */
						this.mTextureWarmUpVertexBufferObject.warmup(pGLState, textureToBeLoaded);
					} catch (final IOException e) {
						Debug.e(e);
					}
				}
				texturesLoaded.add(textureToBeLoaded);
			}
		}

		/* Then unload pending Textures. */
		final int texturesToBeUnloadedCount = texturesToBeUnloaded.size();

		if(texturesToBeUnloadedCount > 0) {
			for(int i = texturesToBeUnloadedCount - 1; i >= 0; i--) {
				final ITexture textureToBeUnloaded = texturesToBeUnloaded.remove(i);
				if(textureToBeUnloaded.isLoadedToHardware()) {
					textureToBeUnloaded.unloadFromHardware(pGLState);
				}
				texturesLoaded.remove(textureToBeUnloaded);
				texturesManaged.remove(textureToBeUnloaded);
			}
		}

		/* Finally invoke the GC if anything has changed. */
		if((texturesToBeLoadedCount > 0) || (texturesToBeUnloadedCount > 0)) {
			System.gc();
		}
	}

	public synchronized ITexture getTexture(final String pID, final AssetManager pAssetManager, final String pAssetPath) throws IOException {
		return this.getTexture(pID, pAssetManager, pAssetPath, TextureOptions.DEFAULT);
	}

	public synchronized ITexture getTexture(final String pID, final AssetManager pAssetManager, final String pAssetPath, final TextureOptions pTextureOptions) throws IOException {
		if(this.hasMappedTexture(pID)) {
			return this.getMappedTexture(pID);
		} else {
			final ITexture texture = new BitmapTexture(this, new AssetInputStreamOpener(pAssetManager, pAssetPath), pTextureOptions);
			this.loadTexture(texture);
			this.addMappedTexture(pID, texture);

			return texture;
		}
	}

	public synchronized ITexture getTexture(final String pID, final IInputStreamOpener pInputStreamOpener) throws IOException {
		return this.getTexture(pID, pInputStreamOpener, TextureOptions.DEFAULT);
	}

	public synchronized ITexture getTexture(final String pID, final IInputStreamOpener pInputStreamOpener, final TextureOptions pTextureOptions) throws IOException {
		return this.getTexture(pID, pInputStreamOpener, BitmapTextureFormat.RGBA_8888, pTextureOptions);
	}

	public synchronized ITexture getTexture(final String pID, final IInputStreamOpener pInputStreamOpener, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions) throws IOException {
		return this.getTexture(pID, pInputStreamOpener, pBitmapTextureFormat, pTextureOptions, true);
	}

	public synchronized ITexture getTexture(final String pID, final IInputStreamOpener pInputStreamOpener, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions, final boolean pLoadToHardware) throws IOException {
		if(this.hasMappedTexture(pID)) {
			return this.getMappedTexture(pID);
		} else {
			final ITexture texture = new BitmapTexture(this, pInputStreamOpener, pBitmapTextureFormat, pTextureOptions);
			if(pLoadToHardware) {
				this.loadTexture(texture);
			}
			this.addMappedTexture(pID, texture);

			return texture;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
