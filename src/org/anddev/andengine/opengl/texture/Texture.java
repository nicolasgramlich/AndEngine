package org.anddev.andengine.opengl.texture;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.source.ITextureAtlasSource;
import org.anddev.andengine.opengl.util.GLHelper;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:55:02 - 08.03.2010
 */
public abstract class Texture implements ITexture {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] HARDWARETEXTUREID_FETCHER = new int[1];

	// ===========================================================
	// Fields
	// ===========================================================

	protected final PixelFormat mPixelFormat;
	protected final TextureOptions mTextureOptions;

	protected int mHardwareTextureID = -1;
	protected boolean mLoadedToHardware;
	protected boolean mUpdateOnHardwareNeeded = false;

	protected final ITextureStateListener mTextureStateListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pPixelFormat
	 * @param pTextureOptions the (quality) settings of the Texture.
	 * @param pTextureStateListener to be informed when this {@link Texture} is loaded, unloaded or a {@link ITextureAtlasSource} failed to load.
	 */
	public Texture(final PixelFormat pPixelFormat, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException {
		this.mPixelFormat = pPixelFormat;
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
	
	public PixelFormat getPixelFormat() {
		return this.mPixelFormat;
	}

	@Override
	public TextureOptions getTextureOptions() {
		return this.mTextureOptions;
	}

	@Override
	public ITextureStateListener getTextureStateListener() {
		return this.mTextureStateListener;
	}

	@Override
	public boolean hasTextureStateListener() {
		return this.mTextureStateListener != null;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void writeTextureToHardware(final GL10 pGL) throws IOException;

	@Override
	public void loadToHardware(final GL10 pGL) throws IOException {
		GLHelper.enableTextures(pGL);

		this.generateHardwareTextureID(pGL);

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

	protected void applyTextureOptions(final GL10 pGL) {
		this.mTextureOptions.apply(pGL);
	}

	protected void bindTextureOnHardware(final GL10 pGL) {
		GLHelper.forceBindTexture(pGL, this.mHardwareTextureID);
	}

	protected void deleteTextureOnHardware(final GL10 pGL) {
		GLHelper.deleteTexture(pGL, this.mHardwareTextureID);
	}

	protected void generateHardwareTextureID(final GL10 pGL) {
		pGL.glGenTextures(1, Texture.HARDWARETEXTUREID_FETCHER, 0);

		this.mHardwareTextureID = Texture.HARDWARETEXTUREID_FETCHER[0];
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public enum PixelFormat {
		// ===========================================================
		// Elements
		// ===========================================================

		UNDEFINED(-1, -1, -1),
		RGBA_4444(GL10.GL_RGBA, GL10.GL_UNSIGNED_SHORT_4_4_4_4, 16),
		RGBA_5551(GL10.GL_RGBA, GL10.GL_UNSIGNED_SHORT_5_5_5_1, 16),
		RGBA_8888(GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, 32),
		RGB_565(GL10.GL_RGB, GL10.GL_UNSIGNED_SHORT_5_6_5, 16),
		A_8(GL10.GL_ALPHA, GL10.GL_UNSIGNED_BYTE, 8),
		I_8(GL10.GL_LUMINANCE, GL10.GL_UNSIGNED_BYTE, 8),
		AI_8(GL10.GL_LUMINANCE_ALPHA, GL10.GL_UNSIGNED_BYTE, 16);

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mGLFormat;
		private final int mGLType;
		private final int mBitsPerPixel;

		// ===========================================================
		// Constructors
		// ===========================================================

		private PixelFormat(final int pGLFormat, final int pGLType, final int pBitsPerPixel) {
			this.mGLFormat = pGLFormat;
			this.mGLType = pGLType;
			this.mBitsPerPixel = pBitsPerPixel;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getGLFormat() {
			return this.mGLFormat;
		}

		public int getGLType() {
			return this.mGLType;
		}

		public int getBitsPerPixel() {
			return this.mBitsPerPixel;
		}

		// ===========================================================
		// Methods from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}