package org.andengine.engine.camera.hud;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.Scene;

/**
 * While you can add a {@link HUD} to a {@link Scene}, you should not do so.
 * {@link HUD}s are meant to be added to {@link Camera}s via
 * {@link Camera#setHUD(HUD)}. <br>
 * HUD is the acronym of head-up display and it represents all those parts of
 * the user interface that are placed on a fixed position on the display. In a
 * game this normally includes life bars, ammunition count, maps, level, etc. <br>
 * (c) 2010 Nicolas Gramlich <br>
 * (c) 2011 Zynga Inc.
 * 
 * 
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga Inc.
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
		super();

		this.setBackgroundEnabled(false);
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
