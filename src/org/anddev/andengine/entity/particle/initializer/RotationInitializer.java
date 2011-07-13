package org.anddev.andengine.entity.particle.initializer;

import org.anddev.andengine.entity.particle.Particle;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:17:42 - 29.06.2010
 */
public class RotationInitializer extends BaseSingleValueInitializer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public RotationInitializer(final float pRotation) {
		this(pRotation, pRotation);
	}

	public RotationInitializer(final float pMinRotation, final float pMaxRotation) {
		super(pMinRotation, pMaxRotation);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getMinRotation() {
		return this.mMinValue;
	}

	public float getMaxRotation() {
		return this.mMaxValue;
	}

	public void setRotation(final float pRotation) {
		this.mMinValue = pRotation;
		this.mMaxValue = pRotation;
	}

	public void setRotation(final float pMinRotation, final float pMaxRotation) {
		this.mMinValue = pMinRotation;
		this.mMaxValue = pMaxRotation;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onInitializeParticle(final Particle pParticle, final float pRotation) {
		pParticle.setRotation(pRotation);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
