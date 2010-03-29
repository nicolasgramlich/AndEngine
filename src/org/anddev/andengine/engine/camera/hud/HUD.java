package org.anddev.andengine.engine.camera.hud;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.Scene;

/**
 * @author Nicolas Gramlich
 * @since 15:35:53 - 29.03.2010
 */
public class HUD extends Scene {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public HUD() {
		this(1);
	}

	public HUD(final int pLayerCount) {
		super(pLayerCount);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Deprecated
	@Override
	public void setBackgroundColor(final float pRed, final float pGreen, final float pBlue) {
		/* Nothing. */
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void drawBackground(final GL10 pGL) {
		/* HUD has no background. */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
