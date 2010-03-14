package org.anddev.andengine.entity.particle;

/**
 * @author Nicolas Gramlich
 * @since 20:06:05 - 14.03.2010
 */
public interface IParticleModifier {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void onInitializeParticle(final Particle pParticle);
	
	public void onUpdateParticle(final Particle pParticle);
}
