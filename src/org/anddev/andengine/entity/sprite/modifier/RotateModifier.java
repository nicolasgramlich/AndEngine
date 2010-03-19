package org.anddev.andengine.entity.sprite.modifier;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.entity.sprite.IModifierListener;

/**
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class RotateModifier extends BaseModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private float mAnglePerSecond;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public RotateModifier(final float pDuration, final float pFromAngle, final float pToAngle) {
		this(pDuration, pFromAngle, pToAngle, null);
	}
	
	public RotateModifier(final float pDuration, final float pFromAngle, final float pToAngle, final IModifierListener pModiferListener) {
		super(pDuration, pModiferListener);
		this.mAnglePerSecond = (pToAngle - pFromAngle) / pDuration;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedUpdateSprite(final float pSecondsElapsed, final BaseSprite pBaseSprite) {
		pBaseSprite.setRotationAngleClockwise(pBaseSprite.getRotationAngleClockwise() + this.mAnglePerSecond * pSecondsElapsed);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
