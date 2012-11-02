package org.andengine.entity.particle.emitter;

import static org.andengine.util.Constants.VERTEX_INDEX_X;
import static org.andengine.util.Constants.VERTEX_INDEX_Y;

import org.andengine.util.math.MathConstants;
import org.andengine.util.math.MathUtils;

import android.util.FloatMath;

/**
 * An {@link IParticleEmitter} that emits particles on the outline of a circle
 * or ellipsis.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:18:41 - 01.10.2010
 */
public class CircleOutlineParticleEmitter extends BaseCircleParticleEmitter {
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
	 * Creates a new {@code CircleOutlineParticleEmitter} that emits particles
	 * on the outline of a circle with a given center and radius.
	 * 
	 * @param pCenterX The x-coordinate of the center of the circle.
	 * @param pCenterY The y-coordinate of the center of the circle.
	 * @param pRadius The radius of the circle.
	 */
	public CircleOutlineParticleEmitter(final float pCenterX, final float pCenterY, final float pRadius) {
		super(pCenterX, pCenterY, pRadius);
	}

	/**
	 * Creates a new {@code CircleOutlineParticleEmitter} that emits particles
	 * on the outline of an ellipsis with a given center as well as an x- and y-radius.
	 * 
	 * @param pCenterX The x-coordinate of the center of the ellipsis.
	 * @param pCenterY The y-coordinate of the center of the ellipsis.
	 * @param pRadiusX The x-radius of the ellipsis.
	 * @param pRadiusY The-y-radius of the ellipsis.
	 */
	public CircleOutlineParticleEmitter(final float pCenterX, final float pCenterY, final float pRadiusX, final float pRadiusY) {
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
		pOffset[VERTEX_INDEX_X] = this.mCenterX + FloatMath.cos(random) * this.mRadiusX;
		pOffset[VERTEX_INDEX_Y] = this.mCenterY + FloatMath.sin(random) * this.mRadiusY;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
