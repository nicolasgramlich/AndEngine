package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.Particle;

/**
 * @author Nicolas Gramlich
 * @since 10:17:42 - 29.06.2010
 */
public class RotateInitialize extends BaseInitializer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public RotateInitialize(final float pRotation) {
		this(pRotation, pRotation);
	}

	public RotateInitialize(final float pMinRotation, final float pMaxRotation) {
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
	public void onInitializeParticle(final Particle pParticle, final float pValue) {
		pParticle.setRotation(pValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
