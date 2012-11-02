package org.andengine.entity.particle.emitter;

import static org.andengine.util.Constants.VERTEX_INDEX_X;
import static org.andengine.util.Constants.VERTEX_INDEX_Y;

import org.andengine.util.math.MathUtils;

/**
 * An {@link IParticleEmitter} that emits new particles on one of the vertices
 * of an rectangle.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:48:00 - 01.10.2010
 */
public class RectangleOutlineParticleEmitter extends BaseRectangleParticleEmitter {
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
	 * Create a new {@code RectangleOutlineParticleEmitter} that emits new particles
	 * on one of the vertices of the rectangle.
	 * 
	 * @param pCenterX The x-coordinate of the center of the rectangle.
	 * @param pCenterY The y-coordinate of the center of the rectangle.
	 * @param pWidth The width of the rectangle.
	 * @param pHeight The height of the rectangle.
	 */
	public RectangleOutlineParticleEmitter(final float pCenterX, final float pCenterY, final float pWidth, final float pHeight) {
		super(pCenterX, pCenterY, pWidth, pHeight);
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
		pOffset[VERTEX_INDEX_X] = this.mCenterX + MathUtils.randomSign() * this.mWidthHalf;
		pOffset[VERTEX_INDEX_Y] = this.mCenterY + MathUtils.randomSign() * this.mHeightHalf;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
