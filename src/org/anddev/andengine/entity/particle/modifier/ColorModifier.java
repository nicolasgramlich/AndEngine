package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.Particle;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:22:26 - 29.06.2010
 */
public class ColorModifier extends BaseTripleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorModifier(final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue, final float pFromTime, final float pToTime) {
		super(pFromRed, pToRed, pFromGreen, pToGreen, pFromBlue, pToBlue, pFromTime, pToTime);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValues(final Particle pParticle, final float pRed, final float pGreen, final float pBlue) {
		pParticle.setColor(pRed, pGreen, pBlue);
	}

	@Override
	protected void onSetValues(final Particle pParticle, final float pRed, final float pGreen, final float pBlue) {
		pParticle.setColor(pRed, pGreen, pBlue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
