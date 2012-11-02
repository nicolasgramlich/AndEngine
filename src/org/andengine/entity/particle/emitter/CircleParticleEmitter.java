package org.andengine.entity.particle.emitter;

import static org.andengine.util.Constants.VERTEX_INDEX_X;
import static org.andengine.util.Constants.VERTEX_INDEX_Y;

import org.andengine.util.math.MathConstants;
import org.andengine.util.math.MathUtils;

import android.util.FloatMath;

/**
 * An {@link IParticleEmitter} that emits new particles inside a specific
 * circle or ellipsis. If you want particles to be emitted only on the outline
 * use a {@link CircleOutlineParticleEmitter}.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:18:41 - 01.10.2010
 */
public class CircleParticleEmitter extends BaseCircleParticleEmitter {
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
	 * Creates a new {@code CircleParticleEmitter} that emits particles
	 * inside a circle with a given center and radius.
	 * 
	 * @param pCenterX The x-coordinate of the center of the circle.
	 * @param pCenterY The y-coordinate of the center of the circle.
	 * @param pRadius The radius of the circle.
	 */
	public CircleParticleEmitter(final float pCenterX, final float pCenterY, final float pRadius) {
		super(pCenterX, pCenterY, pRadius);
	}

	/**
	 * Creates a new {@code CircleParticleEmitter} that emits particles
	 * inside an ellipsis with a given center as well as an x- and y-radius.
	 * 
	 * @param pCenterX The x-coordinate of the center of the ellipsis.
	 * @param pCenterY The y-coordinate of the center of the ellipsis.
	 * @param pRadiusX The x-radius of the ellipsis.
	 * @param pRadiusY The y-radius of the ellipsis.
	 */
	public CircleParticleEmitter(final float pCenterX, final float pCenterY, final float pRadiusX, final float pRadiusY) {
		super(pCenterX, pCenterY, pRadiusX, pRadiusY);
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
		final float random = MathUtils.RANDOM.nextFloat() * MathConstants.PI * 2;
		pOffset[VERTEX_INDEX_X] = this.mCenterX + FloatMath.cos(random) * this.mRadiusX * MathUtils.RANDOM.nextFloat();
		pOffset[VERTEX_INDEX_Y] = this.mCenterY + FloatMath.sin(random) * this.mRadiusY * MathUtils.RANDOM.nextFloat();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
