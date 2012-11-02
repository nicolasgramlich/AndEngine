package org.andengine.entity.particle.emitter;

import static org.andengine.util.Constants.VERTEX_INDEX_X;
import static org.andengine.util.Constants.VERTEX_INDEX_Y;

/**
 * This {@link IParticleEmitter} returns the same position for every particle
 * to emit (so all particles are emitted at the very same point).
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	/**
	 * Create a new {@code PointParticleEmitter}, that gives all emitted particles
	 * the same starting position.
	 * 
	 * @param pCenterX The x-coordinate for new particles.
	 * @param pCenterY The y-coordinate for new particles.
	 */
	public PointParticleEmitter(final float pCenterX, final float pCenterY) {
		super(pCenterX, pCenterY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getPositionOffset(final float[] pOffset) {
		pOffset[VERTEX_INDEX_X] = this.mCenterX;
		pOffset[VERTEX_INDEX_Y] = this.mCenterY;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
