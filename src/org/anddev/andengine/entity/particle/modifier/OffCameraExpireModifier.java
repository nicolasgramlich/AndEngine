package org.anddev.andengine.entity.particle.modifier;


import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.particle.Particle;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class OffCameraExpireModifier implements IParticleModifier {
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

	public OffCameraExpireModifier(final Camera pCamera) {
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

	@Override
	public void onInitializeParticle(final Particle pParticle) {

	}

	@Override
	public void onUpdateParticle(final Particle pParticle) {
		if(!this.mCamera.isRectangularShapeVisible(pParticle)) {
			pParticle.setDead(true);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
