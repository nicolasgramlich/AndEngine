package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;


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

	private final float mScaleCenterX;
	private final float mScaleCenterY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScaleAtModifier(final float pDuration, final float pFromScale, final float pToScale, final float pScaleCenterX, final float pScaleCenterY) {
		this(pDuration, pFromScale, pToScale, pScaleCenterX, pScaleCenterY, IEaseFunction.DEFAULT);
	}

	public ScaleAtModifier(final float pDuration, final float pFromScale, final float pToScale, final float pScaleCenterX, final float pScaleCenterY, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromScale, pToScale, pScaleCenterX, pScaleCenterY, null, pEaseFunction);
	}

	public ScaleAtModifier(final float pDuration, final float pFromScale, final float pToScale, final float pScaleCenterX, final float pScaleCenterY, final IShapeModifierListener pShapeModiferListener) {
		this(pDuration, pFromScale, pToScale, pScaleCenterX, pScaleCenterY, pShapeModiferListener, IEaseFunction.DEFAULT);
	}

	public ScaleAtModifier(final float pDuration, final float pFromScale, final float pToScale, final float pScaleCenterX, final float pScaleCenterY, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromScale, pToScale, pFromScale, pToScale, pScaleCenterX, pScaleCenterY, pShapeModiferListener, pEaseFunction);
	}

	public ScaleAtModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final float pScaleCenterX, final float pScaleCenterY) {
		this(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pScaleCenterX, pScaleCenterY, IEaseFunction.DEFAULT);
	}

	public ScaleAtModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final float pScaleCenterX, final float pScaleCenterY, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pScaleCenterX, pScaleCenterY, null, pEaseFunction);
	}

	public ScaleAtModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final float pScaleCenterX, final float pScaleCenterY, final IShapeModifierListener pShapeModiferListener) {
		this(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pScaleCenterX, pScaleCenterY, pShapeModiferListener, IEaseFunction.DEFAULT);
	}

	public ScaleAtModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final float pScaleCenterX, final float pScaleCenterY, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pShapeModiferListener, pEaseFunction);
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;
	}

	protected ScaleAtModifier(final ScaleAtModifier pScaleAtModifier) {
		super(pScaleAtModifier);
		this.mScaleCenterX = pScaleAtModifier.mScaleCenterX;
		this.mScaleCenterY = pScaleAtModifier.mScaleCenterY;
	}

	@Override
	public ScaleAtModifier clone(){
		return new ScaleAtModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedInitialize(final IShape pShape) {
		super.onManagedInitialize(pShape);
		pShape.setScaleCenter(this.mScaleCenterX, this.mScaleCenterY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
