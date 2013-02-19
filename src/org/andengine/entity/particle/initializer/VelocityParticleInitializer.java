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
public class VelocityParticleInitializer<T extends IEntity> extends BaseDoubleValueParticleInitializer<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public VelocityParticleInitializer(final float pVelocity) {
		this(pVelocity, pVelocity, pVelocity, pVelocity);
	}

	public VelocityParticleInitializer(final float pVelocityX, final float pVelocityY) {
		this(pVelocityX, pVelocityX, pVelocityY, pVelocityY);
	}

	public VelocityParticleInitializer(final float pMinVelocityX, final float pMaxVelocityX, final float pMinVelocityY, final float pMaxVelocityY) {
		super(pMinVelocityX, pMaxVelocityX, pMinVelocityY, pMaxVelocityY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getMinVelocityX() {
		return this.mMinValue;
	}

	public float getMaxVelocityX() {
		return this.mMaxValue;
	}

	public float getMinVelocityY() {
		return this.mMinValueB;
	}

	public float getMaxVelocityY() {
		return this.mMaxValueB;
	}

	public void setVelocityX(final float pVelocityX) {
		this.mMinValue = pVelocityX;
		this.mMaxValue = pVelocityX;
	}

	public void setVelocityY(final float pVelocityY) {
		this.mMinValueB = pVelocityY;
		this.mMaxValueB = pVelocityY;
	}

	public void setVelocityX(final float pMinVelocityX, final float pMaxVelocityX) {
		this.mMinValue = pMinVelocityX;
		this.mMaxValue = pMaxVelocityX;
	}

	public void setVelocityY(final float pMinVelocityY, final float pMaxVelocityY) {
		this.mMinValueB = pMinVelocityY;
		this.mMaxValueB = pMaxVelocityY;
	}

	public void setVelocity(final float pMinVelocityX, final float pMaxVelocityX, final float pMinVelocityY, final float pMaxVelocityY) {
		this.mMinValue = pMinVelocityX;
		this.mMaxValue = pMaxVelocityX;
		this.mMinValueB = pMinVelocityY;
		this.mMaxValueB = pMaxVelocityY;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onInitializeParticle(final Particle<T> pParticle, final float pVelocityX, final float pVelocityY) {
		pParticle.getPhysicsHandler().setVelocity(pVelocityX, pVelocityY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
