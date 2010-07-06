package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;

/**
 * @author Nicolas Gramlich
 * @since 21:59:38 - 06.07.2010
 */
public class RotationAtModifier extends RotationModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final float mRotationCenterX;
	private final float mRotationCenterY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RotationAtModifier(final float pDuration, final float pFromRotation, final float pToRotation, final float pRotationCenterX, final float pRotationCenterY) {
		super(pDuration, pFromRotation, pToRotation);
		this.mRotationCenterX = pRotationCenterX;
		this.mRotationCenterY = pRotationCenterY;
	}

	public RotationAtModifier(final float pDuration, final float pFromRotation, final float pToRotation, final float pRotationCenterX, final float pRotationCenterY, final IModifierListener pModiferListener) {
		super(pDuration, pFromRotation, pToRotation, pModiferListener);
		this.mRotationCenterX = pRotationCenterX;
		this.mRotationCenterY = pRotationCenterY;
	}

	public RotationAtModifier(final RotationAtModifier pRotationAtModifier) {
		super(pRotationAtModifier);
		this.mRotationCenterX = pRotationAtModifier.mRotationCenterX;
		this.mRotationCenterY = pRotationAtModifier.mRotationCenterY; 
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
