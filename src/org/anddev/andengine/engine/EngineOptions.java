package org.anddev.andengine.engine;

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

	public final int mGameWidth;
	public final int mGameHeight;
	public final ScreenOrientation mScreenOrientation;
	public final boolean mFullscreen;

	// ===========================================================
	// Constructors
	// ===========================================================

	public EngineOptions(final boolean pFullscreen, final ScreenOrientation pScreenOrientation, final int pGameWidth, final int pGameHeight) {
		this.mFullscreen = pFullscreen;
		this.mScreenOrientation = pScreenOrientation;
		this.mGameWidth = pGameWidth;
		this.mGameHeight = pGameHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

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