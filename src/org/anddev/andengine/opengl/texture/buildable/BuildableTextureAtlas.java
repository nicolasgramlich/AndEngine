package org.anddev.andengine.opengl.texture.buildable;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.ITextureAtlas;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.bitmap.BuildableBitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.buildable.builder.ITextureBuilder;
import org.anddev.andengine.opengl.texture.buildable.builder.ITextureBuilder.TextureSourcePackingException;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.util.Callback;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 21:26:38 - 12.08.2010
 */
public class BuildableTextureAtlas<T extends ITextureSource, A extends ITextureAtlas<T>> implements ITextureAtlas<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final A mTextureAtlas;
	private final ArrayList<TextureSourceWithWithLocationCallback<T>> mTextureSourcesToPlace = new ArrayList<TextureSourceWithWithLocationCallback<T>>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public BuildableTextureAtlas(final A pTextureAtlas) {
		this.mTextureAtlas = pTextureAtlas;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getWidth() {
		return this.mTextureAtlas.getWidth();
	}

	@Override
	public int getHeight() {
		return this.mTextureAtlas.getHeight();
	}

	@Override
	public int getHardwareTextureID() {
		return this.mTextureAtlas.getHardwareTextureID();
	}

	@Override
	public boolean isLoadedToHardware() {
		return this.mTextureAtlas.isLoadedToHardware();
	}

	@Override
	public void setLoadedToHardware(final boolean pLoadedToHardware) {
		this.mTextureAtlas.setLoadedToHardware(pLoadedToHardware);
	}

	@Override
	public boolean isUpdateOnHardwareNeeded() {
		return this.mTextureAtlas.isUpdateOnHardwareNeeded();
	}

	@Override
	public void setUpdateOnHardwareNeeded(final boolean pUpdateOnHardwareNeeded) {
		this.mTextureAtlas.setUpdateOnHardwareNeeded(pUpdateOnHardwareNeeded);
	}

	@Override
	public void loadToHardware(final GL10 pGL) throws IOException {
		this.mTextureAtlas.loadToHardware(pGL);
	}

	@Override
	public void unloadFromHardware(final GL10 pGL) {
		this.mTextureAtlas.unloadFromHardware(pGL);
	}

	@Override
	public void reloadToHardware(final GL10 pGL) throws IOException {
		this.mTextureAtlas.reloadToHardware(pGL);
	}

	@Override
	public void bind(final GL10 pGL) {
		this.mTextureAtlas.bind(pGL);
	}

	@Override
	public TextureOptions getTextureOptions() {
		return this.mTextureAtlas.getTextureOptions();
	}

	/**
	 * Most likely this is not the method you'd want to be using, as the {@link ITextureSource} won't get packed through this.
	 * @deprecated Use {@link BuildableTextureAtlas#addTextureSource(ITextureSource)} instead.
	 */
	@Deprecated
	@Override
	public void addTextureSource(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
		this.mTextureAtlas.addTextureSource(pTextureSource, pTexturePositionX, pTexturePositionY);
	}

	@Override
	public void removeTextureSource(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
		this.mTextureAtlas.removeTextureSource(pTextureSource, pTexturePositionX, pTexturePositionY);
	}

	@Override
	public void clearTextureSources() {
		this.mTextureAtlas.clearTextureSources();
		this.mTextureSourcesToPlace.clear();
	}

	@Override
	public boolean hasTextureStateListener() {
		return this.mTextureAtlas.hasTextureStateListener();
	}

	@Override
	public ITextureAtlasStateListener<T> getTextureStateListener() {
		return this.mTextureAtlas.getTextureStateListener();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * When all {@link ITextureSource}s are added you have to call {@link BuildableBitmapTextureAtlas#build(ITextureBuilder)}.
	 * @param pTextureSource to be added.
	 * @param pTextureRegion
	 */
	public void addTextureSource(final T pTextureSource, final Callback<T> pCallback) {
		this.mTextureSourcesToPlace.add(new TextureSourceWithWithLocationCallback<T>(pTextureSource, pCallback));
	}

	/**
	 * Removes a {@link ITextureSource} before {@link BuildableBitmapTextureAtlas#build(ITextureBuilder)} is called.
	 * @param pBitmapTextureSource to be removed.
	 */
	public void removeTextureSource(final ITextureSource pTextureSource) {
		final ArrayList<TextureSourceWithWithLocationCallback<T>> textureSources = this.mTextureSourcesToPlace;
		for(int i = textureSources.size() - 1; i >= 0; i--) {
			final TextureSourceWithWithLocationCallback<T> textureSource = textureSources.get(i);
			if(textureSource.mTextureSource == pTextureSource) {
				textureSources.remove(i);
				this.mTextureAtlas.setUpdateOnHardwareNeeded(true);
				return;
			}
		}
	}

	/**
	 * May draw over already added {@link ITextureSource}s.
	 *
	 * @param pTextureSourcePackingAlgorithm the {@link ITextureBuilder} to use for packing the {@link ITextureSource} in this {@link BuildableBitmapTextureAtlas}.
	 * @throws TextureSourcePackingException i.e. when the {@link ITextureSource}s didn't fit into this {@link BuildableBitmapTextureAtlas}.
	 */
	public void build(final ITextureBuilder<T, A> pTextureSourcePackingAlgorithm) throws TextureSourcePackingException {
		pTextureSourcePackingAlgorithm.pack(this.mTextureAtlas, this.mTextureSourcesToPlace);
		this.mTextureSourcesToPlace.clear();
		this.mTextureAtlas.setUpdateOnHardwareNeeded(true);
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
