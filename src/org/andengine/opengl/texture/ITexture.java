package org.andengine.opengl.texture;

import java.io.IOException;

import org.andengine.opengl.util.GLState;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:01:03 - 11.07.2011
 */
public interface ITexture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getWidth();
	public int getHeight();

	public int getHardwareTextureID();

	public boolean isLoadedToHardware();
	public void setNotLoadedToHardware();

	public boolean isUpdateOnHardwareNeeded();
	public void setUpdateOnHardwareNeeded(final boolean pUpdateOnHardwareNeeded);

	/**
	 * @see {@link TextureManager#loadTexture(ITexture)}.
	 */
	public void load();
	/**
	 * @see {@link TextureManager#loadTexture(GLState, ITexture)}.
	 */
	public void load(final GLState pGLState) throws IOException;
	/**
	 * @see {@link TextureManager#unloadTexture(ITexture)}.
	 */
	public void unload();
	/**
	 * @see {@link TextureManager#unloadTexture(GLState, ITexture)}.
	 */
	public void unload(final GLState pGLState);

	public void loadToHardware(final GLState pGLState) throws IOException;
	public void unloadFromHardware(final GLState pGLState);
	public void reloadToHardware(final GLState pGLState) throws IOException;

	public void bind(final GLState pGLState);
	/**
	 * @param pGLActiveTexture from {@link GLES20#GL_TEXTURE0} to {@link GLES20#GL_TEXTURE31}.
	 */
	public void bind(final GLState pGLState, final int pGLActiveTexture);

	public PixelFormat getPixelFormat();
	public TextureOptions getTextureOptions();

	/**
	 * @return in kiloBytes.
	 */
	public int getTextureMemorySize();

	public boolean hasTextureStateListener();
	public ITextureStateListener getTextureStateListener();
	public void setTextureStateListener(final ITextureStateListener pTextureStateListener);

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
}