package org.anddev.andengine.entity.sprite.modifier;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.entity.sprite.IModifierListener;

/**
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class RotateByModifier extends BaseModifier {
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
	
	public RotateByModifier(final float pDuration, final float pAngle) {
		this(pDuration, pAngle, null);
	}
	
	public RotateByModifier(final float pDuration, final float pAngle, final IModifierListener pModiferListener) {
		super(pDuration, pModiferListener);
		this.mAnglePerSecond = pAngle / pDuration;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	protected void onManagedInitializeSprite(final BaseSprite pBaseSprite) {
		
	}

	@Override
	protected void onManagedUpdateSprite(final float pSecondsElapsed, final BaseSprite pBaseSprite) {
		pBaseSprite.setAngle(pBaseSprite.getAngle() + this.mAnglePerSecond * pSecondsElapsed);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
