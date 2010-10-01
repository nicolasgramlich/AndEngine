package org.anddev.andengine.entity.particle.emitter;

import org.anddev.andengine.engine.handler.IUpdateHandler;

/**
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

	public void getPositionOffset(final float[] pOffset);
}
