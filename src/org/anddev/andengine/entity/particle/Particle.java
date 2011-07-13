package org.anddev.andengine.entity.particle;

import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:37:13 - 14.03.2010
 */
public class Particle extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mLifeTime;
	private float mDeathTime = -1;
	boolean mDead = false;
	private final PhysicsHandler mPhysicsHandler = new PhysicsHandler(this);

	// ===========================================================
	// Constructors
	// ===========================================================;

	public Particle(final float pX, final float pY, final TextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);
		this.mLifeTime = 0;
	}

	public Particle(final float pX, final float pY, final TextureRegion pTextureRegion, final RectangleVertexBuffer pRectangleVertexBuffer) {
		super(pX, pY, pTextureRegion, pRectangleVertexBuffer);
		this.mLifeTime = 0;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

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

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(!this.mDead){
			this.mLifeTime += pSecondsElapsed;
			this.mPhysicsHandler.onUpdate(pSecondsElapsed);
			super.onManagedUpdate(pSecondsElapsed);
			final float deathTime = this.mDeathTime;
			if(deathTime != -1 && this.mLifeTime > deathTime) {
				this.setDead(true);
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void reset() {
		super.reset();
		this.mPhysicsHandler.reset();
		this.mDead = false;
		this.mDeathTime = -1;
		this.mLifeTime = 0;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
