package org.anddev.andengine.engine.camera.hud.controls;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

/**
 * @author Nicolas Gramlich
 * @since 00:21:55 - 11.07.2010
 */
public class DigitalOnScreenControl extends BaseOnScreenControl {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public DigitalOnScreenControl(final float pX, final float pY, final Camera pCamera, final TextureRegion pControlBaseTextureRegion, final TextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates, final IOnScreenControlListener pOnScreenControlListener) {
		super(pX, pY, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion, pTimeBetweenUpdates, pOnScreenControlListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onUpdateControlKnob(final float pRelativeX, final float pRelativeY) {
		if(pRelativeX == 0 && pRelativeY == 0) {
			super.onUpdateControlKnob(0, 0);
		}

		if(Math.abs(pRelativeX) > Math.abs(pRelativeY)) {
			if(pRelativeX > 0) {
				super.onUpdateControlKnob(0.5f, 0);
			} else if(pRelativeX < 0) {
				super.onUpdateControlKnob(-0.5f, 0);
			} else if(pRelativeX == 0) {
				super.onUpdateControlKnob(0, 0);
			}
		} else {
			if(pRelativeY > 0) {
				super.onUpdateControlKnob(0, 0.5f);
			} else if(pRelativeY < 0) {
				super.onUpdateControlKnob(0, -0.5f);
			} else if(pRelativeY == 0) {
				super.onUpdateControlKnob(0, 0);
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
