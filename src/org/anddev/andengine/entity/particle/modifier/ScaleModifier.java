package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.Particle;

/**
 * @author Nicolas Gramlich
 * @since 20:37:27 - 04.05.2010
 */
public class ScaleModifier extends BaseDoubleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public ScaleModifier(float pFromValueA, float pToValueA, float pFromValueB, float pToValueB, float pFromTime, float pToTime) {
		super(pFromValueA, pToValueA, pFromValueB, pToValueB, pFromTime, pToTime);
	}

	@Override
	protected void onSetInitialValue(Particle pParticle, float scaleX, float scaleY) {
		pParticle.setScaleX(scaleX);
		pParticle.setScaleY(scaleY);
	}

	@Override
	protected void onSetValue(Particle pParticle, float scaleX, float scaleY) {
		pParticle.setScaleX(scaleX);
		pParticle.setScaleY(scaleY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================



	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
