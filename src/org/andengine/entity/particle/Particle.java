package org.andengine.entity.particle;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.IEntity;
import org.andengine.opengl.util.GLState;

/**
 * A {@code Particle} is emitted by a {@link ParticleSystem}. Each particle
 * has generic type, that represents its content. The {@code Particle) class
 * itself holds only the information about the particle, but not about the actual
 * {@link IEntity} content, that should be drawn to the screen. 
 * <p>
 * If you just want to create a {@code ParticleSystem} in your game, you normally
 * shouldn't need to use this class directly.
 * <p>
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @see ParticleSystem
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

	/**
	 * Returns the actual entity, that is drawn on the screen.
	 * 
	 * @return The actual entity.
	 */
	public T getEntity() {
		return this.mEntity;
	}

	/**
	 * Sets the {@link IEntity} for this particle.
	 * 
	 * @param pEntity The new {@link IEntity}.
	 */
	public void setEntity(final T pEntity) {
		this.mEntity = pEntity;
		this.mPhysicsHandler.setEntity(pEntity);
	}

	/**
	 * Returns the lifetime of the particle.
	 * 
	 * @return Lifetime of the particle in seconds.
	 */
	public float getLifeTime() {
		return this.mLifeTime;
	}

	/**
	 * Returns the expiration time of this particle. This is not the remaining
	 * lifetime of the particle, but the overall expiration time from creation
	 * of the particle.
	 * 
	 * @return The expiration time of this particle in seconds.
	 */
	public float getExpireTime() {
		return this.mExpireTime;
	}

	/**
	 * Sets the expiration time of this particle.
	 * 
	 * @param pExpireTime Expiration time of particle in seconds.
	 */
	public void setExpireTime(final float pExpireTime) {
		this.mExpireTime = pExpireTime;
	}

	/**
	 * Returns whether the particle has been expired. An expired particle won't
	 * be drawn to the screen.
	 * 
	 * @return Whether the particle has been expired.
	 */
	public boolean isExpired() {
		return this.mExpired ;
	}

	/**
	 * Sets whether the particle is expired. An expired particle won't be drawn
	 * to the screen.
	 * 
	 * @param pExpired Whether the particle is expired.
	 */
	public void setExpired(final boolean pExpired) {
		this.mExpired = pExpired;
	}

	/**
	 * Returns the {@link PhysicsHandler} of the particle.
	 * 
	 * @return The {@link PhysicsHandler} of the particle.
	 */
	public PhysicsHandler getPhysicsHandler() {
		return this.mPhysicsHandler;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected void onUpdate(final float pSecondsElapsed) {
		if(!this.mExpired){
			if(this.mExpireTime == Particle.EXPIRETIME_NEVER || this.mLifeTime + pSecondsElapsed < this.mExpireTime) {
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
		if(!this.mExpired) {
			this.mEntity.onDraw(pGLState, pCamera);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Resets the particle.
	 */
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
