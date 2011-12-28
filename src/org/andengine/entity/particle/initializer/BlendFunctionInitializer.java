package org.andengine.entity.particle.initializer;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.shape.Shape;

/**
 * Sets the blend function used to draw the particle during it's lifespan. Only applicable to Shapes.
 * 
 * @author janne.sinivirta
 *
 * @param <T> type of particle
 */
public class BlendFunctionInitializer<T extends Shape> implements IParticleInitializer<T> {

	protected int mSourceBlendFunction;
	protected int mDestinationBlendFunction;
	
	/**
	 * Blend function set for each spawned particle. Use GLES20 constants for setting functions.
	 * @param pSourceBlendFunction
	 * @param pDestinationBlendFunction
	 */
	public BlendFunctionInitializer(final int pSourceBlendFunction, final int pDestinationBlendFunction) {
		this.mSourceBlendFunction = pSourceBlendFunction;
		this.mDestinationBlendFunction = pDestinationBlendFunction;
	}

	@Override
	public void onInitializeParticle(Particle<T> pParticle) {
		pParticle.getEntity().setBlendFunction(mSourceBlendFunction, mDestinationBlendFunction);
	}
}
