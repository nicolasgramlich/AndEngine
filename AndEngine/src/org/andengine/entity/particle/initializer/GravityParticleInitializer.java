package org.andengine.entity.particle.initializer;

import org.andengine.entity.IEntity;

import android.hardware.SensorManager;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:04:00 - 15.03.2010
 */
public class GravityParticleInitializer<T extends IEntity> extends AccelerationParticleInitializer<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public GravityParticleInitializer() {
		super(0, SensorManager.GRAVITY_EARTH);
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
