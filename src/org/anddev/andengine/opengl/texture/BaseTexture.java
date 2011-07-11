package org.anddev.andengine.opengl.texture;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 14:55:02 - 08.03.2010
 */
public abstract class BaseTexture implements ITexture {
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

	protected final ITextureStateListener mTextureStateListener;

	protected boolean mUpdateOnHardwareNeeded = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pTextureOptions the (quality) settings of the Texture.
	 * @param pTextureStateListener to be informed when this {@link BaseTexture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public BaseTexture(final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException {
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
	public boolean isUpdateOnHardwareNeeded() {
		return this.mUpdateOnHardwareNeeded;
	}

	@Override
	public void setLoadedToHardware(final boolean pLoadedToHardware) {
		this.mLoadedToHardware = pLoadedToHardware;
	}

	@Override
	public TextureOptions getTextureOptions() {
		return this.mTextureOptions;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void writeTextureToHardware(final GL10 pGL) throws IOException;

	// ===========================================================
	// Methods
	// ===========================================================

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

	public static interface ITextureStateListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onLoadedToHardware(final ITexture pTexture);
		public void onTextureSourceLoadExeption(final ITexture pTexture, final ITextureSource pTextureSource, final Throwable pThrowable);
		public void onUnloadedFromHardware(final ITexture pTexture);

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class TextureStateAdapter implements ITextureStateListener {
			@Override
			public void onLoadedToHardware(final ITexture pTexture) { }

			@Override
			public void onTextureSourceLoadExeption(final ITexture pTexture, final ITextureSource pTextureSource, final Throwable pThrowable) { }

			@Override
			public void onUnloadedFromHardware(final ITexture pTexture) { }
		}

		public static class DebugTextureStateListener implements ITextureStateListener {
			@Override
			public void onLoadedToHardware(final ITexture pTexture) {
				Debug.d("Texture loaded: " + pTexture.toString());
			}

			@Override
			public void onTextureSourceLoadExeption(final ITexture pTexture, final ITextureSource pTextureSource, final Throwable pThrowable) {
				Debug.e("Exception loading TextureSource. Texture: " + pTexture.toString() + " TextureSource: " + pTextureSource.toString(), pThrowable);
			}

			@Override
			public void onUnloadedFromHardware(final ITexture pTexture) {
				Debug.d("Texture unloaded: " + pTexture.toString());
			}
		}
	}
}
