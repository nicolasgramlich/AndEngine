package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.Particle;

/**
 * @author Nicolas Gramlich
 * @since 20:37:27 - 04.05.2010
 */
public class ScaleModifier extends BaseSingleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScaleModifier(final float pFromScale, final float pToScale, final float pFromTime, final float pToTime) {
		super(pFromScale, pToScale, pFromTime, pToTime);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValue(final Particle pParticle, final float pScale) {
		pParticle.setScale(pScale);
	}

	@Override
	protected void onSetValue(final Particle pParticle, final float pScale) {
		pParticle.setScale(pScale);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
