package org.andengine.entity.particle.initializer;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.shape.Shape;

import android.opengl.GLES20;

/**
 * Sets the blend function used to draw the {@link Particle} during its lifespan. Only applicable to {@link Shape}s.
 * 
 * @author janne.sinivirta
 * 
 * @since 17:24:32 - 28.12.2011
 */
public class BlendFunctionParticleInitializer<T extends IShape> implements IParticleInitializer<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mSourceBlendFunction;
	protected int mDestinationBlendFunction;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Blend function set for each spawned {@link Particle}. Use {@link GLES20} constants for setting functions.
	 */
	public BlendFunctionParticleInitializer(final int pSourceBlendFunction, final int pDestinationBlendFunction) {
		this.mSourceBlendFunction = pSourceBlendFunction;
		this.mDestinationBlendFunction = pDestinationBlendFunction;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onInitializeParticle(final Particle<T> pParticle) {
		pParticle.getEntity().setBlendFunction(this.mSourceBlendFunction, this.mDestinationBlendFunction);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
