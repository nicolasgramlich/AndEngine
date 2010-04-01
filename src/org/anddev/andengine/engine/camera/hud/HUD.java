package org.anddev.andengine.engine.camera.hud;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.CameraScene;
import org.anddev.andengine.entity.Scene;

/**
 * While you can add a {@link HUD} to {@link Scene}, you should not do so.
 * {@link HUD}s are meant to be added to {@link Camera}s via {@link Camera#setHUD(HUD)}.
 * 
 * @author Nicolas Gramlich
 * @since 14:13:13 - 01.04.2010
 */
public class HUD extends CameraScene {
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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Deprecated
	@Override
	protected void drawBackground(final GL10 pGL) {
		/* HUD has no background. */
	}

	@Deprecated
	@Override
	public void setBackgroundColor(final float pRed, final float pGreen, final float pBlue) {
		/* Nothing. */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
