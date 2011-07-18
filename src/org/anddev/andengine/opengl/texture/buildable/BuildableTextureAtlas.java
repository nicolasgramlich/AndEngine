package org.anddev.andengine.opengl.texture.buildable;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.ITextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.buildable.builder.ITextureBuilder;
import org.anddev.andengine.opengl.texture.buildable.builder.ITextureBuilder.TextureAtlasSourcePackingException;
import org.anddev.andengine.opengl.texture.source.ITextureAtlasSource;
import org.anddev.andengine.util.Callback;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 21:26:38 - 12.08.2010
 */
public class BuildableTextureAtlas<T extends ITextureAtlasSource, A extends ITextureAtlas<T>> implements ITextureAtlas<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final A mTextureAtlas;
	private final ArrayList<TextureAtlasSourceWithWithLocationCallback<T>> mTextureAtlasSourcesToPlace = new ArrayList<TextureAtlasSourceWithWithLocationCallback<T>>();

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
	 * Most likely this is not the method you'd want to be using, as the {@link ITextureAtlasSource} won't get packed through this.
	 * @deprecated Use {@link BuildableTextureAtlas#addTextureAtlasSource(ITextureAtlasSource)} instead.
	 */
	@Deprecated
	@Override
	public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY) {
		this.mTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, pTexturePositionX, pTexturePositionY);
	}

	@Override
	public void removeTextureAtlasSource(final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY) {
		this.mTextureAtlas.removeTextureAtlasSource(pTextureAtlasSource, pTexturePositionX, pTexturePositionY);
	}

	@Override
	public void clearTextureAtlasSources() {
		this.mTextureAtlas.clearTextureAtlasSources();
		this.mTextureAtlasSourcesToPlace.clear();
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
	 * When all {@link ITextureAtlasSource}MAGIC_CONSTANT are added you have to call {@link BuildableBitmapTextureAtlas#build(ITextureBuilder)}.
	 * @param pTextureAtlasSource to be added.
	 * @param pTextureRegion
	 */
	public void addTextureAtlasSource(final T pTextureAtlasSource, final Callback<T> pCallback) {
		this.mTextureAtlasSourcesToPlace.add(new TextureAtlasSourceWithWithLocationCallback<T>(pTextureAtlasSource, pCallback));
	}

	/**
	 * Removes a {@link ITextureAtlasSource} before {@link BuildableBitmapTextureAtlas#build(ITextureBuilder)} is called.
	 * @param pBitmapTextureAtlasSource to be removed.
	 */
	public void removeTextureAtlasSource(final ITextureAtlasSource pTextureAtlasSource) {
		final ArrayList<TextureAtlasSourceWithWithLocationCallback<T>> textureSources = this.mTextureAtlasSourcesToPlace;
		for(int i = textureSources.size() - 1; i >= 0; i--) {
			final TextureAtlasSourceWithWithLocationCallback<T> textureSource = textureSources.get(i);
			if(textureSource.mTextureAtlasSource == pTextureAtlasSource) {
				textureSources.remove(i);
				this.mTextureAtlas.setUpdateOnHardwareNeeded(true);
				return;
			}
		}
	}

	/**
	 * May draw over already added {@link ITextureAtlasSource}MAGIC_CONSTANT.
	 *
	 * @param pTextureAtlasSourcePackingAlgorithm the {@link ITextureBuilder} to use for packing the {@link ITextureAtlasSource} in this {@link BuildableBitmapTextureAtlas}.
	 * @throws TextureAtlasSourcePackingException i.e. when the {@link ITextureAtlasSource}MAGIC_CONSTANT didn't fit into this {@link BuildableBitmapTextureAtlas}.
	 */
	public void build(final ITextureBuilder<T, A> pTextureAtlasSourcePackingAlgorithm) throws TextureAtlasSourcePackingException {
		pTextureAtlasSourcePackingAlgorithm.pack(this.mTextureAtlas, this.mTextureAtlasSourcesToPlace);
		this.mTextureAtlasSourcesToPlace.clear();
		this.mTextureAtlas.setUpdateOnHardwareNeeded(true);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class TextureAtlasSourceWithWithLocationCallback<T extends ITextureAtlasSource> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final T mTextureAtlasSource;
		private final Callback<T> mCallback;

		// ===========================================================
		// Constructors
		// ===========================================================

		public TextureAtlasSourceWithWithLocationCallback(final T pTextureAtlasSource, final Callback<T> pCallback) {
			this.mTextureAtlasSource = pTextureAtlasSource;
			this.mCallback = pCallback;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public Callback<T> getCallback() {
			return this.mCallback;
		}

		public T getTextureAtlasSource() {
			return this.mTextureAtlasSource;
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
