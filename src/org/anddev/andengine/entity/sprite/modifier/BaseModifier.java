package org.anddev.andengine.entity.sprite.modifier;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.entity.sprite.IModifierListener;
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
	private IModifierListener mModiferListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseModifier() {
		this(-1, null);
	}

	public BaseModifier(final float pDuration) {
		this(pDuration, null);
	}

	public BaseModifier(final float pDuration, final IModifierListener pModiferListener) {
		this.mDuration = pDuration;
		this.mModiferListener = pModiferListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isExpired() {
		return this.mExpired;
	}

	public void setExpired(final boolean pExpired) {
		this.mExpired = pExpired;
	}

	protected float getTotalSecondsElapsed() {
		return this.mTotalSecondsElapsed;
	}

	public IModifierListener getModiferListener() {
		return this.mModiferListener;
	}

	public void setModiferListener(final IModifierListener pModiferListener) {
		this.mModiferListener = pModiferListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onManagedUpdateSprite(final float pSecondsElapsed, final BaseSprite pBaseSprite);

	protected abstract void onManagedInitializeSprite(final BaseSprite pBaseSprite);

	@Override
	public final void onUpdateSprite(final float pSecondsElapsed, final BaseSprite pBaseSprite) {
		if(!this.isExpired()){
			if(this.mTotalSecondsElapsed == 0) {
				this.onManagedInitializeSprite(pBaseSprite);
			}

			final float secondsToElapse;
			if(this.mTotalSecondsElapsed + pSecondsElapsed < this.mDuration) {
				secondsToElapse = pSecondsElapsed;
			} else {
				secondsToElapse = this.mDuration - this.mTotalSecondsElapsed;
			}

			this.mTotalSecondsElapsed += secondsToElapse;
			this.onManagedUpdateSprite(secondsToElapse, pBaseSprite);

			if(this.mDuration != -1 && this.mTotalSecondsElapsed >= this.mDuration) {
				this.mTotalSecondsElapsed = this.mDuration;
				this.setExpired(true);
				if(this.mModiferListener != null) {
					this.mModiferListener.onModifierFinished(this, pBaseSprite);
				}
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset() {
		this.mExpired = false;
		this.mTotalSecondsElapsed = 0;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
