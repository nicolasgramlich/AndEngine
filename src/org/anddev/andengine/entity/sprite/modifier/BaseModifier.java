package org.anddev.andengine.entity.sprite.modifier;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.entity.sprite.IModificationListener;
import org.anddev.andengine.entity.sprite.ISpriteModifier;

/**
 * @author Nicolas Gramlich
 * @since 16:10:42 - 19.03.2010
 */
public abstract class BaseModifier implements ISpriteModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mExpired;
	private float mTotalSecondsElapsed;
	private final float mDuration;
	private final IModificationListener mModificationListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public BaseModifier(final float pDuration) {
		this(pDuration, null);
	}
	
	public BaseModifier(final float pDuration, final IModificationListener pModificationListener) {
		this.mDuration = pDuration;
		this.mModificationListener = pModificationListener;
	}

	public boolean isExpired() {
		return this.mExpired;
	}

	public void setExpired(final boolean pExpired) {
		this.mExpired = pExpired;
	}
	
	protected float getTotalSecondsElapsed() {
		return this.mTotalSecondsElapsed;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onManagedUpdateSprite(final float pSecondsElapsed, final BaseSprite pBaseSprite);

	@Override
	public final void onUpdateSprite(final float pSecondsElapsed, final BaseSprite pBaseSprite) {
		if(!this.isExpired()){

			final float secondsToElapse;
			if(this.mTotalSecondsElapsed + pSecondsElapsed < this.mDuration) {
				secondsToElapse = pSecondsElapsed;
			} else {
				secondsToElapse = this.mDuration - this.mTotalSecondsElapsed;
			}

			this.mTotalSecondsElapsed += secondsToElapse;
			this.onManagedUpdateSprite(secondsToElapse, pBaseSprite);
			
			if(this.mTotalSecondsElapsed >= this.mDuration) {
				this.mTotalSecondsElapsed = this.mDuration;
				this.setExpired(true);
				if(this.mModificationListener != null)
					this.mModificationListener.onFinished(this, pBaseSprite);
			} 
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
