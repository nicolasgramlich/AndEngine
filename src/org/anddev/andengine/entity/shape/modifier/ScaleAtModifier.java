package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;

/**
 * @author Nicolas Gramlich
 * @since 21:53:30 - 06.07.2010
 */
public class ScaleAtModifier extends ScaleModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mScaleCenterX;
	private float mScaleCenterY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScaleAtModifier(final float pDuration, final float pFromScale, final float pToScale, float pScaleCenterX, float pScaleCenterY) {
		super(pDuration, pFromScale, pToScale);
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;
	}

	public ScaleAtModifier(final float pDuration, final float pFromScale, final float pToScale, float pScaleCenterX, float pScaleCenterY, final IModifierListener pModiferListener) {
		super(pDuration, pFromScale, pToScale, pModiferListener);
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;
	}

	public ScaleAtModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, float pScaleCenterX, float pScaleCenterY) {
		super(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY);
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;
	}

	public ScaleAtModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, float pScaleCenterX, float pScaleCenterY, final IModifierListener pModiferListener) {
		super(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pModiferListener);
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;
	}

	public ScaleAtModifier(final ScaleAtModifier pScaleAtModifier) {
		super(pScaleAtModifier);
		this.mScaleCenterX = pScaleAtModifier.mScaleCenterX;
		this.mScaleCenterY = pScaleAtModifier.mScaleCenterY;
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
