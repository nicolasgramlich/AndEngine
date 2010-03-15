package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.entity.particle.IParticleModifier;
import org.anddev.andengine.entity.particle.Particle;

/**
 * @author Nicolas Gramlich
 * @since 21:21:10 - 14.03.2010
 */
public class VelocityModifier implements IParticleModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mMinVelocityX;
	private float mMaxVelocityX;
	private float mMinVelocityY;
	private float mMaxVelocityY;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public VelocityModifier(final float pVelocityX, final float pVelocityY) {
		this(pVelocityX, pVelocityX, pVelocityY, pVelocityY);
	}

	public VelocityModifier(final float pMinVelocityX, final float pMaxVelocityX, final float pMinVelocityY, final float pMaxVelocityY) {
		this.mMinVelocityX = pMinVelocityX;
		this.mMaxVelocityX = pMaxVelocityX;
		this.mMinVelocityY = pMinVelocityY;
		this.mMaxVelocityY = pMaxVelocityY;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getMinVelocityX() {
		return this.mMinVelocityX;
	}

	public float getMaxVelocityX() {
		return this.mMaxVelocityX;
	}

	public float getMinVelocityY() {
		return this.mMinVelocityY;
	}

	public float getMaxVelocityY() {
		return this.mMaxVelocityY;
	}	
	
	public void setVelocityX(final float pVelocityX) {
		this.mMinVelocityX = pVelocityX;
		this.mMaxVelocityX = pVelocityX;
	}
	
	public void setVelocityY(final float pVelocityY) {
		this.mMinVelocityY = pVelocityY;
		this.mMaxVelocityY = pVelocityY;
	}
	
	public void setVelocityX(final float pMinVelocityX, final float pMaxVelocityX) {
		this.mMinVelocityX = pMinVelocityX;
		this.mMaxVelocityX = pMaxVelocityX;
	}
	
	public void setVelocityY(final float pMinVelocityY, final float pMaxVelocityY) {
		this.mMinVelocityY = pMinVelocityY;
		this.mMaxVelocityY = pMaxVelocityY;
	}
	
	public void setVelocity(final float pMinVelocityX, final float pMaxVelocityX, final float pMinVelocityY, final float pMaxVelocityY) {
		this.mMinVelocityX = pMinVelocityX;
		this.mMaxVelocityX = pMaxVelocityX;
		this.mMinVelocityY = pMinVelocityY;
		this.mMaxVelocityY = pMaxVelocityY;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onInitializeParticle(final Particle pParticle) {
		pParticle.setVelocity(determineVelocityX(), determineVelocityY());
	}

	@Override
	public void onUpdateParticle(final Particle pParticle) {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	private float determineVelocityX() {
		if(this.mMinVelocityX == this.mMaxVelocityX)
			return this.mMaxVelocityX;
		else
			return (float)Math.random() * (this.mMaxVelocityX - this.mMinVelocityX) + this.mMinVelocityX;
	}
	
	private float determineVelocityY() {
		if(this.mMinVelocityY == this.mMaxVelocityY)
			return this.mMaxVelocityY;
		else
			return (float)Math.random() * (this.mMaxVelocityY - this.mMinVelocityY) + this.mMinVelocityY;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
