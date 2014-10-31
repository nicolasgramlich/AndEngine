package org.andengine.opengl.texture.atlas.buildable;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.opengl.texture.ITextureStateListener;
import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.ITextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.atlas.source.ITextureAtlasSource;
import org.andengine.opengl.util.GLState;
import org.andengine.util.call.Callback;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 21:26:38 - 12.08.2010
 */
public class BuildableTextureAtlas<S extends ITextureAtlasSource, T extends ITextureAtlas<S>> implements IBuildableTextureAtlas<S, T> {
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
	public void setNotLoadedToHardware() {
		this.mTextureAtlas.setNotLoadedToHardware();
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
	public void load() {
		this.mTextureAtlas.load();
	}

	@Override
	public void load(final GLState pGLState) throws IOException {
		this.mTextureAtlas.load(pGLState);
	}

	@Override
	public void unload() {
		this.mTextureAtlas.unload();
	}

	@Override
	public void unload(final GLState pGLState) {
		this.mTextureAtlas.unload(pGLState);
	}

	@Override
	public void loadToHardware(final GLState pGLState) throws IOException {
		this.mTextureAtlas.loadToHardware(pGLState);
	}

	@Override
	public void unloadFromHardware(final GLState pGLState) {
		this.mTextureAtlas.unloadFromHardware(pGLState);
	}

	@Override
	public void reloadToHardware(final GLState pGLState) throws IOException {
		this.mTextureAtlas.reloadToHardware(pGLState);
	}

	@Override
	public void bind(final GLState pGLState) {
		this.mTextureAtlas.bind(pGLState);
	}

	@Override
	public void bind(final GLState pGLState, final int pGLActiveTexture) {
		this.mTextureAtlas.bind(pGLState, pGLActiveTexture);
	}

	@Override
	public PixelFormat getPixelFormat() {
		return this.mTextureAtlas.getPixelFormat();
	}

	@Override
	public TextureOptions getTextureOptions() {
		return this.mTextureAtlas.getTextureOptions();
	}

	@Override
	@Deprecated
	public void addTextureAtlasSource(final S pTextureAtlasSource, final int pTextureX, final int pTextureY) {
		this.mTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, pTextureX, pTextureY);
	}

	@Override
	@Deprecated
	public void addTextureAtlasSource(final S pTextureAtlasSource, final int pTextureX, final int pTextureY, final int pTextureAtlasSourcePadding) {
		this.mTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, pTextureX, pTextureY, pTextureAtlasSourcePadding);
	}

	@Override
	public void removeTextureAtlasSource(final S pTextureAtlasSource, final int pTextureX, final int pTextureY) {
		this.mTextureAtlas.removeTextureAtlasSource(pTextureAtlasSource, pTextureX, pTextureY);
	}

	@Override
	public void clearTextureAtlasSources() {
		this.mTextureAtlas.clearTextureAtlasSources();
		this.mTextureAtlasSourcesToPlace.clear();
	}

	/**
	 * @see {@link BuildableBitmapTextureAtlas#hasTextureAtlasStateListener()}
	 */
	@Deprecated
	@Override
	public boolean hasTextureStateListener() {
		return this.mTextureAtlas.hasTextureStateListener();
	}

	@Override
	public boolean hasTextureAtlasStateListener() {
		return this.mTextureAtlas.hasTextureAtlasStateListener();
	}

	/**
	 * @see {@link BuildableBitmapTextureAtlas#setTextureStateListener(ITextureAtlasStateListener)}
	 */
	@Deprecated
	@Override
	public ITextureAtlasStateListener<S> getTextureStateListener() {
		return this.mTextureAtlas.getTextureStateListener();
	}
	
	@Override
	public ITextureAtlasStateListener<S> getTextureAtlasStateListener() {
		return this.mTextureAtlas.getTextureAtlasStateListener();
	}

	/**
	 * @see {@link BuildableBitmapTextureAtlas#setTextureStateListener(ITextureAtlasStateListener)}
	 */
	@Deprecated
	@Override
	public void setTextureStateListener(final ITextureStateListener pTextureStateListener) {
		this.mTextureAtlas.setTextureStateListener(pTextureStateListener);
	}

	@Override
	public void setTextureAtlasStateListener(final ITextureAtlasStateListener<S> pTextureAtlasStateListener) {
		this.mTextureAtlas.setTextureAtlasStateListener(pTextureAtlasStateListener);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void addEmptyTextureAtlasSource(final int pTextureX, final int pTextureY, final int pWidth, final int pHeight) {
		this.mTextureAtlas.addEmptyTextureAtlasSource(pTextureX, pTextureY, pWidth, pHeight);
	}

	@Override
	public void addTextureAtlasSource(final S pTextureAtlasSource, final Callback<S> pCallback) {
		this.mTextureAtlasSourcesToPlace.add(new TextureAtlasSourceWithWithLocationCallback<S>(pTextureAtlasSource, pCallback));
	}

	@Override
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

	@Override
	public IBuildableTextureAtlas<S, T> build(final ITextureAtlasBuilder<S, T> pTextureAtlasBuilder) throws TextureAtlasBuilderException {
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
