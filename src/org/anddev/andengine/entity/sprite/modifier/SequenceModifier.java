package org.anddev.andengine.entity.sprite.modifier;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.entity.sprite.IModifierListener;
import org.anddev.andengine.entity.sprite.ISpriteModifier;

/**
 * @author Nicolas Gramlich
 * @since 19:39:25 - 19.03.2010
 */
public class SequenceModifier implements ISpriteModifier, IModifierListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private IModifierListener mModiferListener;
	private final BaseModifier[] mSpriteModifiers;
	private int mCurrentSpriteModifier;
	private boolean mExpired;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SequenceModifier(final IModifierListener pModiferListener, final BaseModifier ... pSpriteModifiers) {
		assert(pSpriteModifiers.length > 0);
		
		this.mModiferListener = pModiferListener;
		this.mSpriteModifiers = pSpriteModifiers;

		pSpriteModifiers[0].setModiferListener(this);
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

	public IModifierListener getModiferListener() {
		return this.mModiferListener;
	}

	public void setModiferListener(final IModifierListener pModiferListener) {
		this.mModiferListener = pModiferListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onModifierFinished(final ISpriteModifier pSpriteModifier, final BaseSprite pBaseSprite) {
		this.mCurrentSpriteModifier++;
		if(this.mCurrentSpriteModifier < this.mSpriteModifiers.length) {
			this.mSpriteModifiers[this.mCurrentSpriteModifier].setModiferListener(this);
		} else {
			this.setExpired(true);
			if(this.mModiferListener != null)
				this.mModiferListener.onModifierFinished(this, pBaseSprite);
		}
	}

	@Override
	public void onUpdateSprite(final float pSecondsElapsed, final BaseSprite pBaseSprite) {
		if(!this.isExpired()) {
			this.mSpriteModifiers[this.mCurrentSpriteModifier].onUpdateSprite(pSecondsElapsed, pBaseSprite);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
