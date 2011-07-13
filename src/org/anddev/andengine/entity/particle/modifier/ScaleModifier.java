package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.Particle;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	public ScaleModifier(final float pFromScale, final float pToScale, final float pFromTime, final float pToTime) {
		this(pFromScale, pToScale, pFromScale, pToScale, pFromTime, pToTime);
	}

	public ScaleModifier(final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final float pFromTime, final float pToTime) {
		super(pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pFromTime, pToTime);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValues(final Particle pParticle, final float pScaleX, final float pScaleY) {
		pParticle.setScale(pScaleX, pScaleY);
	}

	@Override
	protected void onSetValues(final Particle pParticle, final float pScaleX, final float pScaleY) {
		pParticle.setScale(pScaleX, pScaleY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
