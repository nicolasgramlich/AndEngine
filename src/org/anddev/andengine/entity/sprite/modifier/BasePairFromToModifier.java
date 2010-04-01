package org.anddev.andengine.entity.sprite.modifier;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.entity.sprite.IModifierListener;

/**
 * @author Nicolas Gramlich
 * @since 23:29:22 - 19.03.2010
 */
public abstract class BasePairFromToModifier extends BaseFromToModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mValuePerSecondB;
	private final float mFromValueB;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BasePairFromToModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB) {
		this(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, null);
	}

	public BasePairFromToModifier(final float pDuration, final float pFromValueA, final float pToValueA, final float pFromValueB, final float pToValueB, final IModifierListener pModiferListener) {
		super(pDuration, pFromValueA, pToValueA, pModiferListener);
		this.mFromValueB = pFromValueB;
		this.mValuePerSecondB = (pToValueB - pFromValueB) / pDuration;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSetInitialValues(final float pValueA, final float pValueB, final BaseSprite pBaseSprite);
	protected abstract void onSetValues(final float pValueA, final float pValueB, final BaseSprite pBaseSprite);

	protected final void onSetInitialValue(final float pValueA, final BaseSprite pBaseSprite) {
		this.onSetInitialValues(pValueA, this.mFromValueB, pBaseSprite);
	}
	
	protected final void onSetValue(final float pValueA, final BaseSprite pBaseSprite) {
		this.onSetValues(pValueA, this.mFromValueB + this.getTotalSecondsElapsed() * this.mValuePerSecondB, pBaseSprite);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
