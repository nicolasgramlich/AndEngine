package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Nicolas Gramlich
 * @since 23:37:53 - 19.03.2010
 */
public class ScaleModifier extends BaseDoubleValueSpanModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public ScaleModifier(float pDuration, float pFromValueA, float pToValueA, float pFromValueB, float pToValueB) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB);
	}

	public ScaleModifier(float pDuration, float pFromValueA, float pToValueA, float pFromValueB, float pToValueB, IModifierListener pModiferListener) {
		super(pDuration, pFromValueA, pToValueA, pFromValueB, pToValueB, pModiferListener);
	}

	public ScaleModifier(final ScaleModifier pScaleModifier) {
		super(pScaleModifier);
	}
	
	@Override
	public ScaleModifier clone(){
		return new ScaleModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected void onSetInitialValues(final Shape pShape, float pScaleX, float pScaleY) {
		pShape.setScaleX(pScaleX);
		pShape.setScaleY(pScaleY);
	}

	protected void onSetValues(final Shape pShape, float pScaleX, float pScaleY) {
		pShape.setScaleX(pScaleX);
		pShape.setScaleY(pScaleY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
