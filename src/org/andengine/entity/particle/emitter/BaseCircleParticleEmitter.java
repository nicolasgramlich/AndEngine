package org.andengine.entity.particle.emitter;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 20:14:43 - 01.10.2010
 */
public abstract class BaseCircleParticleEmitter extends BaseParticleEmitter {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mRadiusX;
	protected float mRadiusY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseCircleParticleEmitter(final float pCenterX, final float pCenterY, final float pRadius) {
		this(pCenterX, pCenterY, pRadius, pRadius);
	}

	public BaseCircleParticleEmitter(final float pCenterX, final float pCenterY, final float pRadiusX, final float pRadiusY) {
		super(pCenterX, pCenterY);
		this.setRadiusX(pRadiusX);
		this.setRadiusY(pRadiusY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getRadiusX() {
		return this.mRadiusX;
	}

	public void setRadiusX(final float pRadiusX) {
		this.mRadiusX = pRadiusX;
	}

	public float getRadiusY() {
		return this.mRadiusY;
	}

	public void setRadiusY(final float pRadiusY) {
		this.mRadiusY = pRadiusY;
	}

	public void setRadius(final float pRadius) {
		this.mRadiusX = pRadius;
		this.mRadiusY = pRadius;
	}

	public void setRadius(final float pRadiusX, final float pRadiusY) {
		this.mRadiusX = pRadiusX;
		this.mRadiusY = pRadiusY;
	}

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
