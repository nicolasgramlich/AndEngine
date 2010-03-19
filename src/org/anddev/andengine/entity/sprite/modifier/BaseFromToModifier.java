package org.anddev.andengine.entity.sprite.modifier;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.entity.sprite.IModifierListener;

/**
 * @author Nicolas Gramlich
 * @since 23:29:22 - 19.03.2010
 */
public abstract class BaseFromToModifier extends BaseModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private float mValuePerSecond;
	private final float mFromValue;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public BaseFromToModifier(final float pDuration, final float pFromValue, final float pToValue) {
		this(pDuration, pFromValue, pToValue, null);
	}
	
	public BaseFromToModifier(final float pDuration, final float pFromValue, final float pToValue, final IModifierListener pModiferListener) {
		super(pDuration, pModiferListener);
		this.mFromValue = pFromValue;
		this.mValuePerSecond = (pToValue - pFromValue) / pDuration;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	protected abstract void onSetInitialValue(final float pValue, final BaseSprite pBaseSprite);
	protected abstract void onSetValue(final float pValue, final BaseSprite pBaseSprite);
	
	@Override
	protected void onManagedInitializeSprite(final BaseSprite pBaseSprite) {
		onSetInitialValue(this.mFromValue, pBaseSprite);
	}

	@Override
	protected void onManagedUpdateSprite(final float pSecondsElapsed, final BaseSprite pBaseSprite) {
		onSetValue(this.mFromValue + this.getTotalSecondsElapsed() * this.mValuePerSecond, pBaseSprite);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
