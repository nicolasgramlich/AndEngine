package org.anddev.andengine.engine;

import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * @author  Nicolas Gramlich
 * @since  15:59:52 - 09.03.2010
 */
public class EngineOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mGameWidth;
	private final int mGameHeight;
	private final ScreenOrientation mScreenOrientation;
	private final boolean mFullscreen;
	private final ITextureSource mLoadingScreenTextureSource;

	// ===========================================================
	// Constructors
	// ===========================================================

	public EngineOptions(final ITextureSource pLoadingScreenTextureSource, final boolean pFullscreen, final ScreenOrientation pScreenOrientation, final int pGameWidth, final int pGameHeight) {
		this.mLoadingScreenTextureSource = pLoadingScreenTextureSource;
		this.mFullscreen = pFullscreen;
		this.mScreenOrientation = pScreenOrientation;
		this.mGameWidth = pGameWidth;
		this.mGameHeight = pGameHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public int getGameWidth() {
		return this.mGameWidth;
	}

	public int getGameHeight() {
		return this.mGameHeight;
	}

	public ScreenOrientation getScreenOrientation() {
		return this.mScreenOrientation;
	}

	public boolean isFullscreen() {
		return this.mFullscreen;
	}
	
	public ITextureSource getLoadingScreenTextureSource() {
		return this.mLoadingScreenTextureSource;
	}
	
	public boolean hasLoadingScreen() {
		return this.mLoadingScreenTextureSource != null;
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



	public static enum ScreenOrientation {
		LANDSCAPE, PORTRAIT
	}
}