package org.anddev.andengine.opengl.texture;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.Debug;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:55:02 - 08.03.2010
 */
public abstract class BaseTexture<T extends ITextureSource> implements ITexture<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] HARDWARETEXTUREID_FETCHER = new int[1];

	// ===========================================================
	// Fields
	// ===========================================================

	protected boolean mLoadedToHardware;
	protected int mHardwareTextureID = -1;
	protected final TextureOptions mTextureOptions;

	protected final ITextureStateListener<T> mTextureStateListener;

	protected boolean mUpdateOnHardwareNeeded = false;

	protected final ArrayList<T> mTextureSources = new ArrayList<T>();

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pTextureOptions the (quality) settings of the Texture.
	 * @param pTextureStateListener to be informed when this {@link BaseTexture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public BaseTexture(final TextureOptions pTextureOptions, final ITextureStateListener<T> pTextureStateListener) throws IllegalArgumentException {
		this.mTextureOptions = pTextureOptions;
		this.mTextureStateListener = pTextureStateListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getHardwareTextureID() {
		return this.mHardwareTextureID;
	}

	@Override
	public boolean isLoadedToHardware() {
		return this.mLoadedToHardware;
	}

	@Override
	public void setLoadedToHardware(final boolean pLoadedToHardware) {
		this.mLoadedToHardware = pLoadedToHardware;
	}

	@Override
	public boolean isUpdateOnHardwareNeeded() {
		return this.mUpdateOnHardwareNeeded;
	}

	@Override
	public void setUpdateOnHardwareNeeded(final boolean pUpdateOnHardwareNeeded) {
		this.mUpdateOnHardwareNeeded = pUpdateOnHardwareNeeded;
	}

	@Override
	public TextureOptions getTextureOptions() {
		return this.mTextureOptions;
	}
	
	public ITextureStateListener<T> getTextureStateListener() {
		return this.mTextureStateListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void writeTextureToHardware(final GL10 pGL) throws IOException;


	@Override
	public void loadToHardware(final GL10 pGL) throws IOException {
		GLHelper.enableTextures(pGL);

		this.mHardwareTextureID = BaseTexture.generateHardwareTextureID(pGL);

		this.bindTextureOnHardware(pGL);

		this.applyTextureOptions(pGL);

		this.writeTextureToHardware(pGL);

		this.mUpdateOnHardwareNeeded = false;
		this.mLoadedToHardware = true;

		if(this.mTextureStateListener != null) {
			this.mTextureStateListener.onLoadedToHardware(this);
		}
	}

	@Override
	public void unloadFromHardware(final GL10 pGL) {
		GLHelper.enableTextures(pGL);

		this.deleteTextureOnHardware(pGL);

		this.mHardwareTextureID = -1;

		this.mLoadedToHardware = false;

		if(this.mTextureStateListener != null) {
			this.mTextureStateListener.onUnloadedFromHardware(this);
		}
	}

	@Override
	public void reloadToHardware(final GL10 pGL) throws IOException {
		this.unloadFromHardware(pGL);
		this.loadToHardware(pGL);
	}

	@Override
	public void bind(final GL10 pGL) {
		GLHelper.bindTexture(pGL, this.mHardwareTextureID);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void addTextureSource(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY) throws IllegalArgumentException {
		this.checkTextureSourcePosition(pTextureSource, pTexturePositionX, pTexturePositionY);
		pTextureSource.setTexturePositionX(pTexturePositionX);
		pTextureSource.setTexturePositionY(pTexturePositionY);
		this.mTextureSources.add(pTextureSource);
		this.mUpdateOnHardwareNeeded = true;
	}

	private void checkTextureSourcePosition(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY) throws IllegalArgumentException {
		if(pTexturePositionX < 0) {
			throw new IllegalArgumentException("Illegal negative pTexturePositionX supplied: '" + pTexturePositionX + "'");
		} else if(pTexturePositionY < 0) {
			throw new IllegalArgumentException("Illegal negative pTexturePositionY supplied: '" + pTexturePositionY + "'");
		} else if(pTexturePositionX + pTextureSource.getWidth() > this.getWidth() || pTexturePositionY + pTextureSource.getHeight() > this.getHeight()) {
			throw new IllegalArgumentException("Supplied pTextureSource must not exceed bounds of Texture.");
		}
	}

	@Override
	public void removeTextureSource(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
		final ArrayList<T> textureSources = this.mTextureSources;
		for(int i = textureSources.size() - 1; i >= 0; i--) {
			final T textureSource = textureSources.get(i);
			if(textureSource == pTextureSource && textureSource.getTexturePositionX() == pTexturePositionX && textureSource.getTexturePositionY() == pTexturePositionY) {
				textureSources.remove(i);
				this.mUpdateOnHardwareNeeded = true;
				return;
			}
		}
	}

	public void clearTextureSources() {
		this.mTextureSources.clear();
		this.mUpdateOnHardwareNeeded = true;
	}

	private void applyTextureOptions(final GL10 pGL) {
		final TextureOptions textureOptions = this.mTextureOptions;
		pGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, textureOptions.mMinFilter);
		pGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, textureOptions.mMagFilter);
		pGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, textureOptions.mWrapS);
		pGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, textureOptions.mWrapT);
		pGL.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, textureOptions.mTextureEnvironment);
	}

	protected void bindTextureOnHardware(final GL10 pGL) {
		GLHelper.forceBindTexture(pGL, this.mHardwareTextureID);
	}

	private void deleteTextureOnHardware(final GL10 pGL) {
		GLHelper.deleteTexture(pGL, this.mHardwareTextureID);
	}

	private static int generateHardwareTextureID(final GL10 pGL) {
		pGL.glGenTextures(1, BaseTexture.HARDWARETEXTUREID_FETCHER, 0);

		return BaseTexture.HARDWARETEXTUREID_FETCHER[0];
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ITextureStateListener<T extends ITextureSource> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onLoadedToHardware(final ITexture<T> pTexture);
		public void onTextureSourceLoadExeption(final ITexture<T> pTexture, final T pTextureSource, final Throwable pThrowable);
		public void onUnloadedFromHardware(final ITexture<T> pTexture);

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class TextureStateAdapter<T extends ITextureSource> implements ITextureStateListener<T> {
			@Override
			public void onLoadedToHardware(final ITexture<T> pTexture) { }

			@Override
			public void onTextureSourceLoadExeption(final ITexture<T> pTexture, final T pTextureSource, final Throwable pThrowable) { }

			@Override
			public void onUnloadedFromHardware(final ITexture<T> pTexture) { }
		}

		public static class DebugTextureStateListener<T extends ITextureSource> implements ITextureStateListener<T> {
			@Override
			public void onLoadedToHardware(final ITexture<T> pTexture) {
				Debug.d("Texture loaded: " + pTexture.toString());
			}

			@Override
			public void onTextureSourceLoadExeption(final ITexture<T> pTexture, final T pTextureSource, final Throwable pThrowable) {
				Debug.e("Exception loading TextureSource. Texture: " + pTexture.toString() + " TextureSource: " + pTextureSource.toString(), pThrowable);
			}

			@Override
			public void onUnloadedFromHardware(final ITexture<T> pTexture) {
				Debug.d("Texture unloaded: " + pTexture.toString());
			}
		}
	}
}
