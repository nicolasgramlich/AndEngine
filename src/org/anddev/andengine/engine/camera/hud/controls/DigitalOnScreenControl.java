package org.anddev.andengine.engine.camera.hud.controls;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.MathUtils;

/**
 * @author Nicolas Gramlich
 * @since 00:21:55 - 11.07.2010
 */
public class DigitalOnScreenControl extends BaseOnScreenControl {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float DIAGONAL_ANGLE_DELTA = 22.5f;

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final boolean mAllowDiagonal;

	// ===========================================================
	// Constructors
	// ===========================================================

	public DigitalOnScreenControl(final float pX, final float pY, final Camera pCamera, final TextureRegion pControlBaseTextureRegion, final TextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates, final IOnScreenControlListener pOnScreenControlListener) {
		this(pX, pY, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion, pTimeBetweenUpdates, pOnScreenControlListener, false);
	}

	public DigitalOnScreenControl(final float pX, final float pY, final Camera pCamera, final TextureRegion pControlBaseTextureRegion, final TextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates, final IOnScreenControlListener pOnScreenControlListener, final boolean pAllowDiagonal) {
		super(pX, pY, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion, pTimeBetweenUpdates, pOnScreenControlListener);
		this.mAllowDiagonal = pAllowDiagonal;
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
			return;
		}

		if(this.mAllowDiagonal) {
			final float angle = MathUtils.radToDeg(MathUtils.atan2(pRelativeY, pRelativeX)) + 180;
			if(testDiagonalAngle(0, angle) || testDiagonalAngle(360, angle)) {
				super.onUpdateControlKnob(-0.5f, 0);				
			} else if(testDiagonalAngle(45, angle)) {
				super.onUpdateControlKnob(-0.354f, -0.354f);				
			} else if(testDiagonalAngle(90, angle)) {
				super.onUpdateControlKnob(0, -0.5f);				
			} else if(testDiagonalAngle(135, angle)) {
				super.onUpdateControlKnob(0.354f, -0.354f);				
			} else if(testDiagonalAngle(180, angle)) {
				super.onUpdateControlKnob(0.5f, 0);				
			} else if(testDiagonalAngle(225, angle)) {
				super.onUpdateControlKnob(0.354f, 0.354f);				
			} else if(testDiagonalAngle(270, angle)) {
				super.onUpdateControlKnob(0, 0.5f);				
			} else if(testDiagonalAngle(315, angle)) {
				super.onUpdateControlKnob(-0.354f, 0.354f);				
			} else {
				super.onUpdateControlKnob(0, 0);
			}
		} else {
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
	}

	private boolean testDiagonalAngle(final float pTestAngle, final float pActualAngle) {
		return pActualAngle > pTestAngle - DIAGONAL_ANGLE_DELTA && pActualAngle < pTestAngle + DIAGONAL_ANGLE_DELTA;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
