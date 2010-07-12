package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;


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

	public ScaleAtModifier(final float pDuration, final float pFromScale, final float pToScale, float pScaleCenterX, float pScaleCenterY, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pFromScale, pToScale, pShapeModiferListener);
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;
	}

	public ScaleAtModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, float pScaleCenterX, float pScaleCenterY) {
		super(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY);
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;
	}

	public ScaleAtModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, float pScaleCenterX, float pScaleCenterY, final IShapeModifierListener pShapeModiferListener) {
		super(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pShapeModiferListener);
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;
	}

	protected ScaleAtModifier(final ScaleAtModifier pScaleAtModifier) {
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
	
	@Override
	protected void onManagedInitializeShape(IShape pShape) {
		super.onManagedInitializeShape(pShape);
		pShape.setScaleCenter(this.mScaleCenterX, this.mScaleCenterY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
