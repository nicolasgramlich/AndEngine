package org.anddev.andengine.opengl.texture.atlas.buildable;

import java.io.IOException;
import java.util.ArrayList;

import org.anddev.andengine.opengl.texture.PixelFormat;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.ITextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.anddev.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.anddev.andengine.opengl.texture.atlas.source.ITextureAtlasSource;
import org.anddev.andengine.util.Callback;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 21:26:38 - 12.08.2010
 */
public class BuildableTextureAtlas<S extends ITextureAtlasSource, T extends ITextureAtlas<S>> implements ITextureAtlas<S> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final T mTextureAtlas;
	private final ArrayList<TextureAtlasSourceWithWithLocationCallback<S>> mTextureAtlasSourcesToPlace = new ArrayList<TextureAtlasSourceWithWithLocationCallback<S>>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public BuildableTextureAtlas(final T pTextureAtlas) {
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
	public BuildableTextureAtlas<S, T> load() {
		this.mTextureAtlas.load();

		return this;
	}

	@Override
	public BuildableTextureAtlas<S, T> unload() {
		this.mTextureAtlas.unload();

		return this;
	}

	@Override
	public void loadToHardware() throws IOException {
		this.mTextureAtlas.loadToHardware();
	}

	@Override
	public void unloadFromHardware() {
		this.mTextureAtlas.unloadFromHardware();
	}

	@Override
	public void reloadToHardware() throws IOException {
		this.mTextureAtlas.reloadToHardware();
	}

	@Override
	public void bind() {
		this.mTextureAtlas.bind();
	}

	@Override
	public void bind(final int pGLActiveTexture) {
		this.mTextureAtlas.bind(pGLActiveTexture);
	}

	@Override
	public PixelFormat getPixelFormat() {
		return this.mTextureAtlas.getPixelFormat();
	}

	@Override
	public TextureOptions getTextureOptions() {
		return this.mTextureAtlas.getTextureOptions();
	}

	/**
	 * Most likely this is not the method you'd want to be using, as the {@link ITextureAtlasSource} won't get built through this {@link BuildableTextureAtlas}.
	 * @deprecated Use {@link BuildableTextureAtlas#addTextureAtlasSource(ITextureAtlasSource)} instead.
	 */
	@Deprecated
	@Override
	public void addTextureAtlasSource(final S pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY) {
		this.mTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, pTexturePositionX, pTexturePositionY);
	}

	/**
	 * Most likely this is not the method you'd want to be using, as the {@link ITextureAtlasSource} won't get built through this {@link BuildableTextureAtlas}.
	 * @deprecated Use {@link BuildableTextureAtlas#addTextureAtlasSource(ITextureAtlasSource)} instead.
	 */
	@Deprecated
	public void addTextureAtlasSource(final S pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY, final int pTextureAtlasSourcePadding) {
		this.addTextureAtlasSource(pTextureAtlasSource, pTexturePositionX, pTexturePositionY, pTextureAtlasSourcePadding);
	}

	@Override
	public void removeTextureAtlasSource(final S pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY) {
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
	public ITextureAtlasStateListener<S> getTextureStateListener() {
		return this.mTextureAtlas.getTextureStateListener();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void addEmptyTextureAtlasSource(final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		this.mTextureAtlas.addEmptyTextureAtlasSource(pTexturePositionX, pTexturePositionY, pWidth, pHeight);
	}

	/**
	 * When all {@link ITextureAtlasSource}MAGIC_CONSTANT are added you have to call {@link BuildableBitmapTextureAtlas#build(ITextureAtlasBuilder)}.
	 * @param pTextureAtlasSource to be added.
	 * @param pCallback
	 */
	public void addTextureAtlasSource(final S pTextureAtlasSource, final Callback<S> pCallback) {
		this.mTextureAtlasSourcesToPlace.add(new TextureAtlasSourceWithWithLocationCallback<S>(pTextureAtlasSource, pCallback));
	}

	/**
	 * Removes a {@link ITextureAtlasSource} before {@link BuildableBitmapTextureAtlas#build(ITextureAtlasBuilder)} is called.
	 * @param pBitmapTextureAtlasSource to be removed.
	 */
	public void removeTextureAtlasSource(final ITextureAtlasSource pTextureAtlasSource) {
		final ArrayList<TextureAtlasSourceWithWithLocationCallback<S>> textureSources = this.mTextureAtlasSourcesToPlace;
		for(int i = textureSources.size() - 1; i >= 0; i--) {
			final TextureAtlasSourceWithWithLocationCallback<S> textureSource = textureSources.get(i);
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
	 * @param pTextureAtlasBuilder the {@link ITextureAtlasBuilder} to use for building the {@link ITextureAtlasSource} in this {@link BuildableBitmapTextureAtlas}.
	 * @return itself for method chaining.
	 * @throws TextureAtlasBuilderException i.e. when the {@link ITextureAtlasSource}MAGIC_CONSTANT didn't fit into this {@link BuildableBitmapTextureAtlas}.
	 */
	public BuildableTextureAtlas<S, T> build(final ITextureAtlasBuilder<S, T> pTextureAtlasBuilder) throws TextureAtlasBuilderException {
		pTextureAtlasBuilder.build(this.mTextureAtlas, this.mTextureAtlasSourcesToPlace);
		this.mTextureAtlasSourcesToPlace.clear();
		this.mTextureAtlas.setUpdateOnHardwareNeeded(true);

		return this;
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

		public T getTextureAtlasSource() {
			return this.mTextureAtlasSource;
		}

		public Callback<T> getCallback() {
			return this.mCallback;
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
