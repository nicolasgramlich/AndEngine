package org.anddev.andengine.entity.particle;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.entity.Entity;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:37:13 - 14.03.2010
 */
public class Particle<T extends Entity> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final PhysicsHandler mPhysicsHandler = new PhysicsHandler(null);

	private float mLifeTime;
	private float mDeathTime = -1;
	boolean mDead;

	private T mEntity;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public T getEntity() {
		return this.mEntity;
	}

	public void setEntity(final T pEntity) {
		this.mEntity = pEntity;
		this.mPhysicsHandler.setEntity(pEntity);
	}

	public float getLifeTime() {
		return this.mLifeTime;
	}

	public float getDeathTime() {
		return this.mDeathTime;
	}

	public void setDeathTime(final float pDeathTime) {
		this.mDeathTime = pDeathTime;
	}

	public boolean isDead() {
		return this.mDead ;
	}

	public void setDead(final boolean pDead) {
		this.mDead = pDead;
	}

	public PhysicsHandler getPhysicsHandler() {
		return this.mPhysicsHandler;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected void onUpdate(final float pSecondsElapsed) {
		if(!this.mDead){
			this.mLifeTime += pSecondsElapsed;
			this.mPhysicsHandler.onUpdate(pSecondsElapsed);
			final float deathTime = this.mDeathTime;
			if(deathTime != -1 && this.mLifeTime > deathTime) {
				this.setDead(true);
			}
		}
	}

	public void onDraw(final Camera pCamera) {
		if(!this.mDead) {
			this.mEntity.onDraw(pCamera);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset() {
		this.mEntity.reset();
		this.mPhysicsHandler.reset();
		this.mDead = false;
		this.mDeathTime = -1;
		this.mLifeTime = 0;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
