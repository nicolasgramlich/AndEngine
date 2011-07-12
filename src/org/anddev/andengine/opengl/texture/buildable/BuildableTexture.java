package org.anddev.andengine.opengl.texture.buildable;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.buildable.builder.ITextureBuilder;
import org.anddev.andengine.opengl.texture.buildable.builder.ITextureBuilder.TextureSourcePackingException;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.util.Callback;

/**
 * @author Nicolas Gramlich
 * @since 21:26:38 - 12.08.2010
 */
public class BuildableTexture<T extends ITextureSource, K extends ITexture<T>> implements ITexture<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final K mTexture;
	private final ArrayList<TextureSourceWithWithLocationCallback<T>> mTextureSourcesToPlace = new ArrayList<TextureSourceWithWithLocationCallback<T>>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public BuildableTexture(final K pTexture) {
		this.mTexture = pTexture;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getWidth() {
		return this.mTexture.getWidth();
	}

	@Override
	public int getHeight() {
		return this.mTexture.getHeight();
	}

	@Override
	public int getHardwareTextureID() {
		return this.mTexture.getHardwareTextureID();
	}

	@Override
	public boolean isLoadedToHardware() {
		return this.mTexture.isLoadedToHardware();
	}

	@Override
	public void setLoadedToHardware(final boolean pLoadedToHardware) {
		this.mTexture.setLoadedToHardware(pLoadedToHardware);
	}

	@Override
	public boolean isUpdateOnHardwareNeeded() {
		return this.mTexture.isUpdateOnHardwareNeeded();
	}

	@Override
	public void setUpdateOnHardwareNeeded(final boolean pUpdateOnHardwareNeeded) {
		this.mTexture.setUpdateOnHardwareNeeded(pUpdateOnHardwareNeeded);
	}

	@Override
	public void loadToHardware(final GL10 pGL) throws IOException {
		this.mTexture.loadToHardware(pGL);
	}

	@Override
	public void unloadFromHardware(final GL10 pGL) {
		this.mTexture.unloadFromHardware(pGL);
	}

	@Override
	public void reloadToHardware(final GL10 pGL) throws IOException {
		this.mTexture.reloadToHardware(pGL);
	}

	@Override
	public void bind(final GL10 pGL) {
		this.mTexture.bind(pGL);
	}

	@Override
	public TextureOptions getTextureOptions() {
		return this.mTexture.getTextureOptions();
	}

	/**
	 * Most likely this is not the method you'd want to be using, as the {@link ITextureSource} won't get packed through this.
	 * @deprecated Use {@link BuildableTexture#addTextureSource(ITextureSource)} instead.
	 */
	@Deprecated
	@Override
	public void addTextureSource(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
		this.mTexture.addTextureSource(pTextureSource, pTexturePositionX, pTexturePositionY);
	}

	@Override
	public void removeTextureSource(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
		this.mTexture.removeTextureSource(pTextureSource, pTexturePositionX, pTexturePositionY);
	}

	@Override
	public void clearTextureSources() {
		this.mTexture.clearTextureSources();
		this.mTextureSourcesToPlace.clear();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * When all {@link ITextureSource}s are added you have to call {@link BuildableBitmapTexture#build(ITextureBuilder)}.
	 * @param pTextureSource to be added.
	 * @param pTextureRegion
	 */
	public void addTextureSource(final T pTextureSource, final Callback<T> pCallback) {
		this.mTextureSourcesToPlace.add(new TextureSourceWithWithLocationCallback<T>(pTextureSource, pCallback));
	}

	/**
	 * Removes a {@link ITextureSource} before {@link BuildableBitmapTexture#build(ITextureBuilder)} is called.
	 * @param pBitmapTextureSource to be removed.
	 */
	public void removeTextureSource(final ITextureSource pTextureSource) {
		final ArrayList<TextureSourceWithWithLocationCallback<T>> textureSources = this.mTextureSourcesToPlace;
		for(int i = textureSources.size() - 1; i >= 0; i--) {
			final TextureSourceWithWithLocationCallback<T> textureSource = textureSources.get(i);
			if(textureSource.mTextureSource == pTextureSource) {
				textureSources.remove(i);
				this.mTexture.setUpdateOnHardwareNeeded(true);
				return;
			}
		}
	}

	/**
	 * May draw over already added {@link ITextureSource}s.
	 *
	 * @param pTextureSourcePackingAlgorithm the {@link ITextureBuilder} to use for packing the {@link ITextureSource} in this {@link BuildableBitmapTexture}.
	 * @throws TextureSourcePackingException i.e. when the {@link ITextureSource}s didn't fit into this {@link BuildableBitmapTexture}.
	 */
	public void build(final ITextureBuilder<T, K> pTextureSourcePackingAlgorithm) throws TextureSourcePackingException {
		pTextureSourcePackingAlgorithm.pack(this.mTexture, this.mTextureSourcesToPlace);
		this.mTextureSourcesToPlace.clear();
		this.mTexture.setUpdateOnHardwareNeeded(true);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class TextureSourceWithWithLocationCallback<T extends ITextureSource> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final T mTextureSource;
		private final Callback<T> mCallback;

		// ===========================================================
		// Constructors
		// ===========================================================

		public TextureSourceWithWithLocationCallback(final T pTextureSource, final Callback<T> pCallback) {
			this.mTextureSource = pTextureSource;
			this.mCallback = pCallback;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public Callback<T> getCallback() {
			return this.mCallback;
		}

		public T getTextureSource() {
			return this.mTextureSource;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
