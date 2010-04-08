package org.anddev.andengine.entity.particle;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

/**
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
	private boolean mDead = false;

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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(!this.mDead){
			this.mLifeTime += pSecondsElapsed;
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
		this.mDead = false;
		this.mDeathTime = -1;
		this.mLifeTime = 0;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
