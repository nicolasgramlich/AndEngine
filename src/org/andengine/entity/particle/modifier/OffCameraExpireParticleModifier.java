package org.andengine.entity.particle.modifier;


import org.andengine.engine.camera.Camera;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.shape.RectangularShape;

/**
 * An {@link IParticleModifier} that expires a particle, if it isn't visible to
 * a specific camera anymore.
 * See the documentation of the {@link IParticleModifier} for usage hints.
 * <p>
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @see Camera
 * 
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class OffCameraExpireParticleModifier<T extends RectangularShape> implements IParticleModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Camera mCamera;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Creates a new {@code OffCameraExpireParticleModifier} that expires particles
	 * when they aren't visible to the specified camera anymore.
	 * 
	 * @param pCamera The {@link Camera} that particles must leave to expire.
	 */
	public OffCameraExpireParticleModifier(final Camera pCamera) {
		this.mCamera = pCamera;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Camera getCamera() {
		return this.mCamera;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onInitializeParticle(final Particle<T> pParticle) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUpdateParticle(final Particle<T> pParticle) {
		if(!this.mCamera.isRectangularShapeVisible(pParticle.getEntity())) {
			pParticle.setExpired(true);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
