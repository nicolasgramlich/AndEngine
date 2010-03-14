package org.anddev.andengine.entity.particle;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.TextureRegion;

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

	private long mBirthTime;
	private long mDeathTime = -1;
	private boolean mDead = false;

	// ===========================================================
	// Constructors
	// ===========================================================;

	public Particle(final float pX, final float pY, final TextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);
		this.mBirthTime = System.nanoTime();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public long getBirthTime() {
		return this.mBirthTime;
	}
	
	public long getDeathTime() {
		return this.mDeathTime;
	}

	public void setDeathTime(final long pDeathTime) {
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
	protected void onManagedUpdate(float pSecondsElapsed) {
		if(!this.isDead()){
			super.onManagedUpdate(pSecondsElapsed);
			if(this.mDeathTime != -1 && System.nanoTime() > this.mDeathTime)
				this.setDead(true);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset() {
		super.reset();
		this.setDead(false);
		this.mDeathTime = -1;
		this.mBirthTime = System.nanoTime();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
