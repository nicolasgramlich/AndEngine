package org.anddev.andengine.entity.sprite.modifier;

import org.anddev.andengine.entity.sprite.BaseSprite;
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

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isExpired() {
		return this.mExpired;
	}

	public void setExpired(final boolean pExpired) {
		this.mExpired = pExpired;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onManagedUpdateSprite(final float pSecondsElapsed, final BaseSprite pBaseSprite);

	@Override
	public final void onUpdateSprite(final float pSecondsElapsed, final BaseSprite pBaseSprite) {
		if(!this.isExpired()){
			
			onManagedUpdateSprite(pSecondsElapsed, pBaseSprite);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
