package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.IParticleModifier;
import org.anddev.andengine.entity.particle.Particle;

/**
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class AccelerationModifier implements IParticleModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mMinAccelerationX;
	private final float mMaxAccelerationX;
	private final float mMinAccelerationY;
	private final float mMaxAccelerationY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AccelerationModifier(final float pAcceleration) {
		this(pAcceleration, pAcceleration);
	}

	public AccelerationModifier(final float pAccelerationX, final float pAccelerationY) {
		this(pAccelerationX, pAccelerationX, pAccelerationY, pAccelerationY);
	}

	public AccelerationModifier(final float pMinAccelerationX, final float pMaxAccelerationX, final float pMinAccelerationY, final float pMaxAccelerationY) {
		this.mMinAccelerationX = pMinAccelerationX;
		this.mMaxAccelerationX = pMaxAccelerationX;
		this.mMinAccelerationY = pMinAccelerationY;
		this.mMaxAccelerationY = pMaxAccelerationY;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onInitializeParticle(final Particle pParticle) {
		pParticle.accelerate(this.determineAccelerationX(), this.determineAccelerationY());
	}

	@Override
	public void onUpdateParticle(final Particle pParticle) {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	private float determineAccelerationX() {
		if(this.mMinAccelerationX == this.mMaxAccelerationX) {
			return this.mMaxAccelerationX;
		} else {
			return (float)Math.random() * (this.mMaxAccelerationX - this.mMinAccelerationX) + this.mMinAccelerationX;
		}
	}

	private float determineAccelerationY() {
		if(this.mMinAccelerationY == this.mMaxAccelerationY) {
			return this.mMaxAccelerationY;
		} else {
			return (float)Math.random() * (this.mMaxAccelerationY - this.mMinAccelerationY) + this.mMinAccelerationY;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
