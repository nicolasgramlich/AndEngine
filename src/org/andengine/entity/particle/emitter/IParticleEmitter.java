package org.andengine.entity.particle.emitter;

import org.andengine.engine.handler.IUpdateHandler;

/**
 * An {@code IParticleEmitter} is used by a {@link org.andengine.entity.particle.ParticleSystem}
 * to determine the position for an emitted {@link Particle}.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:48:09 - 01.10.2010
 */
public interface IParticleEmitter extends IUpdateHandler {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Writes the position for the new {@link Particle} into {@code pOffset}.
	 * Use {@link org.andengine.util.Constants#VERTEX_INDEX_X} as index of the array
	 * to store the x-coordinate and {@link org.andengine.util.Constants#VERTEX_INDEX_Y}
	 * as index to store the y-coordinate.
	 *  
	 * @param pOffset The array in which the new position has to be stored.
	 */
	public void getPositionOffset(final float[] pOffset);
}
