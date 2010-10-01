package org.anddev.andengine.entity.particle.emitter;

import org.anddev.andengine.util.constants.Constants;

/**
 * @author Nicolas Gramlich
 * @since 23:14:42 - 01.10.2010
 */
public class PointParticleEmitter extends BaseParticleEmitter {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public PointParticleEmitter(final float pCenterX, final float pCenterY) {
		super(pCenterX, pCenterY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void getPositionOffset(final float[] pOffset) {
		pOffset[Constants.VERTEX_INDEX_X] = this.mCenterX;
		pOffset[Constants.VERTEX_INDEX_Y] = this.mCenterY;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
