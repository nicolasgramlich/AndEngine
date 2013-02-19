package org.andengine.entity.particle;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.IEntity;
import org.andengine.opengl.util.GLState;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 19:37:13 - 14.03.2010
 */
public class Particle<T extends IEntity> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int EXPIRETIME_NEVER = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	private final PhysicsHandler mPhysicsHandler = new PhysicsHandler(null);

	private float mLifeTime;
	private float mExpireTime = Particle.EXPIRETIME_NEVER;
	boolean mExpired;

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

	public float getExpireTime() {
		return this.mExpireTime;
	}

	public void setExpireTime(final float pExpireTime) {
		this.mExpireTime = pExpireTime;
	}

	public boolean isExpired() {
		return this.mExpired;
	}

	public void setExpired(final boolean pExpired) {
		this.mExpired = pExpired;
	}

	public PhysicsHandler getPhysicsHandler() {
		return this.mPhysicsHandler;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected void onUpdate(final float pSecondsElapsed) {
		if (!this.mExpired) {
			if (this.mExpireTime == Particle.EXPIRETIME_NEVER || this.mLifeTime + pSecondsElapsed < this.mExpireTime) {
				/* Particle doesn't expire or didn't expire yet. */
				this.mLifeTime += pSecondsElapsed;
				this.mEntity.onUpdate(pSecondsElapsed);
				this.mPhysicsHandler.onUpdate(pSecondsElapsed);
			} else {
				final float secondsElapsedUsed = this.mExpireTime - this.mLifeTime;
				this.mLifeTime = this.mExpireTime;
				this.mEntity.onUpdate(secondsElapsedUsed);
				this.mPhysicsHandler.onUpdate(secondsElapsedUsed);
				this.setExpired(true);
			}
		}
	}

	public void onDraw(final GLState pGLState, final Camera pCamera) {
		if (!this.mExpired) {
			this.mEntity.onDraw(pGLState, pCamera);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset() {
		this.mEntity.reset();
		this.mPhysicsHandler.reset();
		this.mExpired = false;
		this.mExpireTime = Particle.EXPIRETIME_NEVER;
		this.mLifeTime = 0;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
