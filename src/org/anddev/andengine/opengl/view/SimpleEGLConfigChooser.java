package org.anddev.andengine.opengl.view;


/**
 * This class will choose a supported surface as close to RGB565 as
 * possible, with or without a depth buffer.
 * 
 * @author Nicolas Gramlich
 * @since 20:53:29 - 28.06.2010
 */
class SimpleEGLConfigChooser extends ComponentSizeChooser {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SimpleEGLConfigChooser(final boolean pWithDepthBuffer) {
		super(4, 4, 4, 0, pWithDepthBuffer ? 16 : 0, 0);
		// Adjust target values. This way we'll accept a 4444 or
		// 555 buffer if there's no 565 buffer available.
		this.mRedSize = 5;
		this.mGreenSize = 6;
		this.mBlueSize = 5;
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
}