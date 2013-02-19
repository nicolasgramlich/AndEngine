package org.andengine.engine.camera.hud.controls;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 00:21:55 - 11.07.2010
 */
public class DigitalOnScreenControl extends BaseOnScreenControl {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final float EXTENT_SIDE = 0.5f;
	private static final float EXTENT_DIAGONAL = 0.354f;

	private static final float ANGLE_DELTA = 22.5f;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mAllowDiagonal;

	// ===========================================================
	// Constructors
	// ===========================================================

	public DigitalOnScreenControl(final float pX, final float pY, final Camera pCamera, final ITextureRegion pControlBaseTextureRegion, final ITextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates, final VertexBufferObjectManager pVertexBufferObjectManager, final IOnScreenControlListener pOnScreenControlListener) {
		this(pX, pY, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion, pTimeBetweenUpdates, false, pVertexBufferObjectManager, pOnScreenControlListener);
	}

	public DigitalOnScreenControl(final float pX, final float pY, final Camera pCamera, final ITextureRegion pControlBaseTextureRegion, final ITextureRegion pControlKnobTextureRegion, final float pTimeBetweenUpdates, final boolean pAllowDiagonal, final VertexBufferObjectManager pVertexBufferObjectManager, final IOnScreenControlListener pOnScreenControlListener) {
		super(pX, pY, pCamera, pControlBaseTextureRegion, pControlKnobTextureRegion, pTimeBetweenUpdates, pVertexBufferObjectManager, pOnScreenControlListener);

		this.mAllowDiagonal = pAllowDiagonal;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isAllowDiagonal() {
		return this.mAllowDiagonal;
	}

	public void setAllowDiagonal(final boolean pAllowDiagonal) {
		this.mAllowDiagonal = pAllowDiagonal;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onUpdateControlKnob(final float pRelativeX, final float pRelativeY) {
		if (pRelativeX == 0 && pRelativeY == 0) {
			super.onUpdateControlKnob(0, 0);
			return;
		}

		if (this.mAllowDiagonal) {
			final float angle = MathUtils.radToDeg(MathUtils.atan2(pRelativeY, pRelativeX)) + 180;
			if (this.testDiagonalAngle(0, angle) || this.testDiagonalAngle(360, angle)) {
				super.onUpdateControlKnob(-EXTENT_SIDE, 0);
			} else if (this.testDiagonalAngle(45, angle)) {
				super.onUpdateControlKnob(-EXTENT_DIAGONAL, -EXTENT_DIAGONAL);
			} else if (this.testDiagonalAngle(90, angle)) {
				super.onUpdateControlKnob(0, -EXTENT_SIDE);
			} else if (this.testDiagonalAngle(135, angle)) {
				super.onUpdateControlKnob(EXTENT_DIAGONAL, -EXTENT_DIAGONAL);
			} else if (this.testDiagonalAngle(180, angle)) {
				super.onUpdateControlKnob(EXTENT_SIDE, 0);
			} else if (this.testDiagonalAngle(225, angle)) {
				super.onUpdateControlKnob(EXTENT_DIAGONAL, EXTENT_DIAGONAL);
			} else if (this.testDiagonalAngle(270, angle)) {
				super.onUpdateControlKnob(0, EXTENT_SIDE);
			} else if (this.testDiagonalAngle(315, angle)) {
				super.onUpdateControlKnob(-EXTENT_DIAGONAL, EXTENT_DIAGONAL);
			} else {
				super.onUpdateControlKnob(0, 0);
			}
		} else {
			if (Math.abs(pRelativeX) > Math.abs(pRelativeY)) {
				if (pRelativeX > 0) {
					super.onUpdateControlKnob(EXTENT_SIDE, 0);
				} else if (pRelativeX < 0) {
					super.onUpdateControlKnob(-EXTENT_SIDE, 0);
				} else if (pRelativeX == 0) {
					super.onUpdateControlKnob(0, 0);
				}
			} else {
				if (pRelativeY > 0) {
					super.onUpdateControlKnob(0, EXTENT_SIDE);
				} else if (pRelativeY < 0) {
					super.onUpdateControlKnob(0, -EXTENT_SIDE);
				} else if (pRelativeY == 0) {
					super.onUpdateControlKnob(0, 0);
				}
			}
		}
	}

	private boolean testDiagonalAngle(final float pTestAngle, final float pActualAngle) {
		return pActualAngle > pTestAngle - ANGLE_DELTA && pActualAngle < pTestAngle + ANGLE_DELTA;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
