package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class AccelerationParticleInitializer<T extends IEntity> extends BaseDoubleValueParticleInitializer<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AccelerationParticleInitializer(final float pAcceleration) {
		this(pAcceleration, pAcceleration);
	}

	public AccelerationParticleInitializer(final float pAccelerationX, final float pAccelerationY) {
		this(pAccelerationX, pAccelerationX, pAccelerationY, pAccelerationY);
	}

	public AccelerationParticleInitializer(final float pMinAccelerationX, final float pMaxAccelerationX, final float pMinAccelerationY, final float pMaxAccelerationY) {
		super(pMinAccelerationX, pMaxAccelerationX, pMinAccelerationY, pMaxAccelerationY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getMinAccelerationX() {
		return this.mMinValue;
	}

	public float getMaxAccelerationX() {
		return this.mMaxValue;
	}

	public float getMinAccelerationY() {
		return this.mMinValueB;
	}

	public float getMaxAccelerationY() {
		return this.mMaxValueB;
	}

	public void setAccelerationX(final float pAccelerationX) {
		this.mMinValue = pAccelerationX;
		this.mMaxValue = pAccelerationX;
	}

	public void setAccelerationY(final float pAccelerationY) {
		this.mMinValueB = pAccelerationY;
		this.mMaxValueB = pAccelerationY;
	}

	public void setAccelerationX(final float pMinAccelerationX, final float pMaxAccelerationX) {
		this.mMinValue = pMinAccelerationX;
		this.mMaxValue = pMaxAccelerationX;
	}

	public void setAccelerationY(final float pMinAccelerationY, final float pMaxAccelerationY) {
		this.mMinValueB = pMinAccelerationY;
		this.mMaxValueB = pMaxAccelerationY;
	}

	public void setAcceleration(final float pMinAccelerationX, final float pMaxAccelerationX, final float pMinAccelerationY, final float pMaxAccelerationY) {
		this.mMinValue = pMinAccelerationX;
		this.mMaxValue = pMaxAccelerationX;
		this.mMinValueB = pMinAccelerationY;
		this.mMaxValueB = pMaxAccelerationY;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onInitializeParticle(final Particle<T> pParticle, final float pAccelerationX, final float pAccelerationY) {
		pParticle.getPhysicsHandler().accelerate(pAccelerationX, pAccelerationY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
